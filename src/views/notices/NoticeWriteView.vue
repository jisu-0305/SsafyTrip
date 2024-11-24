<script setup>
import { useRouter } from "vue-router";
import { useNoticeStore } from "@/stores/noticeStores";
import { useAuthStore } from "@/stores/authStores";
import BoardWrite from "@/components/board/BoardWrite.vue";

const router = useRouter();
const noticeStore = useNoticeStore();
const authStore = useAuthStore();

const handleSubmit = async (formData) => {
  try {
    await noticeStore.createNotice({
      ...formData,
      userId: authStore.user.id
    });
    alert("공지사항이 등록되었습니다.");
    router.push({ name: "notice" });
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
          <PageHeader title="공지사항 상세" icon="mdi-clipboard-text" />
          <div class="content-area">
          <BoardWrite
            type="notice"
            title="공지사항"
            @submit="handleSubmit"
            />
          </div>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>