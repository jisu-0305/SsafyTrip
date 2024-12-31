<script setup>
import { onMounted, watch } from "vue";
import { storeToRefs } from "pinia";
import { useQuestionStore } from "@/stores/questionStores";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/authStores";
import PageHeader from "@/components/common/PageHeader.vue";
import BoardList from "@/components/board/BoardList.vue";
import SearchResultInfo from "@/components/common/SearchResultInfo.vue";

const router = useRouter();
const questionStore = useQuestionStore();
const authStore = useAuthStore();
const { questions, currentPage, totalPages, totalElements, searchKeyword } =
  storeToRefs(questionStore);

console.log("QuestionListView, jun");
console.log(questions);

// console.log(questions.value);

onMounted(async () => {
  try {
    if (!authStore.isLoggedIn) {
      alert("로그인이 필요한 서비스입니다.");
      router.push({ name: "user-login" });
      return;
    }
    await questionStore.fetchQuestions();
  } catch (error) {
    if (error.response?.status === 401 || error.response?.status === 403) {
      authStore.handleAuthError(error);
    }
  }
});

watch(currentPage, async (newPage) => {
  await questionStore.fetchQuestions(newPage, 10, searchKeyword.value);
});

const handleSearch = async () => {
  await questionStore.fetchQuestions(1, 10, searchKeyword.value);
};

const goToWrite = () => {
  if (!authStore.isLoggedIn) {
    alert("로그인이 필요한 서비스입니다.");
    router.push("/login");
    return;
  }
  router.push({ name: "question-write" });
};

const handleRowClick = (article) => {
  router.push({
    name: "question-detail",
    params: { id: article.questionId.toString() },
  });
};

const columns = [
  {
    key: "questionId",
    label: "번호",
    style: "width: 100px",
  },
  {
    key: "title",
    label: "제목",
  },
  {
    key: "answered",
    label: "상태",
    style: "width: 100px",
    formatter: (value) => ({
      text: value ? "답변완료" : "답변대기",
      color: value ? "success" : "error",
      variant: "flat",
    }),
  },
  {
    key: "createdAt",
    label: "작성일",
    style: "width: 150px",
  },
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
              <v-card>
                <v-card-text v-if="!questions.length" class="py-16">
                  <div class="d-flex flex-column align-center">
                    <v-icon
                      icon="mdi-help-circle-outline"
                      size="64"
                      color="grey-lighten-1"
                      class="mb-4"
                    ></v-icon>
                    <h3 class="text-h6 text-grey-darken-1 font-weight-medium mb-2">
                      문의 내역이 없습니다
                    </h3>
                    <p class="text-body-1 text-grey">
                      궁금한 점이 있다면 문의해주세요!
                    </p>
                    <v-btn
                      color="primary"
                      class="mt-4"
                      prepend-icon="mdi-plus"
                      @click="goToWrite"
                    >
                      문의하기
                    </v-btn>
                  </div>
                </v-card-text>

                <BoardList
                  v-else
                  :articles="questions"
                  :columns="columns"
                  :on-row-click="handleRowClick"
                  type="question"
                />

                <div v-if="questions.length" class="text-center mt-6">
                  <v-pagination
                    v-model="currentPage"
                    :length="totalPages"
                    @update:model-value="handlePageChange"
                  ></v-pagination>
                </div>
              </v-card>
            </div>
          </div>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>
