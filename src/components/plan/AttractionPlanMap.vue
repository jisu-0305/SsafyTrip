<template>
    <div id="map-container">
        <div id="map"></div>
    </div>
</template>

<script setup>
import { onMounted } from 'vue';

let map;

const initMap = () => {
    const container = document.getElementById('map');
    const options = {
        center: new kakao.maps.LatLng(37.5665, 126.9780), // 서울 한복판
        level: 3,
    };
    map = new kakao.maps.Map(container, options);
};

onMounted(() => {
    if (window.kakao && window.kakao.maps) {
        initMap();
    } else {
        const script = document.createElement('script');
        script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${import.meta.env.VITE_KAKAO_MAP_SERVICE_KEY
            }&libraries=services,clusterer`;
        script.onload = () => kakao.maps.load(() => initMap());
        document.head.appendChild(script);
    }
});
</script>

<style scoped>
#map-container {
    width: 50%;
    /* 전체 화면의 60% 사용 */
    margin: 0 auto;
    /* 좌우축 여백을 균등하게 */
}

#map {
    width: 100%;
    height: 300px;
}
</style>