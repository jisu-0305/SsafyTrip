<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { usePlanStore } from '@/stores/planStores';
import PageHeader from '@/components/common/PageHeader.vue';
import { useAuthStore } from '@/stores/authStores';

const route = useRoute();
const router = useRouter();
const planStore = usePlanStore();
const authStore = useAuthStore();
const scheduleDetail = ref(null);
const isLoading = ref(true);

onMounted(async () => {
  try {
    // 로그인 체크
    if (!authStore.isLoggedIn) {
      alert('로그인이 필요한 서비스입니다.');
      router.push('/login');
      return;
    }

    const scheduleId = route.params.id;
    const response = await planStore.getScheduleDetail(scheduleId);
    scheduleDetail.value = response;
  } catch (error) {
    console.error('일정 상세 정보를 불러오는 중 오류 발생:', error);
    alert('일정을 불러오는데 실패했습니다.');
    router.push('/schedules');
  } finally {
    isLoading.value = false;
  }
});
</script>

<template>
  <v-container fluid class="page-container">
    <v-row justify="center">
      <v-col cols="12" class="content-wrapper">
        <div class="inner-content">
          <PageHeader title="여행 일정 상세" icon="mdi-calendar-text" />

          <!-- 로딩 상태 -->
          <div v-if="isLoading" class="d-flex justify-center align-center my-8">
            <v-progress-circular indeterminate color="primary"></v-progress-circular>
          </div>

          <!-- 일정 상세 정보 -->
          <div v-else-if="scheduleDetail">
            <!-- 여기에 상세 정보 표시 컴포넌트들이 추가될 예정 -->
          </div>

          <!-- 에러 상태 -->
          <v-card v-else class="text-center pa-8" variant="outlined">
            <v-icon
              icon="mdi-alert-circle"
              size="64"
              color="error"
              class="mb-4"
            ></v-icon>
            <div class="text-h6 text-grey-darken-1 mb-4">
              일정을 불러올 수 없습니다
            </div>
            <v-btn
              color="primary"
              @click="router.push('/schedules')"
              prepend-icon="mdi-arrow-left"
            >
              일정 목록으로 돌아가기
            </v-btn>
          </v-card>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.inner-content {
  max-width: 1200px;
  margin: 0 auto;
}
</style> 