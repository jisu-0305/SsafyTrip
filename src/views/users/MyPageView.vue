<script setup>
import { ref } from 'vue';
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
  <div class="center-container">
    <v-container class="page-container" fluid>
      <!-- 마이페이지 타이틀 -->
      <v-row>
        <v-col>
          <h1 class="my-page-title">
            <v-icon left size="24">mdi-account-circle-outline</v-icon> 마이페이지
          </h1>
        </v-col>
      </v-row>
      <div class="content-wrapper">
        <NavigationMyPage @updateFlag="updateCurrentCompHanlder"></NavigationMyPage>
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
    </v-container>
  </div>
</template>

<style scoped>
/* 전체 컨테이너를 중앙에 배치 */
.center-container {
  display: flex;
  justify-content: center;  /* 수평 중앙 정렬 */
  align-items: center;      /* 수직 중앙 정렬 */
  min-height: 100vh;        /* 화면 전체 높이 사용 */
  padding: 20px;            /* 페이지 전체에 여유 공간 */
}

/* 페이지 컨테이너 스타일 */
.page-container {
  max-width: 1200px;        /* 페이지 최대 너비를 설정 (원하는 너비로 조절 가능) */
  width: 100%;
  padding: 32px;
  margin: 0 auto;           /* 자동으로 좌우 여백 설정 */
}

/* 타이틀 스타일 */
.my-page-title {
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 20px;
}

/* 콘텐츠 래퍼 */
.content-wrapper {
  display: flex;
  gap: 20px;                /* 컴포넌트 간격 설정 */
}
</style>