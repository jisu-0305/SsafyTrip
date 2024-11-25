<template>
    <v-container fluid class="page-container">
      <v-row justify="center">
        <v-col cols="12" class="content-wrapper">
          <div class="inner-content">
            <PageHeader title="여행 일정 만들기" icon="mdi-calendar-clock" />
            
            <div class="content-area">
              <!-- 제목 및 날짜 입력 폼 -->
              <v-card class="mb-6" elevation="0">
                <TravelInputForm />
              </v-card>
  
              <!-- 지도 -->
              <v-card class="mb-6" elevation="0">
                <AttractionPlanMap />
              </v-card>
  
              <!-- 일정 카드 -->
              <v-row>
                <v-col 
                  v-for="(plan, index) in plans" 
                  :key="index"
                  cols="12"
                  md="6"
                  class="plan-card-col"
                >
                  <AttractionPlanCard
                    :day="plan.day"
                    @addSchedule="showWishListPopup(plan.day)"
                  />
                </v-col>
              </v-row>
            </div>
          </div>
        </v-col>
      </v-row>
  
      <!-- 찜 목록 팝업 -->
      <WishListDialog 
        :showWishList="showWishList" 
        @update:showWishList="updateShowWishList"
        @spotSelected="selectSpot" 
      />
  
      <!-- 일정 추가 팝업 -->
      <v-dialog
        v-model="showSchedulePopup"
        max-width="500"
      >
        <AttractionSchedulePopup
          @close="showSchedulePopup = false"
          @add="addSchedule"
        />
      </v-dialog>
    </v-container>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import PageHeader from '@/components/common/PageHeader.vue';
  import TravelInputForm from '@/components/plan/TravelInputForm.vue';
  import AttractionPlanMap from '@/components/plan/AttractionPlanMap.vue';
  import AttractionPlanCard from '@/components/plan/AttractionPlanCard.vue';
  import WishListDialog from '@/components/plan/WishListDialog.vue';
  import AttractionSchedulePopup from '@/components/plan/AttractionSchedulePopup.vue';
  
  // 기존 script 로직 유지
  const plans = ref([{ day: 1 }, { day: 2 }]);
  const showSchedulePopup = ref(false);
  const showWishList = ref(false);
  const currentDay = ref(null);
  const selectedSpot = ref(null);
  
  const showWishListPopup = (day) => {
    currentDay.value = day;
    showWishList.value = true;
  };
  
  const updateShowWishList = (value) => {
    showWishList.value = value;
  };
  
  const selectSpot = (spot) => {
    selectedSpot.value = spot;
    showWishList.value = false;
    showSchedulePopup.value = true;
  };
  
  const addSchedule = (schedule) => {
    console.log("관광지 추가됨:", schedule, selectedSpot.value);
    showSchedulePopup.value = false;
  };
  </script>
  
  <style scoped>
  .plan-card-col {
    margin-bottom: 16px;
  }
  
  .content-area {
    max-width: 1200px;
    margin: 0 auto;
  }
  
  /* 팝업 스타일 */
  :deep(.v-dialog) {
    border-radius: 8px;
    overflow: hidden;
  }
  </style>