<script setup>
import { ref } from 'vue';
import NavigationMyPage from '@/components/mypage/NavigationMyPage.vue'
import ActivityMyPage from '@/components/mypage/ActivityMyPage.vue'
import EditMyPage from '@/components/mypage/EditMyPage.vue'
import DeleteMyPage from '@/components/mypage/DeleteMyPage.vue'

  // 현재 컴포넌트를 결정하는 flag 값 (1, 2, 그 외)
  const currentCompFlag = ref(1);

  // currentCompFlag 값에 따라 컴포넌트를 결정하는 computed
// const currentComponent = computed(() => {
//   return componentMap[currentCompFlag.value];
// });

function updateCurrentCompHanlder(params){
  currentCompFlag.value = params;
}

</script>

<template>
  <div style="display: flex; gap: 0;">
    <NavigationMyPage @updateFlag="updateCurrentCompHanlder"></NavigationMyPage>
    <!-- :currentCompFlag="currentCompFlag" -->
    <div v-if="currentCompFlag === 2">
      <EditMyPage />
    </div>
    <div v-else-if="currentCompFlag === 3"  >
      <DeleteMyPage />
    </div>
    <div v-else>
      <ActivityMyPage />
    </div>
    
  </div>

</template>

<style scoped>
  .v-container {
    width: 100%;
    padding: 32px;  /* 패딩을 32px로 수정 */
    margin-right: 0;  /* 기본 마진을 제거 */
    margin-left: 0;   /* 기본 마진을 제거 */
    }
  .pa-4 {
    padding: 0px !important;
  }
</style>
