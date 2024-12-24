<script setup>
import { ref, onMounted } from 'vue';
import { useAuthStore } from '@/stores/authStores';
import { useAttractionStore } from '@/stores/attractionStore';

const authStore = useAuthStore();
const attractionStore = useAttractionStore();

// 이미지 페이드 인아웃을 위한 데이터
const images = ref([
  { src: '/img/main1.png', loading: 'eager', alt: '메인 이미지 1' },
  { src: '/img/main.jpg', loading: 'lazy', alt: '메인 이미지 2' },
  { src: 'image3.jpg', loading: 'lazy', alt: '메인 이미지 3' }
]);

// 캐러셀 아이템
const carouselItems = ref([
  {
    image: '/img/main2.jpg',
    loading: 'eager',
    heading: '여행을 준비하는 지수님은 인싸루트의 AI 코디추천으로 완벽하게 여행을 준비하셨어요.',
    subtitle: '여자친구와 제주 여행을 준비하는 지수님은 날씨와 지역을 고려하며 늘 1시간 이상 고민하던 코디를 인싸루트를 통해 단 5분만에 정할 수 있었어요!'
  },
  {
    image: '/img/main3.jpg', 
    heading: '북촌의 방문시간을 알고 계신가요?',
    subtitle: '북촌 한옥마을은 주말 오전이 가장 한산합니다. 현지인이 알려주는 꿀팁으로 더 즐거운 여행을 준비하세요.'
  },
  {
    image: '/img/main4.jpg',
    heading: 'AI가 추천하는 맞춤형 여행 코스',
    subtitle: '당신의 취향과 일정에 맞는 최적의 여행 코스를 AI가 추천해드립니다. 더 이상 고민하지 마세요!'
  }
]);

const popularAttractions = ref([]);
const currentSidoName = ref('전국');

const getSidoCodeFromSidoList = (address, sidoList) => {
  const sido = sidoList.find(item => address.startsWith(item.name));
  return {
    code: sido ? sido.code : '1', // 기본값 서울
    name: sido ? sido.name : '서울특별시'
  };
};

onMounted(async () => {
  // 시도 목록 조회
  const sidoList = await attractionStore.fetchSidoList();
  
  if (authStore.user?.address) {
    const { code: sidoCode, name: sidoName } = getSidoCodeFromSidoList(authStore.user.address, sidoList);
    const result = await attractionStore.fetchPopularAttractions(sidoCode);
    popularAttractions.value = result.attractions;
    currentSidoName.value = sidoName;
  } else {
    const result = await attractionStore.fetchPopularAttractions();
    popularAttractions.value = result.attractions;
    currentSidoName.value = '전국';
  }
});
</script>
<template>
  <div class="hero-section">
    <div class="content-wrapper">
      <v-row align="center" justify="space-between" no-gutters>
        <v-col cols="12" md="6" class="hero-text">
          <div class="text-wrapper">
            <h1 class="text-h3 font-weight-bold mb-4">
              <span class="title-text">당신만을 위한 </span>
              <span class="title-text">여행 플래너</span>
            </h1>
            <p class="text-h5">
              AI로 똑똑하고 완벽하게 일정 챙기기
            </p>
          </div>
        </v-col>
        <v-col cols="12" md="6" class="hero-image-col">
          <v-fade-transition>
            <v-img
              :src="images[0].src"
              :alt="images[0].alt"
              :loading="images[0].loading"
              :height="$vuetify.display.mdAndUp ? '37.5rem' : '25rem'"
              cover
              class="hero-image"
            ></v-img>
          </v-fade-transition>
        </v-col>
      </v-row>
    </div>
  </div>

  <div class="content-wrapper">
    <v-row class="carousel-section" justify="center">
      <v-col cols="12">
        <v-carousel
          cycle
          :height="$vuetify.display.mdAndUp ? '31.25rem' : '43.75rem'"
          hide-delimiter-background
          show-arrows="hover"
        >
          <v-carousel-item
            v-for="(item, i) in carouselItems"
            :key="i"
          >
            <v-row class="fill-height" align="center">
              <v-col 
                cols="12" 
                md="6"
                class="carousel-image-col"
              >
                <v-img
                  :src="item.image"
                  :loading="item.loading"
                  :height="$vuetify.display.mdAndUp ? '25rem' : '18.75rem'"
                  cover
                  class="rounded-lg carousel-image"
                ></v-img>
              </v-col>
              <v-col 
                cols="12" 
                md="6"
                class="carousel-text-col"
              >
                <h2 class="carousel-heading">{{ item.heading }}</h2>
                <p class="carousel-subtitle">{{ item.subtitle }}</p>
              </v-col>
            </v-row>
          </v-carousel-item>
        </v-carousel>
      </v-col>
    </v-row>

    <v-row class="popular-section">
      <v-col cols="12" class="text-center popular-header">
        <h2 class="popular-title">
          <strong>{{ currentSidoName }}</strong> 인기 여행지
        </h2>
        <p class="popular-subtitle">좋아요 기준 TOP 4</p>
        <p class="popular-description">
          {{ currentSidoName === '전국' 
            ? '전국의 인기 여행지를 소개합니다' 
            : `${currentSidoName}의 매력적인 여행지를 만나보세요` 
          }}
        </p>
      </v-col>
      
      <v-col 
        v-for="attraction in popularAttractions" 
        :key="attraction.contentId" 
        cols="12" 
        sm="6" 
        md="3"
        class="attraction-col"
      >
        <v-card elevation="2" class="attraction-card" @click="$router.push(`/attraction/${attraction.no}`)">
          <v-img
            :src="attraction.firstImage1"
            height="12.5rem"
            cover
            class="attraction-image"
          >
            <template v-slot:placeholder>
              <v-row align="center" justify="center" class="fill-height">
                <v-icon icon="mdi-image-off" size="4rem"></v-icon>
              </v-row>
            </template>
          </v-img>
          <v-card-title class="attraction-title">{{ attraction.title }}</v-card-title>
          <v-card-subtitle class="attraction-address">{{ attraction.addr1 }}</v-card-subtitle>
          <v-card-text class="attraction-stats">
            <div class="d-flex align-center gap-4">
              <div class="d-flex align-center">
                <v-icon size="small" color="grey" class="me-1">mdi-eye</v-icon>
                <span class="text-caption">{{ attraction.views }}</span>
              </div>
              <div class="d-flex align-center">
                <v-icon size="small" color="error" class="me-1">mdi-heart</v-icon>
                <span class="text-caption">{{ attraction.hit }}</span>
              </div>
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<style scoped>
.hero-section {
  composes: full-width-section from '@/assets/styles/common.css';
  background-color: var(--color-background);
  padding: var(--section-padding) 0;
}

.content-wrapper {
  max-width: 1464px;
  margin: 0 auto;
  padding: 0 16px;
}

.hero-text {
  padding: 2rem;
  position: relative;
  z-index: 2;
}

.text-wrapper {
  background-color: rgba(248, 249, 250, 0.9);
  padding: 1rem;
  border-radius: var(--border-radius-md);
}

.carousel-section {
  margin: clamp(1rem, 5vw, 4rem) 0;
}

.popular-section {
  composes: section-container from '@/assets/styles/common.css';
}

@media (max-width: 1100px) {
  .content-wrapper {
    padding: 0 24px;
  }
}

@media (max-width: 960px) {
  .hero-text {
    text-align: center;
    padding: 1.5rem 1rem;
  }
}

@media (max-width: 600px) {
  .hero-text {
    padding: 1rem 0.5rem;
  }
}
</style>

