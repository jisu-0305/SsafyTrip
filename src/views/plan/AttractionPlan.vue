<template>
    <div class="plan-page">
        <AttractionPlanMap />

        <div class="plan-container">
            <AttractionPlanCard v-for="(plan, index) in plans" :key="index" :day="plan.day"
                @addSchedule="showPopup(plan.day)" />
        </div>

        <AttractionSchedulePopup v-if="showSchedulePopup" class="popup-right" @close="showSchedulePopup = false"
            @add="addSchedule" />
    </div>
</template>

<script setup>
import { ref } from 'vue';
import AttractionPlanMap from '@/components/plan/AttractionPlanMap.vue';
import AttractionPlanCard from '@/components/plan/AttractionPlanCard.vue';
import AttractionSchedulePopup from '@/components/plan/AttractionSchedulePopup.vue';

const plans = ref([{ day: 1 }, { day: 2 }]);
const showSchedulePopup = ref(false);
const currentDay = ref(null);

const showPopup = (day) => {
    currentDay.value = day;
    showSchedulePopup.value = true;
};

const addSchedule = (schedule) => {
    // 일정 추가 로직, 특정 day에 schedule 추가
    showSchedulePopup.value = false;
};
</script>

<style scoped>
.plan-page {
    position: relative;
}

.plan-container {
    width: 50%;
    margin: 20px auto;
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 20px;
}

/* 팝업을 오른쪽에 고정시키기 위한 스타일 */
.popup-right {
    position: fixed;
    top: 20%;
    /* 상단에서 떨어진 거리 */
    right: 5%;
    /* 오른쪽에서 떨어진 거리 */
    width: 20%;
    /* 팝업의 가로 크기 */
    background-color: #fff;
    padding: 20px;
    border: 1px solid #ddd;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    z-index: 1000;
}
</style>
