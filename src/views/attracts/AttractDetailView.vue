<script setup>
import { onMounted, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { storeToRefs } from "pinia";
import { useAttractionDetailStore } from "@/stores/attractionDetailStore";
import PageHeader from "@/components/common/PageHeader.vue";
import { useAuthStore } from "@/stores/authStores";
import axios from "axios";
import AttractComment from "@/components/attract/AttractComment.vue";
import { useFavoriteStore } from "@/stores/favoriteStore";
import { useAttractionStore } from "@/stores/attractionStore";

const route = useRoute();
const router = useRouter();
const attractionDetailStore = useAttractionDetailStore();
const { attraction, comments, loading } = storeToRefs(attractionDetailStore);
const map = ref(null);

const authStore = useAuthStore();
const newComment = ref("");

const favoriteStore = useFavoriteStore();
const { favoriteAttractions } = storeToRefs(favoriteStore);

const attractionStore = useAttractionStore();

const initMap = () => {
  if (!window.kakao?.maps || !attraction.value) return;

  const container = document.getElementById("map");
  if (!container) return;

  const options = {
    center: new kakao.maps.LatLng(
      attraction.value.latitude || 37.5665,
      attraction.value.longitude || 126.978
    ),
    level: 3,
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
    map: map.value,
  });
};

const loadKakaoMapScript = () => {
  if (window.kakao?.maps) {
    setTimeout(initMap, 0);
    return;
  }

  const script = document.createElement("script");
  script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${
    import.meta.env.VITE_KAKAO_MAP_SERVICE_KEY
  }`;
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
  attractionStore.saveState();

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

const submitComment = async () => {
  if (!newComment.value.trim()) return;

  try {
    await axios.post("/api/comments", {
      attractionId: parseInt(route.params.id),
      content: newComment.value,
    });

    // 댓글 작성 후 목록 새로고침
    await attractionDetailStore.fetchAttractionDetail(
      parseInt(route.params.id)
    );
    newComment.value = ""; // 입력창 초기화
  } catch (error) {
    console.error("댓글 작성 실패:", error);
    alert("댓글 작성에 실패했습니다.");
  }
};

const toggleFavorite = async () => {
  if (!authStore.isLoggedIn) {
    alert("로그인이 필요한 서비스입니다.");
    return;
  }
  await favoriteStore.toggleFavorite(parseInt(route.params.id));

  if (attraction.value.isLike == false) {
    attraction.value.isLike = true;
  } else {
    attraction.value.isLike = false;
  }
};

const goBack = async () => {
  const success = attractionStore.restoreState();
  if (success) {
    // 상태 복원 후 API 호출하여 데이터 동기화
    await attractionStore.fetchAttractions(attractionStore.searchParams, false);
    router.go(-1);
  } else {
    router.push("/search");
  }
};
console.log("detailView");
if (attraction.value) {
  console.log(attraction.value.isLike);
} else {
  console.log(
    "attraction 데이터가 아직 로드되지 않았습니다. from AttractionDetailView"
  );
}
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
                  <v-img :src="attraction?.firstImage1" height="400" cover>
                    <template v-slot:placeholder>
                      <v-row
                        class="fill-height ma-0"
                        align="center"
                        justify="center"
                      >
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
                      :color="
                        favoriteAttractions.has(parseInt(route.params.id))
                        //attraction.isLike 
                        ? 'red' : ''
                      "
                      :loading="favoriteStore.loading"
                      @click="toggleFavorite"
                      class="ml-2"
                    >
                      <v-icon>
                        {{
                          favoriteAttractions.has(parseInt(route.params.id))
                          //attraction.isLike 
                          ? "mdi-heart" : "mdi-heart-outline"
                        }}
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

                        <div
                          v-if="attraction?.tel"
                          class="text-subtitle-1 mb-4"
                        >
                          <v-icon start>mdi-phone</v-icon>
                          {{ attraction?.tel }}
                        </div>

                        <!-- <div class="text-body-1 mb-4">
                          {{ attraction?.overview || "상세 설명이 없습니다." }}
                        </div> -->

                        <div class="text-body-1 mb-4" v-html="attraction?.overview?.replace(/\n/g, '<br>') || '상세 설명이 없습니다.'">
                        </div>

                        <div class="d-flex gap-4">
                          <v-chip variant="outlined">
                            <v-icon start>mdi-eye</v-icon>
                            조회수 {{ attraction?.views }}
                          </v-chip>
                          <v-chip
                            :color="
                              favoriteAttractions.has(parseInt(route.params.id))
                                ? 'red'
                                : ''
                            "
                            :variant="
                              favoriteAttractions.has(parseInt(route.params.id))
                                ? 'tonal'
                                : 'outlined'
                            "
                          >
                            <v-icon start>mdi-heart</v-icon>
                            좋아요 {{ attraction?.hit }}
                          </v-chip>
                        </div>
                      </v-col>

                      <v-col cols="12" md="4">
                        <v-card variant="outlined">
                          <v-card-text>
                            <div
                              id="map"
                              style="width: 100%; height: 200px"
                            ></div>
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
                <AttractComment :attraction-id="parseInt(route.params.id)" />
              </v-col>
            </v-row>

            <div
              v-else
              class="d-flex justify-center align-center"
              style="height: 400px"
            >
              <v-progress-circular
                indeterminate
                color="primary"
              ></v-progress-circular>
            </div>
          </div>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped></style>
