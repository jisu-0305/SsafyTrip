<script setup>
import { ref } from 'vue';

// 이미지 페이드 인아웃을 위한 데이터
const images = ref([
  '/img/main1.png',
  '/img/main.jpg', 
  'image3.jpg'
]);

// 캐러셀 아이템
const carouselItems = ref([
  {
    image: '/img/main2.jpg',
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
</script>

<template>
  <v-container class="main-container pa-0">
    <!-- 첫 번째 섹션 -->
    <div class="hero-section">
      <v-container class="inner-container">
        <v-row align="center" no-gutters>
          <v-col cols="12" md="6" class="pa-16">
            <h1 class="text-h3 font-weight-bold mb-6">
              당신만을 위한 여행 플래너
            </h1>
            <p class="text-h5">
              AI로 똑똑하고 완벽하게 일정 챙기기
            </p>
          </v-col>
          <v-col cols="12" md="6">
            <v-fade-transition>
              <v-img
                :src="images[0]"
                height="600"
                cover
              ></v-img>
            </v-fade-transition>
          </v-col>
        </v-row>
      </v-container>
    </div>

    <!-- 두 번째 섹션 -->
    <v-row class="my-16" justify="center">
      <v-col cols="12">
        <v-carousel
          cycle
          height="500"
          hide-delimiter-background
          show-arrows="hover"
        >
          <v-carousel-item
            v-for="(item, i) in carouselItems"
            :key="i"
          >
            <v-row class="fill-height" align="center">
              <v-col cols="12" md="6">
                <v-img
                  :src="item.image"
                  height="400"
                  cover
                ></v-img>
              </v-col>
              <v-col cols="12" md="6" class="pa-16">
                <h2 class="text-h4 mb-6">{{ item.heading }}</h2>
                <p class="text-body-1">{{ item.subtitle }}</p>
              </v-col>
            </v-row>
          </v-carousel-item>
        </v-carousel>
      </v-col>
    </v-row>

    <!-- 세 번째 섹션 -->
    <v-row class="my-16 px-4">
      <v-col cols="12" class="text-center mb-16">
        <h2 class="text-h4 mb-4">거주지에서 가까운 거리의 여행지를 추천해드려요</h2>
        <p class="text-subtitle-1">서울 근처에서 인기 많은 여행지</p>
      </v-col>
      
      <v-col 
        v-for="n in 4" 
        :key="n" 
        cols="12" 
        sm="6" 
        md="3"
      >
        <v-card>
          <v-img
            src="place-image.jpg"
            height="200"
            cover
          ></v-img>
          <v-card-title>여행지 이름</v-card-title>
          <v-card-subtitle>간단한 설명</v-card-subtitle>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.main-container {
  max-width: 1440px;
  margin: 0 auto;
}

.hero-section {
  background-color: #f8f9fa;
  width: 100vw;
  position: relative;
  left: 50%;
  right: 50%;
  margin-left: -50vw;
  margin-right: -50vw;
}

.inner-container {
  max-width: 1440px;
  margin: 0 auto;
  min-height: 600px;
}
</style>
