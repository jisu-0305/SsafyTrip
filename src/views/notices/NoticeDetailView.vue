<script setup>
import { onMounted, onUnmounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { storeToRefs } from 'pinia';
import { useNoticeStore } from "@/stores/noticeStores";
import { useAuthStore } from '@/stores/authStores'
import BoardDetail from "@/components/board/BoardDetail.vue";

const route = useRoute();
const router = useRouter();
const noticeStore = useNoticeStore();
const { currentNotice } = storeToRefs(noticeStore);
const authStore = useAuthStore()

// 컴포넌트 마운트 해제 시 사용할 flag
let isComponentMounted = true;

onMounted(async () => {
  try {
    if (isComponentMounted) {
      await noticeStore.fetchNoticeById(route.params.id);
    }
  } catch (error) {
    if (isComponentMounted) {
      console.error('공지사항 로딩 실패:', error);
    }
  }
});

onUnmounted(() => {
  isComponentMounted = false;
});

const handleDelete = async () => {
  if (confirm("이 공지사항을 삭제하시겠습니까?")) {
    try {
      await noticeStore.deleteNotice(route.params.id);
      alert("삭제되었습니다.");
      router.push({ name: "notice" });
    } catch (error) {
      alert("삭제 중 오류가 발생했습니다.");
    }
  }
};

const handleEdit = () => {
  router.push({ name: "notice-edit", params: { id: route.params.id } });
};

const handleList = () => {
  router.push({ name: "notice" });
};
</script>

<template>
  <BoardDetail
    type="notice"
    :article="currentNotice"
    :isAdmin="authStore.isAdmin"
    @edit="handleEdit"
    @delete="handleDelete"
    @list="handleList"
  />
</template>