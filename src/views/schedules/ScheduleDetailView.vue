<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { usePlanStore } from '@/stores/planStores';
import { useAttractionDetailStore } from '@/stores/attractionDetailStore';
import PageHeader from '@/components/common/PageHeader.vue';
import { useAuthStore } from '@/stores/authStores';
import { useAiStore } from '@/stores/aiStores';

const route = useRoute();
const router = useRouter();
const planStore = usePlanStore();
const authStore = useAuthStore();
const attractionDetailStore = useAttractionDetailStore();
const aiStore = useAiStore();
const scheduleDetail = ref(null);
const isLoading = ref(true);
const attractionDetails = ref(new Map());
const showAiContent = ref(false);

// 날짜 포맷팅 함수
const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
};

// 시간 포맷팅 함수 수정
const formatTimeToISO = (dateTimeString, date) => {
  const time = new Date(dateTimeString);
  const hours = time.getHours().toString().padStart(2, '0');
  const minutes = time.getMinutes().toString().padStart(2, '0');
  return `${date}T${hours}:${minutes}:00`;
};

// 여행 기간 계산
const travelPeriod = computed(() => {
  if (!scheduleDetail.value?.schedule) return '';
  const { startDate, endDate } = scheduleDetail.value.schedule;
  return `${formatDate(startDate)} ~ ${formatDate(endDate)}`;
});

// 관광지 상세 정보 조회 함수
const fetchAttractionDetails = async (places) => {
  for (const place of places) {
    try {
      await attractionDetailStore.fetchAttractionDetail(place.attractionId);
      if (attractionDetailStore.attraction) {
        attractionDetails.value.set(
          place.attractionId, 
          attractionDetailStore.attraction
        );
      }
    } catch (error) {
      console.error(`관광지 정보 조회 실패 (ID: ${place.attractionId}):`, error);
    }
  }
};

// 날씨 아이콘 매핑 함수
const getWeatherIcon = (condition) => {
  const iconMap = {
    '맑음': 'mdi-weather-sunny',
    '흐림': 'mdi-weather-cloudy',
    '비': 'mdi-weather-rainy',
    '눈': 'mdi-weather-snowy',
    // 필요한 날씨 조건에 따라 추가
  };
  return iconMap[condition] || 'mdi-weather-cloudy';
};

// 이미지 새 창에서 열기 함수 추가
const openImageInNewWindow = (imageUrl) => {
  if (!imageUrl) return;
  window.open(imageUrl, '_blank', 'width=800,height=600');
};

onMounted(async () => {
  try {
    if (!authStore.isLoggedIn) {
      alert('로그인이 필요한 서비스입니다.');
      router.push('/login');
      return;
    }

    const scheduleId = route.params.id;
    if (!scheduleId) {
      alert('잘못된 접근입니다.');
      router.push('/schedules');
      return;
    }

    const response = await planStore.fetchScheduleDetail(scheduleId);
    if (!response) {
      alert('일정을 찾을 수 없습니다.');
      router.push('/schedules');
      return;
    }
    
    scheduleDetail.value = response;

    // 모든 장소의 관광지 상세 정보 조회
    const allPlaces = response.schedulePlacesByDate.flatMap(date => date.places);
    await fetchAttractionDetails(allPlaces);
  } catch (error) {
    console.error('일정 상세 정보를 불러오는 중 오류 발생:', error);
    alert('일정을 불러오는데 실패했습니다.');
    router.push('/schedules');
  } finally {
    isLoading.value = false;
  }
});

// scheduleDetail이 변경될 때 AI 분석 실행
watch(() => scheduleDetail.value, async (newSchedule) => {
  if (newSchedule) {
    try {
      await aiStore.analyzeSchedule(newSchedule);
    } catch (error) {
      console.error('AI 분석 중 오류 발생:', error);
    }
  }
}, { immediate: true });

// AI 분석 시작 함수 수정
const startAiAnalysis = async () => {
  if (!scheduleDetail.value) return;
  
  try {
    const formattedData = {
      schedule: {
        scheduleId: scheduleDetail.value.schedule.scheduleId,
        title: scheduleDetail.value.schedule.title,
        memo: scheduleDetail.value.schedule.memo,
        totalCost: scheduleDetail.value.schedule.totalCost,
        startDate: scheduleDetail.value.schedule.startDate,
        endDate: scheduleDetail.value.schedule.endDate
      },
      schedulePlacesByDate: scheduleDetail.value.schedulePlacesByDate.map(dateSchedule => ({
        date: dateSchedule.date,
        places: dateSchedule.places.map(place => ({
          attractionId: place.attractionId,
          visitTime: `${dateSchedule.date}T${new Date(place.visitTime).toTimeString().slice(0, 8)}`,
          memo: place.memo,
          cost: place.cost,
          visitOrder: place.visitOrder
        }))
      }))
    };

    console.log('AI 분석 요청 데이터:', JSON.stringify(formattedData));
    await aiStore.analyzeSchedule(formattedData);
    showAiContent.value = true;
  } catch (error) {
    console.error('AI 분석 중 오류 발생:', error);
    alert('여행 준비 분석에 실패했습니다. 다시 시도해주세요.');
  }
};
</script>

