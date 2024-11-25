<script setup>
import { useRouter } from "vue-router";
import { useQuestionStore } from "@/stores/questionStores";
import { useAuthStore } from "@/stores/authStores";
import BoardWrite from "@/components/board/BoardWrite.vue";
import PageHeader from "@/components/common/PageHeader.vue";

const router = useRouter();
const questionStore = useQuestionStore();
const authStore = useAuthStore();

const handleSubmit = async (formData) => {
  try {
    await questionStore.createQuestion({
      ...formData,
      userId: authStore.user.id,
      status: '대기중'
    });
    alert("문의글이 등록되었습니다.");
    router.push({ name: "question" });
  } catch (error) {
    alert("등록 중 오류가 발생했습니다.");
  }
};
</script>

<template>
  <v-container fluid class="page-container">
    <v-row justify="center">
      <v-col cols="12" class="content-wrapper">
        <div class="inner-content">
          <PageHeader title="문의글 작성" icon="mdi-help-circle" />
          <div class="content-area">
            <BoardWrite
              type="question"
              title="1:1 문의"
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