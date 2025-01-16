<template>
    <div id="map-container">
        <div id="map"></div>
    </div>
</template>

<script setup>
import { onMounted, watch, ref } from 'vue';
import { usePlanStore } from '@/stores/planStores';

const planStore = usePlanStore();
const map = ref(null);
const markers = ref(new Map());
const infowindows = ref(new Map());

const initMap = () => {
  const container = document.getElementById('map');
  const options = {
    center: new kakao.maps.LatLng(36.5, 127.5),
    level: 13
  };
  map.value = new kakao.maps.Map(container, options);
  updateMarkers();
};

const updateMarkers = () => {
  // 기존 마커와 인포윈도우 제거
  markers.value.forEach(marker => marker.setMap(null));
  infowindows.value.forEach(infowindow => infowindow.close());
  markers.value.clear();
  infowindows.value.clear();

  // 새 마커 생성
  planStore.markers.forEach(markerData => {
    const position = new kakao.maps.LatLng(
      markerData.latitude,
      markerData.longitude
    );

    // 마커 생성
    const marker = new kakao.maps.Marker({
      position: position,
      map: map.value
    });

    // 인포윈도우 생성
    const content = `
      <div class="marker-info" style="padding: 5px;">
        <div style="color: ${markerData.color}; font-weight: bold;">Day ${markerData.day}</div>
        <div>${markerData.title}</div>
      </div>
    `;

    const infowindow = new kakao.maps.InfoWindow({
      content: content,
      removable: true
    });

    // 마커 클릭 이벤트
    kakao.maps.event.addListener(marker, 'click', () => {
      infowindows.value.forEach(iw => iw.close());
      infowindow.open(map.value, marker);
    });

    // 마커와 인포윈도우 저장
    markers.value.set(markerData.day, marker);
    infowindows.value.set(markerData.day, infowindow);
  });

  // 지도 범위 재설정
  if (markers.value.size > 0) {
    const bounds = new kakao.maps.LatLngBounds();
    markers.value.forEach(marker => {
      bounds.extend(marker.getPosition());
    });
    map.value.setBounds(bounds);
  }
};

// 마커 데이터 변경 감지
watch(() => planStore.markers, updateMarkers, { deep: true });

onMounted(() => {
  if (window.kakao && window.kakao.maps) {
    initMap();
  } else {
    const script = document.createElement('script');
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${import.meta.env.VITE_KAKAO_MAP_SERVICE_KEY}`;
    script.onload = () => kakao.maps.load(initMap);
    document.head.appendChild(script);
  }
});
</script>

<style scoped>
#map-container {
    width: 100%;
    margin: 0 auto;
}

#map {
    width: 100%;
    height: 500px;
    border-radius: 8px;
}

:deep(.marker-info) {
    background: white;
    border-radius: 4px;
    box-shadow: 0 2px 6px rgba(0,0,0,0.1);
}
</style>
