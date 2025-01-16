<script setup>
import { onMounted, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { storeToRefs } from 'pinia';
import { useAttractionDetailStore } from '@/stores/attractionDetailStore';
import PageHeader from "@/components/common/PageHeader.vue";
import { useAuthStore } from '@/stores/authStores';
import axios from 'axios';
import AttractComment from '@/components/attract/AttractComment.vue';
import { useFavoriteStore } from '@/stores/favoriteStore';
import { useAttractionStore } from '@/stores/attractionStore';

const route = useRoute();
const router = useRouter();
const attractionDetailStore = useAttractionDetailStore();
const { attraction, loading } = storeToRefs(attractionDetailStore);
const map = ref(null);

const authStore = useAuthStore();
const newComment = ref('');

const favoriteStore = useFavoriteStore();

const initMap = () => {
  if (!window.kakao?.maps || !attraction.value) return;
  
  const container = document.getElementById('map');
  if (!container) return;
  
  const options = {
    center: new kakao.maps.LatLng(
      attraction.value.latitude || 37.5665,
      attraction.value.longitude || 126.9780
    ),
    level: 3
  };
  
  if (map.value) {
    map.value = null;
  }
  
  map.value = new kakao.maps.Map(container, options);
  
  const marker = new kakao.maps.Marker({
    position: new kakao.maps.LatLng(
      attraction.value.latitude,
      attraction.value.longitude
    ),
    map: map.value
  });
};

const loadKakaoMapScript = () => {
  if (window.kakao?.maps) {
    setTimeout(initMap, 0);
    return;
  }

  const script = document.createElement('script');
  script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${import.meta.env.VITE_KAKAO_MAP_SERVICE_KEY}`;
  script.onload = () => kakao.maps.load(() => setTimeout(initMap, 0));
  document.head.appendChild(script);
};

// loading이 false가 되고 attraction이 있을 때 지도 초기화
watch([loading, attraction], ([newLoading, newAttraction]) => {
  if (!newLoading && newAttraction) {
    loadKakaoMapScript();
  }
});

onMounted(async () => {
  // 상세 페이지 진입 시 현재 상태 저장
  
  const attractId = parseInt(route.params.id);
  await attractionDetailStore.fetchAttractionDetail(attractId);
  if (attraction.value?.isFavorite) {
    favoriteStore.setFavoriteStatus(attractId, true);
  }
});

const contentTypes = {
  12: "관광지",
  14: "문화시설",
  15: "축제공연행사",
  25: "여행코스",
  28: "레포츠",
  32: "숙박",
  38: "쇼핑",
  39: "음식점",
};

const toggleFavorite = async () => {
  if (!authStore.isLoggedIn) {
    alert("로그인이 필요한 서비스입니다.");
    return;
  }
  
  const attractId = parseInt(route.params.id);
  const prevIsLike = attraction.value.isLike;
  const originalHit = attraction.value.hit;
  
  try {
    await favoriteStore.toggleFavorite(attractId);
    
    // 좋아요 상태 업데이트
    attraction.value.isLike = favoriteStore.favoriteAttractions.has(attractId);
    
    // 이전 상태와 현재 상태를 비교하여 hit 수 업데이트
    if (!prevIsLike && attraction.value.isLike) {
      // 좋아요를 눌렀을 때
      attraction.value.hit = originalHit + 1;
    } else if (prevIsLike && !attraction.value.isLike) {
      // 좋아요를 취소했을 때
      attraction.value.hit = originalHit - 1;
    }
  } catch (error) {
    console.error('좋아요 토글 실패:', error);
    alert('좋아요 처리 중 오류가 발생했습니다.');
  }
};

const goBack = () => {
  router.go(-1);
};
</script>

<template>
  <v-container fluid class="page-container">
    <v-row justify="center">
      <v-col cols="12" class="content-wrapper">
        <div class="inner-content">
          <PageHeader 
            title="관광지 상세" 
            icon="mdi-map-marker-radius"
            :additional-icons="[
              'mdi-camera',
              'mdi-map-search',
              'mdi-compass'
            ]"
          />
          
          <div class="content-area">
            <v-row v-if="!loading">
              <v-col cols="12">
                <v-card>
                  <v-img
                    :src="attraction?.firstImage1"
                    height="400"
                    cover
                  >
                    <template v-slot:placeholder>
                      <v-row class="fill-height ma-0" align="center" justify="center">
                        <v-icon
                          icon="mdi-image-off"
                          color="grey-lighten-2"
                          size="64"
                        ></v-icon>
                      </v-row>
                    </template>
                  </v-img>
                  
                  <v-card-title class="text-h4 pa-4 d-flex align-center">
                    {{ attraction?.title }}
                    <v-chip class="ml-2" color="primary" size="small">
                      {{ contentTypes[attraction?.contentTypeId] }}
                    </v-chip>
                      <v-btn
                      icon
                      variant="text"
                      :color="attraction?.isLike ? 'red' : ''"
                      :loading="favoriteStore.loading"
                      @click="toggleFavorite"
                      class="ml-2"
                    >
                      <v-icon>
                        {{ attraction?.isLike ? 'mdi-heart' : 'mdi-heart-outline' }}
                      </v-icon>
                    </v-btn>
                  </v-card-title>

                  <v-card-text>
                    <v-row>
                      <v-col cols="12" md="8">
                        <div class="text-subtitle-1 mb-4">
                          <v-icon start>mdi-map-marker</v-icon>
                          {{ attraction?.addr1 }} {{ attraction?.addr2 }}
                        </div>
                        
                        <div v-if="attraction?.tel" class="text-subtitle-1 mb-4">
                          <v-icon start>mdi-phone</v-icon>
                          {{ attraction?.tel }}
                        </div>

                        <div
                         class="text-body-1 mb-4"
                        v-html="attraction?.overview || '상세 설명이 없습니다.' "
                        ></div>

                        <div class="d-flex gap-4">
                          <v-chip variant="outlined">
                            <v-icon start>mdi-eye</v-icon>
                            조회수 {{ attraction?.views }}
                          </v-chip>
                          <v-chip 
                            :color="attraction?.isLike ? 'red' : ''"
                            :variant="attraction?.isLike ? 'tonal' : 'outlined'"
                          >
                            <v-icon start>mdi-heart</v-icon>
                            좋아요 {{ attraction?.hit || 0 }}
                          </v-chip>
                        </div>
                      </v-col>
                      
                      <v-col cols="12" md="4">
                        <v-card variant="outlined">
                          <v-card-text>
                            <div id="map" style="width:100%;height:200px;"></div>
                          </v-card-text>
                        </v-card>
                      </v-col>
                    </v-row>
                  </v-card-text>
                </v-card>
              </v-col>

              <v-col cols="12" class="d-flex justify-end my-4">
                <v-btn
                  color="primary"
                  variant="tonal"
                  @click="goBack"
                  prepend-icon="mdi-arrow-left"
                >
                  뒤로가기
                </v-btn>
              </v-col>

              <v-col cols="12">
                <AttractComment 
                  :attraction-id="parseInt(route.params.id)"
                />
              </v-col>
            </v-row>

            <div v-else class="d-flex justify-center align-center" style="height: 400px">
              <v-progress-circular indeterminate color="primary"></v-progress-circular>
            </div>
          </div>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>

</style>