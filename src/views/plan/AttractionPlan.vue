<template>
  <v-container fluid class="page-container">
    <v-row justify="center">
      <v-col cols="12" class="content-wrapper">
        <div class="inner-content">
          <PageHeader title="여행 일정 만들기" icon="mdi-calendar-plus" />
          
          <!-- 제목 및 날짜 입력 -->
          <v-card class="mb-6">
            <v-card-text>
              <TravelInputForm 
                :totalCost="calculateTotalCost()"
                @update:travelTitle="travelTitle = $event"
                @update:startDate="startDate = $event"
                @update:endDate="endDate = $event"
                @update:memo="memo = $event"
              />
            </v-card-text>
          </v-card>

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
              <v-btn
                color="primary"
                prepend-icon="mdi-plus"
                @click="addNewPlan"
              >
                일정 추가
              </v-btn>
            </v-card-title>

            <v-divider></v-divider>

            <v-card-text>
              <v-row>
                <v-col
                  v-for="(plan, index) in plans"
                  :key="plan.day"
                  cols="12"
                  md="6"
                >
                  <AttractionPlanCard
                    :day="plan.day"
                    :schedules="plan.schedules"
                    @addSchedule="showWishListPopup(plan.day)"
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
      </v-col>
    </v-row>

    <!-- 팝업 다이얼로그들 -->
    <v-dialog v-model="showWishList" max-width="600px">
      <WishListDialog
        :showWishList="showWishList"
        @update:showWishList="updateShowWishList"
        @spotSelected="selectSpot"
      />
    </v-dialog>

    <v-dialog v-model="showSchedulePopup" max-width="500px">
      <AttractionSchedulePopup
        v-if="showSchedulePopup"
        :showPopup="showSchedulePopup"
        :selectedSpot="selectedSpot"
        :selectedDate="selectedDate"
        @add="addSchedule"
        @close="closeSchedulePopup"
      />
    </v-dialog>
  </v-container>
</template>

<script setup>
import { ref } from 'vue';
import TravelInputForm from '@/components/plan/TravelInputForm.vue';
import AttractionPlanMap from '@/components/plan/AttractionPlanMap.vue';
import AttractionPlanCard from '@/components/plan/AttractionPlanCard.vue';
import WishListDialog from '@/components/plan/WishListDialog.vue';
import AttractionSchedulePopup from '@/components/plan/AttractionSchedulePopup.vue';
import { createSchedule } from '@/api/scheduleApi'; // 일정 저장 API
import PageHeader from '@/components/common/PageHeader.vue';

// 상태 관리
const travelTitle = ref('');
const startDate = ref(null);
const endDate = ref(null);
const memo = ref('');
const plans = ref([{ day: 1, schedules: [] }]);
const showWishList = ref(false);
const showSchedulePopup = ref(false);
const currentDay = ref(null);
const selectedSpot = ref(null);
const selectedDate = ref(null);

// 일정 카드 추가
const addNewPlan = () => {
    plans.value.push({ day: plans.value.length + 1, schedules: [] });
};

const selectSpot = (spot) => {
    selectedSpot.value = spot;
    selectedDate.value = startDate.value; // 기본적으로 출발 날짜를 사용
    showWishList.value = false;
    showSchedulePopup.value = true;
};

// 찜 목록 팝업 열기
const showWishListPopup = (day) => {
    currentDay.value = day;
    showWishList.value = true;
};

// 찜 목록 상태 업데이트
const updateShowWishList = (value) => {
    showWishList.value = value;
};

// 일정 추가
const addSchedule = (schedule) => {
    const dayIndex = plans.value.findIndex((plan) => plan.day === currentDay.value);
    if (dayIndex !== -1) {
        plans.value[dayIndex].schedules.push({
            ...schedule,
            attractionId: selectedSpot.value.no,
        });
    }
    console.log('일정 추가:', plans.value);
    showSchedulePopup.value = false; // 팝업 닫기
};

// 일정 추가 팝업 닫기
const closeSchedulePopup = () => {
    showSchedulePopup.value = false;
};

// 총 비용 계산
const calculateTotalCost = () => {
    return plans.value.reduce((total, plan) => {
        return total + plan.schedules.reduce((dayTotal, schedule) => dayTotal + schedule.cost, 0);
    }, 0);
};

// 일정 저장
const savePlans = async () => {
    const scheduleData = {
        scheduleInformation: {
            title: travelTitle.value,
            memo: memo.value,
            totalCost: calculateTotalCost(),
            startDate: startDate.value,
            endDate: endDate.value,
        },
        schedulePlaces: plans.value.flatMap((plan) =>
            plan.schedules.map((schedule, index) => ({
                attractionId: schedule.attractionId,
                visitTime: schedule.visitTime,
                memo: schedule.memo,
                cost: schedule.cost,
                visitOrder: index + 1,
            }))
        ),
    };

    try {
        const response = await createSchedule(scheduleData);
        console.log('일정 저장 성공:', response);
        alert('일정이 저장되었습니다.');
    } catch (error) {
        console.error('일정 저장 오류:', error);
        alert('일정 저장에 실패했습니다.');
    }
};
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
</style>
