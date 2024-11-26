<template>
    <v-card class="mx-auto my-4" max-width="400">
        <v-toolbar flat color="primary" dark>
            <v-toolbar-title>
                DAY {{ props.day }}
                <span class="text-caption ml-2">
                    {{ formatDate(props.date) }}
                </span>
            </v-toolbar-title>
            <v-spacer></v-spacer>
        </v-toolbar>

        <v-card-text>
            <v-list>
                <v-hover v-for="(item, index) in sortedSchedules" :key="index" v-slot="{ isHovering, props }">
                    <v-list-item
                        v-bind="props"
                        :class="{ 'on-hover': isHovering }"
                    >
                        <template v-slot:prepend>
                            <v-avatar color="primary" size="32" class="white--text">
                                {{ index + 1 }}
                            </v-avatar>
                        </template>

                        <v-list-item-title class="font-weight-bold">
                            {{ item.title }}
                        </v-list-item-title>
                        
                        <v-list-item-subtitle>
                            <div class="d-flex flex-column gap-1">
                                <div class="text-body-2">
                                    <v-icon size="small" class="me-1">mdi-clock-outline</v-icon>
                                    {{ formatTime(item.time) }}
                                </div>
                                <div class="text-body-2">
                                    <v-icon size="small" class="me-1">mdi-text</v-icon>
                                    {{ item.memo || '메모 없음' }}
                                </div>
                                <div class="text-body-2">
                                    <v-icon size="small" class="me-1">mdi-currency-krw</v-icon>
                                    {{ planStore.formatCurrency(item.estimatedCost) }}
                                </div>
                            </div>
                        </v-list-item-subtitle>

                        <template v-slot:append>
                            <v-btn
                                icon="mdi-trash-can-outline"
                                variant="text"
                                size="small"
                                @click="removeSchedule(props.day, index)"
                                aria-label="관광지 삭제"
                            ></v-btn>
                        </template>
                    </v-list-item>
                </v-hover>
            </v-list>

            <v-divider class="my-4"></v-divider>
            
            <div class="d-flex align-center justify-space-between mb-4">
                <span class="text-subtitle-1 font-weight-bold">이 날의 예상 비용</span>
                <span class="text-h6">
                    {{ planStore.formatCurrency(planStore.calculateDayTotalCost(props.date)) }}
                </span>
            </div>

            <v-btn
                block
                color="primary"
                variant="outlined"
                prepend-icon="mdi-plus"
                @click="openWishList"
            >
                관광지 추가
            </v-btn>
        </v-card-text>

        <v-dialog v-model="showDeleteDialog" max-width="300">
            <v-card>
                <v-card-title class="text-h6">
                    일정 삭제
                </v-card-title>
                <v-card-text>
                    DAY {{ props.day }} 일정을 삭제하시겠습니까?
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn
                        color="primary"
                        variant="outlined"
                        @click="closeDeleteDialog"
                    >
                        취소
                    </v-btn>
                    <v-btn
                        color="primary"
                        variant="outlined"
                        @click="emitRemoveDay(props.day)"
                    >
                        삭제
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </v-card>
</template>

<script setup>
import { ref, watch, computed } from 'vue';
import { usePlanStore } from '@/stores/planStores';

const planStore = usePlanStore();
const showDeleteDialog = ref(false);

// 데이터 모니터링을 위한 watch 추가
watch(() => planStore.currentSchedules, (newSchedules) => {
  console.log('Current Schedules in Card:', newSchedules);
}, { immediate: true });

// currentDay 모니터링
watch(() => planStore.currentDay, (newDay) => {
  console.log('Current Day in Card:', newDay);
}, { immediate: true });

const removeSchedule = (day, index) => {
  planStore.removeSchedule(day, index);
};

const closeDeleteDialog = () => {
  showDeleteDialog.value = false;
};

const emitRemoveDay = (day) => {
  planStore.removePlan(day);
  showDeleteDialog.value = false;
};

// 시간 포맷팅 함수 추가
const formatTime = (time) => {
  if (!time) return '시간 미지정';
  return time;
};

// 현재 계획의 날짜 가져오기
const getCurrentPlanDate = () => {
  const currentPlan = planStore.plans.find(plan => plan.day === planStore.currentDay);
  return currentPlan?.date;
};

// 날짜 포맷팅 함수 수정
const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString('ko-KR', {
    month: 'long',
    day: 'numeric',
    weekday: 'short'
  });
};

const props = defineProps({
  day: {
    type: Number,
    required: true
  },
  date: {
    type: String,
    required: true
  },
  schedules: {
    type: Array,
    default: () => []
  }
});

// 정렬된 schedules computed 속성 추가
const sortedSchedules = computed(() => {
  return [...props.schedules].sort((a, b) => {
    if (!a.time) return 1;
    if (!b.time) return -1;
    return a.time.localeCompare(b.time);
  });
});

const openWishList = () => {
  planStore.setSelectedDate(props.date); // 현재 카드의 날짜 저장
  planStore.setShowWishList(true);
};
</script>

<style scoped>
.on-hover {
    background-color: rgba(var(--v-theme-primary), 0.04);
}

:deep(.v-list-item) {
    padding: 12px;
}

:deep(.v-list-item__prepend) {
    margin-right: 12px;
}

:deep(.v-list-item-subtitle) {
    margin-top: 4px;
}
</style>
