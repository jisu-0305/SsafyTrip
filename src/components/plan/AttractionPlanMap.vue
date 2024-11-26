<template>
    <div id="map-container">
        <div id="map"></div>
    </div>
</template>

<script setup>
import { onMounted } from 'vue';

// Kakao Map 객체 선언
let map;

// 지도 초기화 함수
const initMap = () => {
    const container = document.getElementById('map'); // 지도 컨테이너
    const options = {
        center: new kakao.maps.LatLng(37.5665, 126.9780), // 서울 중심 좌표
        level: 3, // 확대 수준
    };
    map = new kakao.maps.Map(container, options); // 지도 생성
};

// 컴포넌트가 마운트된 후 Kakao Map SDK 로드 및 초기화
onMounted(() => {
    if (window.kakao && window.kakao.maps) {
        initMap();
    } else {
        const script = document.createElement('script');
        script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${import.meta.env.VITE_KAKAO_MAP_SERVICE_KEY}&libraries=services,clusterer`;
        script.onload = () => kakao.maps.load(() => initMap());
        document.head.appendChild(script);
    }
});
</script>

<style scoped>
#map-container {
    width: 50%;
    /* 화면의 절반 너비 */
    margin: 0 auto;
    /* 가운데 정렬 */
}

#map {
    width: 100%;
    height: 300px;
    /* 고정된 높이 */
}
</style>
