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
  return {
    title: currentQuestion.value.questionTitle,
    content: currentQuestion.value.questionContent,
    createdAt: currentQuestion.value.questionCreatedAt,
    status: currentQuestion.value.questionIsAnswered ? 'ANSWERED' : 'WAITING',
    authorEmail: currentQuestion.value.questionAuthorEmail,
    answer: currentQuestion.value.answerContent ? {
      content: currentQuestion.value.answerContent,
      createdAt: currentQuestion.value.answerCreatedAt,
      authorEmail: currentQuestion.value.answerAuthorEmail
    } : null
  };
});

const isOwner = computed(() => {
  return currentQuestion.value?.questionAuthorEmail === authStore.user?.email;
});

const handleList = () => {
  router.push({ name: "question" });
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
              type="question"
              :article="formattedQuestion"
              :isAdmin="isOwner"
              :additionalFields="additionalFields"
              :buttons="['list']"
              @list="handleList"
            />
            
            <v-card v-if="formattedQuestion?.answer" class="mt-6">
              <v-card-title class="text-h6 pa-4">
                답변 내용
                <div class="text-caption mt-1">
                  답변일: {{ new Date(formattedQuestion.answer.createdAt).toLocaleDateString() }}
                  ({{ formattedQuestion.answer.authorEmail }})
                </div>
              </v-card-title>
              <v-card-text class="pa-4" v-html="formattedQuestion.answer.content">
              </v-card-text>
            </v-card>
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