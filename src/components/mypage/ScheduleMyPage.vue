<script setup>
import { ref, onMounted } from 'vue';
import { usePlanStore } from '@/stores/planStores';
import PageHeader from '@/components/common/PageHeader.vue';
import { useRouter } from 'vue-router';

const planStore = usePlanStore();
const router = useRouter();
const schedules = ref([]);
const isLoading = ref(true);

onMounted(async () => {
  try {
    const response = await planStore.fetchAllSchedules();
    schedules.value = response;
  } catch (error) {
    console.error('일정 목록을 불러오는 중 오류 발생:', error);
  } finally {
    isLoading.value = false;
  }
});

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
};

const navigateToDetail = (scheduleId) => {
  router.push({
    name: 'schedule-detail',
    params: { id: scheduleId }
  });
};
</script>

<template>
  <v-container fluid class="page-container">
    <v-row justify="center">
      <v-col cols="12" class="content-wrapper">
        <div class="inner-content">
          <PageHeader title="나의 여행 일정" icon="mdi-calendar-check" />

          <!-- 로딩 상태 -->
          <div v-if="isLoading" class="d-flex justify-center align-center my-8">
            <v-progress-circular indeterminate color="primary"></v-progress-circular>
          </div>

          <!-- 일정이 없는 경우 -->
          <v-card
            v-else-if="!schedules.length"
            class="text-center pa-8"
            variant="outlined"
          >
            <v-icon
              icon="mdi-calendar-blank"
              size="64"
              color="grey-lighten-1"
              class="mb-4"
            ></v-icon>
            <div class="text-h6 text-grey-darken-1 mb-4">
              아직 생성된 여행 일정이 없습니다
            </div>
            <v-btn
              color="primary"
              @click="router.push('/attract-plan')"
              prepend-icon="mdi-plus"
            >
              새로운 여행 계획하기
            </v-btn>
          </v-card>

          <!-- 일정 목록 -->
          <v-row v-else>
            <v-col
              v-for="schedule in schedules"
              :key="schedule.scheduleId"
              cols="12"
              sm="6"
              md="4"
            >
              <v-card
                class="h-100"
                variant="outlined"
                @click="navigateToDetail(schedule.scheduleId)"
              >
                <v-card-title class="text-h6 pa-4">
                  {{ schedule.title }}
                </v-card-title>
                
                <v-card-text>
                  <div class="d-flex align-center mb-2">
                    <v-icon size="small" class="me-2">mdi-calendar-range</v-icon>
                    <span class="text-body-2">
                      {{ formatDate(schedule.startDate) }} - {{ formatDate(schedule.endDate) }}
                    </span>
                  </div>
                  
                  <div v-if="schedule.memo" class="d-flex align-center">
                    <v-icon size="small" class="me-2">mdi-note-text</v-icon>
                    <span class="text-body-2 text-truncate">{{ schedule.memo }}</span>
                  </div>
                </v-card-text>

                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn
                    variant="text"
                    color="primary"
                    size="small"
                  >
                    자세히 보기
                    <v-icon end>mdi-chevron-right</v-icon>
                  </v-btn>
                </v-card-actions>
              </v-card>
            </v-col>
          </v-row>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.v-card {
  transition: transform 0.2s, box-shadow 0.2s;
  cursor: pointer;
}

.v-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1) !important;
}

.text-truncate {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
}
</style>
