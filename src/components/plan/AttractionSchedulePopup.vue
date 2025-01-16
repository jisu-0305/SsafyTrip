<template>
  <div>
    <v-toolbar color="primary" dark flat>
      <v-toolbar-title>
        일정 추가
        <span class="text-caption ml-2">
          {{ formatDate(planStore.selectedDate) }}
        </span>
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn icon @click="closePopup">
        <v-icon>mdi-close</v-icon>
      </v-btn>
    </v-toolbar>

    <div class="pa-6">
      <v-text-field
        :model-value="planStore.selectedSpot?.title"
        label="관광지 이름"
        readonly
        variant="outlined"
        density="comfortable"
        class="mb-4"
      ></v-text-field>

      <v-text-field
        :model-value="planStore.timeFormatted"
        label="방문 시간 선택"
        prepend-icon="mdi-clock"
        readonly
        variant="outlined"
        density="comfortable"
        class="mb-4"
        @click="openTimePicker"
      ></v-text-field>

      <v-textarea
        v-model="planStore.scheduleMemo"
        label="메모"
        variant="outlined"
        counter="200"
        rows="3"
        density="comfortable"
        class="mb-4"
      ></v-textarea>

      <v-text-field
        v-model="planStore.estimatedCost"
        label="예상 금액"
        variant="outlined"
        type="number"
        density="comfortable"
        prefix="₩"
        class="mb-4"
      ></v-text-field>

      <v-btn
        color="primary"
        size="large"
        block
        @click="addSchedule"
        :disabled="!planStore.scheduleTime"
        class="mt-6"
      >
        일정 추가
      </v-btn>
    </div>

    <v-dialog v-model="planStore.timeDialog" width="auto">
      <v-card>
        <v-card-title class="text-primary pa-4">방문 시간 선택</v-card-title>
        <v-divider></v-divider>
        <VTimePicker
          v-model="selectedTime"
          color="primary"
          header-color="primary"
          format="24hr"
        >
          <template v-slot:actions>
            <v-spacer></v-spacer>
            <v-btn
              color="primary"
              variant="text"
              @click="planStore.setTimeDialog(false)"
            >
              취소
            </v-btn>
            <v-btn
              color="primary"
              variant="text"
              @click="confirmTime"
            >
              확인
            </v-btn>
          </template>
        </VTimePicker>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import { usePlanStore } from '@/stores/planStores';
import { VTimePicker } from 'vuetify/labs/VTimePicker'

const planStore = usePlanStore();
const selectedTime = ref(null);

// planStore의 scheduleTime이 변경될 때 selectedTime 업데이트
watch(() => planStore.scheduleTime, (newTime) => {
  selectedTime.value = newTime;
});

const closePopup = () => {
  planStore.setShowSchedulePopup(false);
  planStore.resetScheduleForm();
};

const openTimePicker = () => {
  selectedTime.value = planStore.scheduleTime || '00:00';  // 기본값 설정
  planStore.setTimeDialog(true);
};

const confirmTime = () => {
  if (selectedTime.value) {
    // 24시간 형식으로 시간 저장
    const formattedTime = new Date(`2000-01-01T${selectedTime.value}`).toLocaleTimeString('ko-KR', {
      hour: '2-digit',
      minute: '2-digit',
      hour12: false
    });
    planStore.setScheduleTime(formattedTime);
    planStore.setTimeDialog(false);
  }
};

// 날짜 포맷팅 함수 추가
const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString('ko-KR', {
    month: 'long',
    day: 'numeric',
    weekday: 'short'
  });
};

const addSchedule = () => {
  if (!planStore.scheduleTime || !planStore.selectedDate) {
    console.error('필수 정보가 누락되었습니다.');
    return;
  }
  
  // 마커 추가
  planStore.addMarker(planStore.selectedSpot, planStore.currentDay);
  
  // 일정 추가
  planStore.addSchedule();
  closePopup();
};
</script>

<style scoped>
:deep(.v-field__input) {
  font-size: 14px;
}

:deep(.v-label) {
  font-size: 14px;
}

:deep(.v-text-field .v-input__details) {
  padding-inline-start: 0;
}
</style>
