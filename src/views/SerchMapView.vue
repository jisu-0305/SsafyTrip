<script setup>
import { ref, computed } from "vue";
import AttrationKakakMap from "@/components/search/AttrationKakakMap.vue";

// 더미 데이터 
const attractions = ref([
  {
    title: "가회동성당",
    latlng: new kakao.maps.LatLng(37.5820858828, 126.9846616856), 
    img: "http://tong.visitkorea.or.kr/cms/resource/09/3303909_image2_1.jpg",
    loc: "서울특별시 종로구 율곡로23길 16",
  },
  // ... 더미데이터
]);

// 필터 상태
const selectedCity = ref(null);
const selectedDistrict = ref(null);
const selectedType = ref(null);
const searchKeyword = ref("");

// 페이징 관련 상태
const currentPage = ref(1);
const itemsPerPage = 5;
const totalPages = ref(10); // 백엔드에서 받아올 값

// 도시 목록
const cities = [
  { title: '서울', value: 'seoul' },
  { title: '인천', value: 'incheon' },
  { title: '대전', value: 'daejeon' },
  { title: '대구', value: 'daegu' },
  { title: '광주', value: 'gwangju' },
  { title: '부산', value: 'busan' },
];

// 관광지 유형
const types = [
  { title: '관광지', value: 'spot' },
  { title: '문화시설', value: 'culture' },
  { title: '축제공연행사', value: 'festival' },
  { title: '여행코스', value: 'course' },
  { title: '레포츠', value: 'leisure' },
  { title: '숙박', value: 'accommodation' },
  { title: '쇼핑', value: 'shopping' },
  { title: '음식점', value: 'restaurant' },
];

const search = () => {
  // 검색 로직 구현
  console.log('검색:', {
    city: selectedCity.value,
    district: selectedDistrict.value,
    type: selectedType.value,
    keyword: searchKeyword.value
  });
};

const handlePageChange = (page) => {
  currentPage.value = page;
  // 백엔드에서 해당 페이지 데이터 요청
};
</script>

<template>
  <v-container fluid class="fill-height">
    <v-row>
      <!-- 필터 영역 -->
      <v-col cols="12">
        <v-card class="mb-4">
          <v-card-text>
            <v-row>
              <v-col cols="12" md="3">
                <v-select
                  v-model="selectedCity"
                  :items="cities"
                  item-title="title"
                  item-value="value"
                  label="시도"
                  variant="outlined"
                  density="comfortable"
                ></v-select>
              </v-col>

              <v-col cols="12" md="3">
                <v-select
                  v-model="selectedDistrict"
                  :items="[]"
                  label="구군"
                  variant="outlined"
                  density="comfortable"
                ></v-select>
              </v-col>

              <v-col cols="12" md="3">
                <v-select
                  v-model="selectedType"
                  :items="types"
                  item-title="title"
                  item-value="value"
                  label="관광지 유형"
                  variant="outlined"
                  density="comfortable"
                ></v-select>
              </v-col>

              <v-col cols="12" md="2">
                <v-text-field
                  v-model="searchKeyword"
                  label="검색어"
                  variant="outlined"
                  density="comfortable"
                ></v-text-field>
              </v-col>

              <v-col cols="12" md="1">
                <v-btn 
                  color="primary" 
                  block 
                  @click="search"
                  height="56"
                >
                  검색
                </v-btn>
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>
      </v-col>

      <!-- 지도 영역 -->
      <v-col cols="12" md="6">
        <v-card height="700">
          <AttrationKakakMap :attractions="attractions" />
        </v-card>
      </v-col>

      <!-- 리스트 영역 -->
      <v-col cols="12" md="6">
        <v-card>
          <v-list lines="two">
            <v-list-item
              v-for="attraction in attractions"
              :key="attraction.title"
              :title="attraction.title"
              :subtitle="attraction.loc"
            >
              <template v-slot:prepend>
                <v-avatar size="100">
                  <v-img :src="attraction.img" cover></v-img>
                </v-avatar>
              </template>
            </v-list-item>
          </v-list>

          <!-- 페이지네이션 -->
          <v-card-actions class="justify-center">
            <v-pagination
              v-model="currentPage"
              :length="totalPages"
              :total-visible="7"
              @update:model-value="handlePageChange"
            ></v-pagination>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.v-container {
  max-width: none;
}
</style>