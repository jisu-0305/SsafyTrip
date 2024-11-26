<template>
  <v-container>
    <v-card flat>
      <v-card-text>
        <v-form>
          <!-- 여행 제목 -->
          <v-text-field
            v-model="travelTitle"
            label="여행 제목"
            variant="outlined"
            density="comfortable"
            prepend-inner-icon="mdi-format-title"
            class="mb-4"
            hide-details="auto"
            @update:model-value="handleTitleUpdate"
          ></v-text-field>

          <v-row>
            <!-- 출발 날짜 -->
            <v-col cols="12" md="6">
              <v-text-field
                v-model="startDateFormatted"
                label="출발 날짜"
                variant="outlined"
                readonly
                density="comfortable"
                prepend-inner-icon="mdi-calendar-start"
                hide-details="auto"
                @click="startDialog = true"
              ></v-text-field>

              <v-dialog v-model="startDialog" width="auto">
                <v-card>
                  <v-card-title class="text-center px-4 py-3">
                    출발 날짜 선택
                  </v-card-title>
                  <v-divider></v-divider>
                  <v-date-picker
                    v-model="startDate"
                    @update:model-value="handleStartDateChange"
                  ></v-date-picker>
                </v-card>
              </v-dialog>
            </v-col>

            <!-- 도착 날짜 -->
            <v-col cols="12" md="6">
              <v-text-field
                v-model="endDateFormatted"
                label="도착 날짜"
                variant="outlined"
                readonly
                density="comfortable"
                prepend-inner-icon="mdi-calendar-end"
                hide-details="auto"
                @click="endDialog = true"
              ></v-text-field>

              <v-dialog v-model="endDialog" width="auto">
                <v-card>
                  <v-card-title class="text-center px-4 py-3">
                    도착 날짜 선택
                  </v-card-title>
                  <v-divider></v-divider>
                  <v-date-picker
                    v-model="endDate"
                    @update:model-value="handleEndDateChange"
                  ></v-date-picker>
                </v-card>
              </v-dialog>
            </v-col>
          </v-row>

          <!-- 총 비용 -->
          <v-text-field
            :model-value="formatCurrency(totalCost)"
            label="총 예상 비용"
            variant="outlined"
            density="comfortable"
            readonly
            prepend-inner-icon="mdi-currency-krw"
            class="my-4"
            hide-details="auto"
          ></v-text-field>

          <!-- 메모 -->
          <v-textarea
            v-model="memo"
            label="메모"
            variant="outlined"
            counter="300"
            rows="3"
            auto-grow
            density="comfortable"
            prepend-inner-icon="mdi-note-text"
            hide-details="auto"
            @update:model-value="handleMemoUpdate"
          ></v-textarea>
        </v-form>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script setup>
import { ref, computed } from 'vue';

const props = defineProps(['totalCost']);
const emit = defineEmits(['update:travelTitle', 'update:startDate', 'update:endDate', 'update:memo']);

const travelTitle = ref('');
const startDate = ref(null);
const endDate = ref(null);
const memo = ref('');
const startDialog = ref(false);
const endDialog = ref(false);

// 날짜 포맷팅
const formatDate = (date) => {
  if (!date) return '';
  return new Date(date).toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'short'
  });
};

// 통화 포맷팅
const formatCurrency = (value) => {
  return new Intl.NumberFormat('ko-KR', {
    style: 'currency',
    currency: 'KRW'
  }).format(value || 0);
};

const startDateFormatted = computed(() => formatDate(startDate.value));
const endDateFormatted = computed(() => formatDate(endDate.value));

// 이벤트 핸들러
const handleTitleUpdate = (value) => {
  emit('update:travelTitle', value);
};

const handleStartDateChange = (value) => {
  startDialog.value = false;
  startDate.value = value;
  emit('update:startDate', value);
};

const handleEndDateChange = (value) => {
  endDialog.value = false;
  endDate.value = value;
  emit('update:endDate', value);
};

const handleMemoUpdate = (value) => {
  emit('update:memo', value);
};
</script>

<style scoped>
.v-text-field :deep(.v-field__input) {
  min-height: 44px;
}

.v-text-field :deep(.v-field__prepend-inner) {
  padding-inline-start: 8px;
}
</style>
