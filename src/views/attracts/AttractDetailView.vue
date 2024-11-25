<script setup>
import { onMounted, ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import { storeToRefs } from 'pinia';
import { useAttractionDetailStore } from '@/stores/attractionDetailStore';
import PageHeader from "@/components/common/PageHeader.vue";

const route = useRoute();
const attractionDetailStore = useAttractionDetailStore();
const { attraction, comments, loading } = storeToRefs(attractionDetailStore);
const map = ref(null);

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
  const attractId = parseInt(route.params.id);
  await attractionDetailStore.fetchAttractionDetail(attractId);
});

const contentTypes = {
  12: '관광지',
  14: '문화시설',
  15: '축제공연행사',
  25: '여행코스',
  28: '레포츠',
  32: '숙박',
  38: '쇼핑'
};
</script>

<template>
  <v-container fluid class="page-container">
    <v-row justify="center">
      <v-col cols="12" class="content-wrapper">
        <div class="inner-content">
          <PageHeader title="관광지 상세" icon="mdi-map-marker-detail" />
          
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
                  
                  <v-card-title class="text-h4 pa-4">
                    {{ attraction?.title }}
                    <v-chip class="ml-2" color="primary" size="small">
                      {{ contentTypes[attraction?.contentTypeId] }}
                    </v-chip>
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

                        <div class="text-body-1 mb-4">
                          {{ attraction?.overview || '상세 설명이 없습니다.' }}
                        </div>

                        <div class="d-flex gap-4">
                          <v-chip variant="outlined">
                            <v-icon start>mdi-eye</v-icon>
                            조회수 {{ attraction?.views }}
                          </v-chip>
                          <v-chip variant="outlined">
                            <v-icon start>mdi-thumb-up</v-icon>
                            추천수 {{ attraction?.hit }}
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

              <v-col cols="12" class="mt-4">
                <v-card>
                  <v-card-title class="d-flex align-center">
                    댓글 
                    <v-chip class="ml-2" color="primary">{{ comments?.length || 0 }}</v-chip>
                  </v-card-title>
                  
                  <v-divider></v-divider>
                  
                  <v-list v-if="comments?.length">
                    <v-list-item
                      v-for="comment in comments"
                      :key="comment.id"
                      class="py-4"
                    >
                      <template v-slot:prepend>
                        <v-avatar color="primary">
                          {{ comment.email.charAt(0).toUpperCase() }}
                        </v-avatar>
                      </template>
                      
                      <v-list-item-title class="font-weight-bold mb-1">
                        {{ comment.email }}
                      </v-list-item-title>
                      <v-list-item-subtitle class="text-body-1 mb-1">
                        {{ comment.content }}
                      </v-list-item-subtitle>
                      <v-list-item-subtitle class="text-caption">
                        {{ new Date(comment.createdAt).toLocaleString() }}
                      </v-list-item-subtitle>
                    </v-list-item>
                  </v-list>
                  
                  <v-card-text v-else class="text-center py-4">
                    작성된 댓글이 없습니다.
                  </v-card-text>
                </v-card>
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