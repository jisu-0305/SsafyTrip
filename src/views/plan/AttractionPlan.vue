<template>
    <div class="plan-page">
        <!-- 제목 및 날짜 입력 -->
        <TravelInputForm class="left-centered-content" :totalCost="calculateTotalCost()"
            @update:travelTitle="travelTitle = $event" @update:startDate="startDate = $event"
            @update:endDate="endDate = $event" @update:memo="memo = $event" />

        <!-- 지도 -->
        <AttractionPlanMap class="left-centered-content" />

        <!-- 일정 카드 -->
        <div class="plan-container">
            <AttractionPlanCard v-for="(plan, index) in plans" :key="plan.day" :day="plan.day"
                :schedules="plan.schedules" @addSchedule="showWishListPopup(plan.day)" />
        </div>

        <!-- + 버튼으로 AttractionPlanCard 추가 -->
        <v-btn class="add-plan-btn" color="primary" @click="addNewPlan">+ 추가</v-btn>

        <!-- 찜 목록 팝업 -->
        <WishListDialog v-if="showWishList" :showWishList="showWishList" @update:showWishList="updateShowWishList"
            @spotSelected="selectSpot" />

        <!-- 일정 추가 팝업 -->
        <AttractionSchedulePopup v-if="showSchedulePopup" :showPopup="showSchedulePopup" :selectedSpot="selectedSpot"
            :selectedDate="selectedDate" @add="addSchedule" @close="closeSchedulePopup" />

        <!-- 저장 버튼 -->
        <v-btn block color="success" class="mt-4" @click="savePlans">일정 저장</v-btn>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import TravelInputForm from '@/components/plan/TravelInputForm.vue';
import AttractionPlanMap from '@/components/plan/AttractionPlanMap.vue';
import AttractionPlanCard from '@/components/plan/AttractionPlanCard.vue';
import WishListDialog from '@/components/plan/WishListDialog.vue';
import AttractionSchedulePopup from '@/components/plan/AttractionSchedulePopup.vue';
import { createSchedule } from '@/api/scheduleApi'; // 일정 저장 API

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
.plan-page {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 20px;
}

.plan-container {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 10px;
}

.add-plan-btn {
    margin: 20px 0;
}
</style>
