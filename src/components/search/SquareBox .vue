<script setup>
import logoSrc from "@/assets/ssafy_logo.png"; // 'ssafy_logo.png'를 import

defineProps({
  title: String,
  latlng: Object, // kakao.maps.LatLng 객체
  img: String,
  loc: String,
});
</script>

<!-- <template>
  <div class="square-box">
    <h3>{{ title }}</h3>
    <img :src="img" alt="SSAFY Logo" style="width: 280px" />
    <p>위도: {{ loc }}</p>
    <button>상세보기</button>
  </div>
</template> -->

<!-- <style scoped>
.square-box {
  border: 1px solid #ccc;
  padding: 10px;
  margin: 10px;
  border-radius: 5px;
  background-color: #f9f9f9;
  width: 310px;
}
</style> -->
<template>
  <div class="square-box">
    <h3>{{ title }}</h3>
    <!-- 이미지 크기 통일: 280px로 설정 -->
    <img :src="img" alt="Attraction Image" class="attraction-img" />
    <!-- 위도 정보 표시 -->
    <p class="location">위도: {{ loc }}</p>
    <!-- 가운데 정렬된 버튼 -->
    <button class="view-detail-btn">상세보기</button>
  </div>
</template>

<style scoped>
.square-box {
  display: flex;
  flex-direction: column;
  align-items: center; /* 내용 중앙 정렬 */
  text-align: center; /* 텍스트 중앙 정렬 */
  width: 300px;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.attraction-img {
  width: 280px; /* 이미지 크기 통일 */
  height: 180px; /* 높이도 고정하여 비율을 맞춤 */
  object-fit: cover; /* 비율에 맞게 이미지 자르기 */
  border-radius: 8px;
  margin-bottom: 15px;
}

.location {
  margin: 10px 0;
  font-size: 14px;
  color: #555;
}

.view-detail-btn {
  margin-top: auto; /* 버튼을 아래로 배치 */
  padding: 10px 20px;
  font-size: 16px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.view-detail-btn:hover {
  background-color: #0056b3;
}
</style>
