<script setup>
import { ref } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useQuestionStore } from "@/stores/questionStores";
import { useAuthStore } from "@/stores/authStores";
import BoardWrite from "@/components/board/BoardWrite.vue";
import PageHeader from "@/components/common/PageHeader.vue";

const route = useRoute();
const router = useRouter();
const questionStore = useQuestionStore();
const authStore = useAuthStore();

const questionId = route.params.id;
console.log("QuestionAnswerView, jun");
console.log(questionId);

const answerText = ref("");

const handleSubmit = async (formData) => {
  console.log(answerText.value);

  try {
    await questionStore.createQuestionAnswer(route.params.id, {
      content: answerText.value,
    });
    alert("답변이 등록되었습니다.");
    router.push({ name: "question-detail", params: { id: route.params.id } });
  } catch (error) {
    alert("답변 등록에 실패했습니다.");
  }
};
</script>

<template>
  <v-container fluid class="page-container">
    <v-row justify="center">
      <v-col cols="12" class="content-wrapper">
        <div class="inner-content">
          <PageHeader title="답변 작성" icon="mdi-comment-text" />
          <div class="content-area">
            <!-- <BoardWrite  활용 불가..
              type="answer"
              title="답변 작성"
              :show-title-field="false"
              @submit="handleSubmit"
            /> -->
            <div class="content-area">
              <!-- 텍스트 입력 및 버튼 -->
              <v-textarea
                label="답변 내용을 입력하세요"
                v-model="answerText"
                outlined
                rows="5"
              ></v-textarea>

              <v-btn color="primary" class="mt-4" @click="handleSubmit">
                답변 등록
              </v-btn>
            </div>
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
