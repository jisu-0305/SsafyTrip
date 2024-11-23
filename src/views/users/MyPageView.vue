<script setup>
import { ref } from 'vue';
import PageHeader from '@/components/common/PageHeader.vue';
import NavigationMyPage from '@/components/mypage/NavigationMyPage.vue';
import ActivityMyPage from '@/components/mypage/ActivityMyPage.vue';
import EditMyPage from '@/components/mypage/EditMyPage.vue';
import DeleteMyPage from '@/components/mypage/DeleteMyPage.vue';

// 현재 컴포넌트를 결정하는 flag 값 (1, 2, 그 외)
const currentCompFlag = ref(1);

// 컴포넌트 업데이트 핸들러
function updateCurrentCompHanlder(params) {
  currentCompFlag.value = params;
}
</script>

<template>
  <div class="page-wrapper">
    <PageHeader 
      title="마이페이지" 
      icon="mdi-account-circle-outline" 
    />
    <div class="content-wrapper">
      <NavigationMyPage @updateFlag="updateCurrentCompHanlder" />
      <div v-if="currentCompFlag === 2">
        <EditMyPage />
      </div>
      <div v-else-if="currentCompFlag === 3">
        <DeleteMyPage />
      </div>
      <div v-else>
        <ActivityMyPage />
      </div>
    </div>
  </div>
</template>

<style scoped>
.page-wrapper {
  width: 100%;
}

.content-wrapper {
  display: flex;
  gap: 32px;
}
</style>