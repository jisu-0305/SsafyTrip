<template>
  <!-- 로딩 상태 표시 -->
  <div v-if="isLoading" class="d-flex justify-center align-center" style="height: 100vh">
    <v-progress-circular
      indeterminate
      color="primary"
      size="64"
    ></v-progress-circular>
  </div>

  <!-- 메인 컨텐츠 -->
  <v-container v-else fluid class="page-container">
    <v-row justify="center">
      <v-col cols="12" class="content-wrapper">
        <div class="inner-content">
          <PageHeader title="여행 일정 만들기" icon="mdi-calendar-plus" />
          
          <!-- 날짜 입력 안내 메시지 -->
          <v-alert
            v-if="!planStore.startDate || !planStore.endDate"
            type="warning"
            variant="tonal"
            class="mb-4"
          >
            <v-icon start icon="mdi-calendar-alert"></v-icon>
            출발 날짜와 도착 날짜를 필수적으로 입력해주세요.
          </v-alert>

          <!-- 제목 및 날짜 입력 -->
          <v-card class="mb-6">
            <v-card-text>
              <TravelInputForm 
                :totalCost="planStore.calculateTotalCost"
                @update:travelTitle="updateTravelInfo('title', $event)"
                @update:startDate="updateTravelInfo('start', $event)"
                @update:endDate="updateTravelInfo('end', $event)"
                @update:memo="updateTravelInfo('memo', $event)"
              />
            </v-card-text>
          </v-card>

          <!-- 날짜가 입력된 경우에만 나머지 컨텐츠 표시 -->
          <div :class="{ 'content-locked': !areDatesSelected }">
            <!-- 지도 -->
            <v-card class="mb-6">
              <v-card-text>
                <AttractionPlanMap />
              </v-card-text>
            </v-card>

            <!-- 일정 카드 -->
            <v-card>
              <v-card-title class="d-flex justify-space-between align-center px-4">
                <span>일정 목록</span>
              </v-card-title>

              <v-divider></v-divider>

              <v-card-text>
                <v-row>
                  <v-col
                    v-for="plan in planStore.plans"
                    :key="plan.day"
                    cols="12"
                    md="6"
                  >
                    <AttractionPlanCard
                      :day="plan.day"
                      :date="plan.date"
                      :schedules="plan.schedules"
                      @addSchedule="showWishListPopup"
                      @removeDay="planStore.removePlan"
                      @updateSchedules="planStore.updateSchedules"
                    />
                  </v-col>
                </v-row>
              </v-card-text>
            </v-card>

            <!-- 저장 버튼 -->
            <v-btn
              block
              color="success"
              size="large"
              class="mt-6"
              @click="savePlans"
            >
              일정 저장
            </v-btn>
          </div>
        </div>
      </v-col>
    </v-row>

    <!-- 찜 목록 서랍 -->
    <v-navigation-drawer
      v-model="planStore.showWishList"
      location="right"
      temporary
      width="600"
    >
      <WishListDialog />
    </v-navigation-drawer>

    <!-- 일정 추가 서랍 -->
    <v-navigation-drawer
      v-model="planStore.showSchedulePopup"
      location="right"
      temporary
      width="500"
    >
      <AttractionSchedulePopup
        v-if="planStore.showSchedulePopup"
        @add="planStore.addSchedule"
        @close="closeSchedulePopup"
      />
    </v-navigation-drawer>
  </v-container>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/authStores';
import TravelInputForm from '@/components/plan/TravelInputForm.vue';
import AttractionPlanMap from '@/components/plan/AttractionPlanMap.vue';
import AttractionPlanCard from '@/components/plan/AttractionPlanCard.vue';
import WishListDialog from '@/components/plan/WishListDialog.vue';
import AttractionSchedulePopup from '@/components/plan/AttractionSchedulePopup.vue';
import { createSchedule } from '@/api/scheduleApi'; // 일정 저장 API
import PageHeader from '@/components/common/PageHeader.vue';
import { usePlanStore } from '@/stores/planStores';

const router = useRouter();
const authStore = useAuthStore();
const planStore = usePlanStore();
const isLoading = ref(true);

// 로그인 체크
onMounted(async () => {
  try {
    if (!authStore.isLoggedIn) {
      alert('로그인이 필요한 서비스입니다.');
      await router.push('/login');
      return;
    }
  } finally {
    isLoading.value = false;
  }
});

// 상태 관리
const travelTitle = ref('');
const startDate = ref(null);
const endDate = ref(null);
const memo = ref('');
const plans = ref([{ day: 1, schedules: [] }]);


// 찜 목록 팝업 열기
const showWishListPopup = (day) => {
  planStore.setCurrentDay(day);
  planStore.setShowWishList(true);
};


// 일정 저장
const savePlans = async () => {
  try {
    if (!planStore.travelTitle) {
      alert('여행 제목을 입력해주세요.');
      return;
    }

    if (!planStore.startDate || !planStore.endDate) {
      alert('여행 날짜를 선택해주세요.');
      return;
    }

    const scheduleData = planStore.formatScheduleData();
    console.log('Saving schedule data:', scheduleData);

    const response = await createSchedule(scheduleData);
    console.log('일정 저장 응답:', response);

    if (response.data.status === 'success') {
      alert(`일정이 성공적으로 저장되었습니다. (일정 번호: ${response.data.message.split('로')[0]})`);
      
      // 모든 데이터 초기화
      planStore.resetAllData();
      
      // 일정 목록 페이지로 이동
      router.push('/schedules');
    } else {
      throw new Error('일정 저장에 실패했습니다.');
    }
  } catch (error) {
    console.error('일정 저장 오류:', error);
    alert('일정 저장에 실패했습니다. 다시 시도해주세요.');
  }
};


onMounted(() => {
  // 컴포넌트 마운트 시 currentDay 설정
  if (planStore.plans.value?.length > 0 && !planStore.currentDay.value) {
    planStore.setCurrentDay(planStore.plans.value[0].day);
  }
});

// 날짜 선택 여부 확인
const areDatesSelected = computed(() => {
  return planStore.startDate && planStore.endDate;
});

// 여행 정보 업데이트 함수 추가
const updateTravelInfo = (type, value) => {
  switch (type) {
    case 'title':
      planStore.travelTitle = value;
      travelTitle.value = value;
      break;
    case 'start':
      planStore.setStartDate(value);
      startDate.value = value;
      console.log('Start date updated:', value);
      break;
    case 'end':
      planStore.setEndDate(value);
      endDate.value = value;
      console.log('End date updated:', value);
      break;
    case 'memo':
      planStore.memo = value;
      memo.value = value;
      break;
  }
};

// computed 속성 추가
const currentPlans = computed(() => {
  return planStore.plans;
});

// watch 추가
watch(currentPlans, (newPlans) => {
  console.log('Plans updated:', newPlans);
}, { deep: true });

// 날짜 변경 감시
watch(
  [() => planStore.startDate, () => planStore.endDate],
  ([newStart, newEnd]) => {
    console.log('Date range changed:', { start: newStart, end: newEnd });
    if (newStart && newEnd) {
      planStore.initializePlans();
    }
  },
  { immediate: true }
);
</script>

<style scoped>
.v-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.v-card-title {
  font-size: 1.25rem;
  font-weight: 600;
  padding: 16px;
}

.v-btn {
  text-transform: none;
  font-weight: 500;
}

.content-locked {
  pointer-events: none;
  opacity: 0.5;
  position: relative;
}

.content-locked::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: transparent;
  z-index: 1;
}

.v-alert {
  border-left: 4px solid var(--v-warning-base);
}
</style>
