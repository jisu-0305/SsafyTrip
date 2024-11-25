<script setup>
import { onMounted } from "vue";
import { storeToRefs } from 'pinia';
import { useQuestionStore } from "@/stores/questionStores";
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/authStores';
import PageHeader from "@/components/common/PageHeader.vue";
import BoardList from "@/components/board/BoardList.vue";
import SearchResultInfo from "@/components/common/SearchResultInfo.vue";

const router = useRouter();
const questionStore = useQuestionStore();
const { questions, currentPage, totalPages, totalElements, searchKeyword } = storeToRefs(questionStore);
const authStore = useAuthStore();

onMounted(() => {
  questionStore.fetchQuestions();
});

const handlePageChange = (questionId) => {
  questionStore.fetchQuestionById(questionId);
};

const handleSearch = () => {
  questionStore.fetchQuestions(1, 10, searchKeyword.value);
};

const goToWrite = () => {
  router.push({ name: 'question-write' });
};

const columns = [
  { 
    key: 'questionId', 
    label: '번호',
    style: 'width: 100px'
  },
  { 
    key: 'title', 
    label: '제목' 
  },
  {
    key: 'status',
    label: '상태',
    style: 'width: 100px',
    formatter: (value) => ({
      text: value === 'ANSWERED' ? '답변완료' : '답변대기',
      color: value === 'ANSWERED' ? 'success' : 'error',
      variant: 'flat'
    })
  },
  { 
    key: 'createdAt', 
    label: '작성일',
    style: 'width: 150px'
  }
];
</script>

<template>
  <v-container fluid class="page-container">
    <v-row justify="center">
      <v-col cols="12" class="content-wrapper">
        <div class="inner-content">
          <PageHeader title="1:1 문의" icon="mdi-help-circle" />
          <div class="content-area">
            <div class="search-area mb-6">
              <v-row>
                <v-col :cols="8">
                  <v-text-field
                    v-model="searchKeyword"
                    label="검색어"
                    @keyup.enter="handleSearch"
                    density="compact"
                    variant="outlined"
                  ></v-text-field>
                </v-col>
                <v-col cols="2">
                  <v-btn 
                    color="primary" 
                    @click="handleSearch" 
                    block
                    height="40"
                  >
                    검색
                  </v-btn>
                </v-col>
                <v-col cols="2">
                  <v-btn
                    color="blue-darken-3"
                    @click="goToWrite"
                    prepend-icon="mdi-plus"
                    block
                    height="40"
                  >
                    문의하기
                  </v-btn>
                </v-col>
              </v-row>
            </div>

            <div class="list-area">
              <SearchResultInfo :total-count="totalElements" />
              <BoardList 
                :articles="questions" 
                :columns="columns"
                type="question"
              />
              
              <div class="text-center mt-6">
                <v-pagination
                  v-model="currentPage"
                  :length="totalPages"
                  @update:model-value="handlePageChange(columns.questionId)"
                ></v-pagination>
              </div>
            </div>
          </div>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template> 