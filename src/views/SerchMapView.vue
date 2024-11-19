<script setup>
import AttrationKakakMap from "@/components/search/AttrationKakakMap.vue";
import SquareBox from "@/components/search/SquareBox .vue";
import { ref } from "vue";

// 더미 데이터
const attractions = ref([
  {
    title: "가회동성당",
    latlng: new kakao.maps.LatLng(37.5820858828, 126.9846616856),
    img: "http://tong.visitkorea.or.kr/cms/resource/09/3303909_image2_1.jpg",
    loc: "서울특별시 종로구 율곡로23길 16",
  },
  {
    title: "간데메공원",
    latlng: new kakao.maps.LatLng(37.5728520032, 127.0490977427),
    img: "http://tong.visitkorea.or.kr/cms/resource/11/2779111_image2_1.png",
    loc: "서울특별시 강남구 역삼동",
  },
  {
    title: "갈산근린공원",
    latlng: new kakao.maps.LatLng(37.5061176314, 126.8684105358),
    img: "http://tong.visitkorea.or.kr/cms/resource/62/2612062_image2_1.bmp",
    loc: "서울특별시 강남구 영동대로 513",
  },
  {
    title: "감로암",
    latlng: new kakao.maps.LatLng(37.5753148419, 127.0066015446),
    img: "http://tong.visitkorea.or.kr/cms/resource/85/2031885_image2_1.jpg",
    loc: "서울특별시 강남구 압구정로 161",
  },
]);
</script>

<template>
  <div class="container">
    <div style="height: 90px"></div>
    <div class="col-md-12">
      <h1 class="mt-1 text-center">전국 관광지 정보</h1>
      <!-- 관광지 검색 start -->
      <form @submit.prevent="search" class="row mb-4">
        <input type="hidden" name="action" value="list" />
        <div class="col-3">
          <select class="form-select" style="height: 60px; font-size: 20px">
            <option value="전체" selected>시도</option>
            <option value="서울">서울</option>
            <option value="인천">인천</option>
            <option value="대전">대전</option>
            <option value="대구">대구</option>
            <option value="광주">광주</option>
            <option value="부산">부산</option>
          </select>
        </div>
        <div class="col-3">
          <select class="form-select" style="height: 60px; font-size: 20px">
            <option value="전체" selected>구군</option>
            <option value="서울">서울</option>
            <option value="인천">인천</option>
            <option value="대전">대전</option>
            <option value="대구">대구</option>
            <option value="광주">광주</option>
            <option value="부산">부산</option>
          </select>
        </div>
        <div class="col-3">
          <select class="form-select" style="height: 60px; font-size: 20px">
            <option value="전체" selected>관광지 유형</option>
            <option value="관광지">관광지</option>
            <option value="문화시설">문화시설</option>
            <option value="축제공연행사">축제공연행사</option>
            <option value="여행코스">여행코스</option>
            <option value="레포츠">레포츠</option>
            <option value="숙박">숙박</option>
            <option value="쇼핑">쇼핑</option>
            <option value="음식점">음식점</option>
          </select>
        </div>
        <div class="col-md-2">
          <input
            type="text"
            class="form-control"
            placeholder="검색어를 입력하세요"
            style="height: 60px; font-size: 20px"
          />
        </div>

        <div class="col-md-1 d-flex align-items-end">
          <button type="submit" class="btn btn-primary w-100">검색</button>
        </div>
      </form>

      <!-- props를 통해 위도, 경도, title 값을 가진 객체 배열 넘겨주기 -->
      <AttrationKakakMap :attractions="attractions"></AttrationKakakMap>

      <div class="square-container">
        <SquareBox
          v-for="(attraction, index) in attractions"
          :key="index"
          :title="attraction.title"
          :latlng="attraction.latlng"
          :img="attraction.img"
          :loc="attraction.loc"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.square-container {
  display: flex;
  justify-content: flex-start; /* 수평 정렬 */
  flex-wrap: nowrap; /* 줄바꿈 없이 한 줄로 정렬 */
}
</style>
