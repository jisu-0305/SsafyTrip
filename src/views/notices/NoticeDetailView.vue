<script setup>
import { onMounted, onUnmounted, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { storeToRefs } from 'pinia';
import { useNoticeStore } from "@/stores/noticeStores";
import { useAuthStore } from '@/stores/authStores'
import BoardDetail from "@/components/board/BoardDetail.vue";
import { useLoadingStore } from '@/stores/loadingStore'
import PageHeader from "@/components/common/PageHeader.vue";

const route = useRoute();
const router = useRouter();
const noticeStore = useNoticeStore();
const { currentNotice } = storeToRefs(noticeStore);
const authStore = useAuthStore()
const loadingStore = useLoadingStore()

let isComponentMounted = true;

onMounted(async () => {
  try {
    if (isComponentMounted) {
      loadingStore.startLoading('notice-detail')
      await noticeStore.fetchNoticeById(route.params.id);
    }
  } catch (error) {
    if (isComponentMounted) {
      console.error('공지사항 로딩 실패:', error);
    }
  } finally {
    if (isComponentMounted) {
      loadingStore.endLoading('notice-detail')
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

const buttonPermissions = computed(() => ({
  edit: authStore.isAdmin,
  delete: authStore.isAdmin,
  list: true
}))

const buttonLabels = {
  edit: '수정',
  delete: '삭제',
  list: '목록'
}

const handleButtonClick = (buttonType) => {
  switch (buttonType) {
    case 'edit':
      handleEdit()
      break
    case 'delete':
      handleDelete()
      break
    case 'list':
      handleList()
      break
  }
}

const handleEdit = () => {
  router.push({ name: "notice-edit", params: { id: route.params.id } });
};

const handleList = () => {
  router.push({ name: "notice" });
};
</script>

<template>
  <v-container fluid class="page-container">
    <v-row justify="center">
      <v-col cols="12" class="content-wrapper">
        <div class="inner-content">
          <PageHeader title="공지사항 상세" icon="mdi-clipboard-text" />
          <div class="content-area">
          <BoardDetail
            :article="currentNotice"
            loading-key="notice-detail"
            :buttons="['edit', 'delete', 'list']"
            :button-permissions="buttonPermissions"
            :button-labels="buttonLabels"
            @click-button="handleButtonClick"
          />
          </div>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
</style>