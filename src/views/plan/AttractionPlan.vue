<template>
    <div class="plan-page">
        <!-- 제목 및 날짜 입력 폼 -->
        <TravelInputForm class="left-centered-content" />

        <!-- 지도 -->
        <AttractionPlanMap class="left-centered-content" />

        <!-- 일정 카드 -->
        <div class="plan-container left-centered-content">
            <AttractionPlanCard v-for="(plan, index) in plans" :key="index" :day="plan.day"
                @addSchedule="showWishListPopup(plan.day)" class="plan-card" />
        </div>

        <!-- 찜 목록 팝업 (오른쪽에 고정) -->
        <WishListDialog :showWishList="showWishList" @update:showWishList="updateShowWishList"
            @spotSelected="selectSpot" />

        <!-- 일정 추가 팝업 -->
        <AttractionSchedulePopup v-if="showSchedulePopup" class="popup-right" @close="showSchedulePopup = false"
            @add="addSchedule" />
    </div>
</template>

<script setup>
import { ref } from 'vue';
import TravelInputForm from '@/components/plan/TravelInputForm.vue';
import AttractionPlanMap from '@/components/plan/AttractionPlanMap.vue';
import AttractionPlanCard from '@/components/plan/AttractionPlanCard.vue';
import WishListDialog from '@/components/plan/WishListDialog.vue';
import AttractionSchedulePopup from '@/components/plan/AttractionSchedulePopup.vue';

const plans = ref([{ day: 1 }, { day: 2 }]);
const showSchedulePopup = ref(false);
const showWishList = ref(false); // 찜 목록 팝업 상태 관리
const currentDay = ref(null);
const selectedSpot = ref(null); // 선택된 관광지 정보

// 찜 목록 팝업 열기
const showWishListPopup = (day) => {
    currentDay.value = day;
    showWishList.value = true;
};

// 찜 목록 상태 업데이트
const updateShowWishList = (value) => {
    showWishList.value = value;
};

// 관광지 선택 시 일정 추가 팝업 열기
const selectSpot = (spot) => {
    selectedSpot.value = spot;
    showWishList.value = false;
    showSchedulePopup.value = true;
};

// 일정 추가
const addSchedule = (schedule) => {
    // 일정 추가 로직, 특정 day에 schedule 추가
    console.log("관광지 추가됨:", schedule, selectedSpot.value);
    showSchedulePopup.value = false;
};
</script>

<style scoped>
.plan-page {
    display: flex;
    flex-direction: column;
    align-items: center;
    /* 자식 요소들을 수평 중앙에 정렬 */
}

/* 왼쪽 중앙 정도에 위치하는 컨테이너 */
.left-centered-content {
    width: 70%;
    /* 전체 너비의 70% 사용 */
    margin-left: -10%;
    /* 살짝 왼쪽으로 이동 */
    max-width: 1000px;
    /* 컨테이너의 최대 너비 설정 */
}

/* 일정 카드 섹션 스타일 */
.plan-container {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 5px;
    /* 각 카드 간의 간격을 줄임 */
    margin: 20px 0;
}

/* 일정 추가 팝업 스타일 */
.popup-right {
    position: fixed;
    top: 20%;
    right: 5%;
    width: 20%;
    background-color: #fff;
    padding: 20px;
    border: 1px solid #ddd;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    z-index: 1000;
}
</style>
