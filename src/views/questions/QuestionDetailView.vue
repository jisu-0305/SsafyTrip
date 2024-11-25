<script setup>
import { onMounted, onUnmounted, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { storeToRefs } from 'pinia';
import { useQuestionStore } from "@/stores/questionStores";
import { useAuthStore } from '@/stores/authStores';
import { useLoadingStore } from '@/stores/loadingStore';
import BoardDetail from "@/components/board/BoardDetail.vue";
import PageHeader from "@/components/common/PageHeader.vue";

const route = useRoute();
const router = useRouter();
const questionStore = useQuestionStore();
const { currentQuestion } = storeToRefs(questionStore);
const authStore = useAuthStore();
const loadingStore = useLoadingStore();

let isComponentMounted = true;

onMounted(async () => {
  try {
    if (isComponentMounted) {
      loadingStore.startLoading('question-detail');
      await questionStore.fetchQuestionById(route.params.id);
    }
  } catch (error) {
    if (isComponentMounted) {
      console.error('문의글 로딩 실패:', error);
    }
  } finally {
    if (isComponentMounted) {
      loadingStore.endLoading('question-detail');
    }
  }
});

onUnmounted(() => {
  isComponentMounted = false;
});

const formattedQuestion = computed(() => {
  if (!currentQuestion.value) return null;
  
  const question = {
    id: currentQuestion.value.questionId,
    title: currentQuestion.value.questionTitle,
    content: currentQuestion.value.questionContent,
    createdAt: new Date(currentQuestion.value.questionCreatedAt).toLocaleDateString('ko-KR'),
    status: currentQuestion.value.questionIsAnswered ? 'ANSWERED' : 'WAITING',
    authorEmail: currentQuestion.value.questionAuthorEmail
  };

  if (currentQuestion.value.answerContent) {
    question.answer = {
      id: currentQuestion.value.answerId,
      content: currentQuestion.value.answerContent,
      createdAt: new Date(currentQuestion.value.answerCreatedAt).toLocaleDateString('ko-KR'),
      authorEmail: currentQuestion.value.answerAuthorEmail
    };
  }

  return question;
});

const isOwner = computed(() => {
  return currentQuestion.value?.questionAuthorEmail === authStore.user?.email;
});

const isAnswered = computed(() => {
  return currentQuestion.value?.questionIsAnswered;
});

const buttonPermissions = computed(() => {
  if (authStore.isAdmin) {
    // 관리자는 답변이 없는 경우에만 답변 작성 버튼이 보임
    return {
      answer: !isAnswered.value,
      list: true
    };
  } else {
    // 일반 사용자는 자신의 글이고 답변이 없는 경우에만 수정/삭제 가능
    return {
      edit: isOwner.value && !isAnswered.value,
      delete: isOwner.value && !isAnswered.value,
      list: true
    };
  }
});

const buttonLabels = computed(() => {
  if (authStore.isAdmin) {
    return {
      answer: '답변작성',
      list: '목록'
    };
  } else {
    return {
      edit: '수정',
      delete: '삭제',
      list: '목록'
    };
  }
});

const handleButtonClick = async (buttonType) => {
  switch (buttonType) {
    case 'edit':
      alert('수정 기능은 준비 중입니다.');
      break;
    case 'delete':
      handleDelete();
      break;
    case 'answer':
      const questionId = route.params.id;
      router.push({ 
        name: 'question-answer', 
        params: { id: questionId } 
      });
      break;
    case 'list':
      router.push({ name: 'question' });
      break;
  }
};

const handleDelete = async () => {
  if (confirm('이 문의글을 삭제하시겠습니까?')) {
    alert('삭제 기능은 준비 중입니다.');
  }
};

const additionalFields = [
  { 
    key: 'status', 
    label: '상태',
    formatter: (value) => value === 'ANSWERED' ? '답변완료' : '답변대기'
  },
  { 
    key: 'authorEmail', 
    label: '작성자' 
  }
];
</script>

<template>
  <v-container fluid class="page-container">
    <v-row justify="center">
      <v-col cols="12" class="content-wrapper">
        <div class="inner-content">
          <PageHeader title="문의글 상세" icon="mdi-help-circle" />
          <div class="content-area">
            <BoardDetail
              :article="formattedQuestion"
              loading-key="question-detail"
              :additional-fields="additionalFields"
              :buttons="authStore.isAdmin ? ['answer', 'list'] : ['edit', 'delete', 'list']"
              :button-permissions="buttonPermissions"
              :button-labels="buttonLabels"
              :answer="formattedQuestion?.answer"
              @click-button="handleButtonClick"
            />
          </div>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.page-container {
  padding: 20px;
}
</style> 