<template>
  <v-container fluid class="page-container">
    <v-row justify="center">
      <v-col cols="12" class="content-wrapper">
        <div class="inner-content">
          <PageHeader title="여행 일정 상세" icon="mdi-calendar-text" />
          
          <v-row>
            <!-- 왼쪽 일정 섹션 (6/12 = 50%) -->
            <v-col cols="12" md="6">
              <!-- 로딩 상태 -->
              <div v-if="isLoading" class="d-flex justify-center align-center my-8">
                <v-progress-circular indeterminate color="primary"></v-progress-circular>
              </div>

              <!-- 일정 상세 정보 -->
              <div v-else-if="scheduleDetail" class="schedule-content">
                <v-card class="mb-6">
                  <v-card-title class="text-h5 font-weight-bold">
                    {{ scheduleDetail.schedule.title }}
                  </v-card-title>
                  <v-card-text>
                    <div class="text-subtitle-1 mb-2">{{ travelPeriod }}</div>
                    <div class="text-body-1 mb-2">
                      총 예상 비용: {{ planStore.formatCurrency(scheduleDetail.schedule.totalCost) }}
                    </div>
                    <div class="text-body-1" v-if="scheduleDetail.schedule.memo">
                      메모: {{ scheduleDetail.schedule.memo }}
                    </div>
                  </v-card-text>
                </v-card>

                <!-- 날짜별 일정 -->
                <div v-for="dateSchedule in scheduleDetail.schedulePlacesByDate" 
                     :key="dateSchedule.date" 
                     class="mb-6">
                  <v-card>
                    <v-card-title class="text-h6 bg-grey-lighten-3">
                      {{ formatDate(dateSchedule.date) }}
                    </v-card-title>
                    <v-card-text>
                      <v-timeline density="compact">
                        <v-timeline-item
                          v-for="place in dateSchedule.places"
                          :key="place.attractionId"
                          size="small"
                        >
                          <template v-slot:opposite>
                            {{ formatTime(place.visitTime) }}
                          </template>
                          <v-card class="mb-2 attraction-card" width="100%">
                            <div class="d-flex attraction-content">
                              <!-- 관광지 이미지 -->
                              <div class="image-container">
                                <v-img
                                  v-if="attractionDetails.get(place.attractionId)?.firstImage1"
                                  :src="attractionDetails.get(place.attractionId)?.firstImage1"
                                  height="120"
                                  width="120"
                                  cover
                                  class="rounded-s"
                                ></v-img>
                                <v-icon
                                  v-else
                                  size="120"
                                  color="grey-lighten-1"
                                  class="no-image-icon"
                                >
                                  mdi-image-off
                                </v-icon>
                              </div>
                              <!-- 관광지 정보 -->
                              <div class="pa-3 flex-grow-1">
                                <div class="text-subtitle-1 mb-1">
                                  {{ attractionDetails.get(place.attractionId)?.title || '관광지 정보 없음' }}
                                </div>
                                <div class="text-body-2 mb-1">
                                  {{ attractionDetails.get(place.attractionId)?.addr1 || '주소 정보 없음' }}
                                </div>
                                <div class="text-body-2" v-if="place.memo">
                                  메모: {{ place.memo }}
                                </div>
                                <div class="text-body-2">
                                  예상 비용: {{ planStore.formatCurrency(place.cost) }}
                                </div>
                              </div>
                            </div>
                          </v-card>
                        </v-timeline-item>
                      </v-timeline>
                    </v-card-text>
                  </v-card>
                </div>
              </div>

              <!-- 에러 상태 -->
              <v-card v-else class="text-center pa-8" variant="outlined">
                <v-icon icon="mdi-alert-circle" size="64" color="error" class="mb-4"></v-icon>
                <div class="text-h6 text-grey-darken-1 mb-4">
                  일정을 불러올 수 없습니다
                </div>
                <v-btn
                  color="primary"
                  @click="router.push('/schedules')"
                  prepend-icon="mdi-arrow-left"
                >
                  일정 목록으로 돌아가기
                </v-btn>
              </v-card>
            </v-col>

            <!-- 오른쪽 날씨/코디 섹션 (6/12 = 50%) -->
            <v-col cols="12" md="6">
              <div class="sticky-container">
                <div v-if="!showAiContent" class="d-flex justify-center align-center" style="height: 90vh">
                  <v-btn
                    color="primary"
                    size="x-large"
                    @click="startAiAnalysis"
                    :loading="aiStore.isLoading"
                    :disabled="aiStore.isLoading"
                    prepend-icon="mdi-brain"
                  >
                    {{ aiStore.isLoading ? '분석 중...' : '스마트하게 여행준비하기' }}
                  </v-btn>
                </div>

                <!-- 로딩 상태 표시 -->
                <div v-else-if="aiStore.isLoading" class="d-flex flex-column justify-center align-center" style="height: 90vh">
                  <v-progress-circular
                    indeterminate
                    color="primary"
                    size="64"
                  ></v-progress-circular>
                  <div class="text-h6 mt-4">여행 준비를 분석하고 있습니다...</div>
                  <div class="text-subtitle-1 mt-2">잠시만 기다려주세요</div>
                </div>

                <!-- AI 분석 결과 표시 -->
                <div v-else>
                  <!-- 날씨 섹션 -->
                  <v-card 
                    class="mb-4" 
                    height="30vh"
                    color="blue-lighten-5"
                  >
                    <v-card-title class="text-h6">
                      <v-icon start>mdi-weather-partly-cloudy</v-icon>
                      날씨 정보
                    </v-card-title>
                    <v-card-text>
                      <v-list v-if="aiStore.weatherList.length" bg-color="transparent">
                        <v-list-item
                          v-for="(weather, index) in aiStore.weatherList"
                          :key="index"
                          :subtitle="formatDate(weather.date)"
                          :class="blue"
                        >
                          <template v-slot:prepend>
                            <v-icon :icon="getWeatherIcon(weather.condition)"></v-icon>
                          </template>
                          <v-list-item-title class="d-flex align-center justify-space-between">
                            <div>
                              <span class="text-h6">{{ weather.highTemperature }}°C</span>
                              <span class="text-subtitle-1 mx-2">/</span>
                              <span class="text-body-1">{{ weather.lowTemperature }}°C</span>
                            </div>
                            <div class="text-right">
                              <div class="text-body-1">{{ weather.weatherCondition }}</div>
                              <div class="text-caption text-grey">강수확률: {{ weather.precipitation }}</div>
                            </div>
                          </v-list-item-title>
                        </v-list-item>
                      </v-list>
                    </v-card-text>
                  </v-card>

                  <!-- 코디 추천 섹션 -->
                  <v-card 
                    height="60vh"
                    color="yellow-lighten-5"
                  >
                    <v-card-title class="text-h6">
                      <v-icon start>mdi-hanger</v-icon>
                      코디 & 준비물 추천
                    </v-card-title>
                    <v-card-text>
                      <v-row>
                        <!-- 코디 이미지 -->
                        <v-col cols="12" class="mb-4">
                          <v-img
                            v-if="aiStore.clothesURL"
                            :src="aiStore.clothesURL"
                            height="200"
                            contain
                            class="rounded-lg cursor-pointer"
                            @click="openImageInNewWindow(aiStore.clothesURL)"
                          ></v-img>
                        </v-col>
                        
                        <!-- 준비물 목록 -->
                        <v-col cols="12">
                          <v-expansion-panels v-if="aiStore.supplies">
                            <v-expansion-panel
                              v-for="(items, category) in aiStore.getParsedSupplies()"
                              :key="category"
                            >
                              <v-expansion-panel-title>
                                {{ category }}
                              </v-expansion-panel-title>
                              <v-expansion-panel-text>
                                <v-list density="compact">
                                  <v-list-item
                                    v-for="(item, index) in items"
                                    :key="index"
                                    :title="item"
                                  >
                                    <template v-slot:prepend>
                                      <v-icon size="small">mdi-circle-small</v-icon>
                                    </template>
                                  </v-list-item>
                                </v-list>
                              </v-expansion-panel-text>
                            </v-expansion-panel>
                          </v-expansion-panels>
                        </v-col>
                      </v-row>
                    </v-card-text>
                  </v-card>
                </div>
              </div>
            </v-col>
          </v-row>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.schedule-content {
  word-wrap: break-word;
  overflow-wrap: break-word;
}

.text-wrap {
  white-space: normal;
  word-break: break-word;
}

.sticky-container {
  position: sticky;
  top: 24px;
  height: calc(100vh - 48px);
  overflow-y: auto;
}

.schedule-content {
  margin-right: 16px; /* 오른쪽 섹션과의 간격 */
}

/* 스크롤바 스타일링 */
.sticky-container::-webkit-scrollbar {
  width: 8px;
}

.sticky-container::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.sticky-container::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 4px;
}

.sticky-container::-webkit-scrollbar-thumb:hover {
  background: #555;
}

/* 반응형 스타일 */
@media (max-width: 960px) {
  .schedule-content {
    margin-right: 0;
  }
  
  .sticky-container {
    position: relative;
    top: 0;
    height: auto;
    margin-top: 16px;
  }
}

.attraction-card {
  width: 100%;
  min-height: 120px;
}

.attraction-content {
  width: 100%;
  height: 100%;
}

.image-container {
  width: 120px;
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f5f5;
  overflow: hidden;
}

.image-container img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.no-image-icon {
  width: 120px;
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.cursor-pointer {
  cursor: pointer;
}
</style> 