<template>
  <v-container>
    <v-card flat>
      <v-card-text>
        <v-form ref="form" v-model="isFormValid">
          <!-- 여행 제목 -->
          <v-text-field
            v-model="planStore.travelTitle"
            label="여행 제목"
            variant="outlined"
            density="comfortable"
            prepend-inner-icon="mdi-format-title"
            class="mb-4"
            :rules="[v => !!v || '여행 제목을 입력해주세요']"
            @update:model-value="planStore.setTravelTitle"
          ></v-text-field>

          <v-row>
            <!-- 출발 날짜 -->
            <v-col cols="12" md="6">
              <v-text-field
                :model-value="planStore.startDateFormatted"
                label="출발 날짜"
                variant="outlined"
                readonly
                density="comfortable"
                prepend-inner-icon="mdi-calendar-start"
                :rules="[v => !!planStore.startDate || '출발 날짜를 선택해주세요']"
                @click="startDialog = true"
              ></v-text-field>

              <v-dialog v-model="startDialog" width="auto">
                <v-card>
                  <v-card-title class="text-center px-4 py-3">
                    출발 날짜 선택
                  </v-card-title>
                  <v-divider></v-divider>
                  <v-date-picker
                    v-model="startDateTemp"
                    @update:model-value="handleStartDateChange"
                  ></v-date-picker>
                </v-card>
              </v-dialog>
            </v-col>

            <!-- 도착 날짜 -->
            <v-col cols="12" md="6">
              <v-text-field
                :model-value="planStore.endDateFormatted"
                label="도착 날짜"
                variant="outlined"
                readonly
                density="comfortable"
                prepend-inner-icon="mdi-calendar-end"
                :rules="[v => !!planStore.endDate || '도착 날짜를 선택해주세요']"
                @click="endDialog = true"
              ></v-text-field>

              <v-dialog v-model="endDialog" width="auto">
                <v-card>
                  <v-card-title class="text-center px-4 py-3">
                    도착 날짜 선택
                  </v-card-title>
                  <v-divider></v-divider>
                  <v-date-picker
                    v-model="endDateTemp"
                    :min="planStore.startDate"
                    @update:model-value="handleEndDateChange"
                  ></v-date-picker>
                </v-card>
              </v-dialog>
            </v-col>
          </v-row>

          <!-- 총 비용 -->
          <v-text-field
            :model-value="planStore.formatCurrency(planStore.calculateTotalCost)"
            label="총 예상 비용"
            variant="outlined"
            density="comfortable"
            readonly
            prepend-inner-icon="mdi-wallet"
            class="my-4"
            bg-color="grey-lighten-3"
            :placeholder="planStore.calculateTotalCost ? undefined : '예상 비용이 계산됩니다.'"
          ></v-text-field>

          <!-- 메모 -->
          <v-textarea
            v-model="planStore.memo"
            label="메모"
            variant="outlined"
            counter="300"
            rows="3"
            auto-grow
            density="comfortable"
            prepend-inner-icon="mdi-note-text"
            hide-details="auto"
            @update:model-value="planStore.setMemo"
          ></v-textarea>
        </v-form>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script setup>
import { ref } from 'vue';
import { usePlanStore } from '@/stores/planStores';

const planStore = usePlanStore();
const form = ref(null);
const startDialog = ref(false);
const endDialog = ref(false);
const startDateTemp = ref(null);
const endDateTemp = ref(null);

const handleStartDateChange = (value) => {
  if (value) {
    console.log('Start date selected:', value);
    planStore.setStartDate(value);
    startDateTemp.value = value;
    startDialog.value = false;
  }
};

const handleEndDateChange = (value) => {
  if (value) {
    console.log('End date selected:', value);
    planStore.setEndDate(value);
    endDateTemp.value = value;
    endDialog.value = false;
  }
};

const emit = defineEmits(['update:startDate', 'update:endDate', 'update:travelTitle', 'update:memo']);
</script>

<style scoped>
.v-text-field :deep(.v-field__input) {
  min-height: 44px;
}

.v-text-field :deep(.v-field__prepend-inner) {
  padding-inline-start: 8px;
}

/* 읽기 전용 필드 스타일 */
.v-text-field.readonly :deep(.v-field__input) {
  background-color: rgb(var(--v-theme-grey-lighten-3));
}
</style>
