<script setup>
import { useRouter, useRoute } from "vue-router";
import { useQuestionStore } from "@/stores/questionStores";
import { useAuthStore } from "@/stores/authStores";
import BoardWrite from "@/components/board/BoardWrite.vue";
import PageHeader from "@/components/common/PageHeader.vue";

const route = useRoute();
const router = useRouter();
const questionStore = useQuestionStore();
const authStore = useAuthStore();

const handleSubmit = async (formData) => {
  try {
    await questionStore.createQuestionAnswer(route.params.id, {
      content: formData.content
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
            <BoardWrite
              type="answer"
              title="답변 작성"
              :show-title-field="false"
              @submit="handleSubmit"
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