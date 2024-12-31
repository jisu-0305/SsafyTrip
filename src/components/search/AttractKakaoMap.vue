<script setup>
import { ref, watch, onMounted, onUnmounted } from "vue";
import { storeToRefs } from 'pinia';
import { useAttractionStore } from '@/stores/attractionStore';

const emit = defineEmits(['hover-marker', 'click-marker']);
const attractionStore = useAttractionStore();
const { mapPositions, attractions } = storeToRefs(attractionStore);

let map = null;
const markers = ref([]);
const infowindow = ref(null);
const selectedMarker = ref(null);
const isMapScriptLoaded = ref(false);

const loadKakaoMapScript = () => {
  return new Promise((resolve, reject) => {
    if (window.kakao && window.kakao.maps) {
      isMapScriptLoaded.value = true;
      resolve(window.kakao.maps);
      return;
    }

    const script = document.createElement('script');
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${import.meta.env.VITE_KAKAO_MAP_SERVICE_KEY}`;
    
    script.onload = () => {
      window.kakao.maps.load(() => {
        isMapScriptLoaded.value = true;
        resolve(window.kakao.maps);
      });
    };
    
    script.onerror = (error) => {
      reject(new Error('카카오맵 스크립트 로딩 실패'));
    };

    document.head.appendChild(script);
  });
};

const initMap = async () => {
  try {
    await loadKakaoMapScript();
    const container = document.getElementById('map');
    if (!container) return;
    
    const options = {
      center: new kakao.maps.LatLng(36.3504119, 127.3845475),
      level: 13,
    };
    
    map = new kakao.maps.Map(container, options);
    infowindow.value = new kakao.maps.InfoWindow({ zIndex: 1 });
    
    // 카카오맵 초기화 후 마커 위치 재설정
    attractionStore.setMapPositions();
    
    if (mapPositions.value?.length) {
      loadMarkers();
    }
  } catch (error) {
    console.error('맵 초기화 실패:', error);
  }
};

const loadMarkers = () => {
  if (!map || !mapPositions.value?.length) return;

  // 기존 마커들 제거
  markers.value.forEach(marker => marker.setMap(null));
  markers.value = [];

  mapPositions.value.forEach((position, index) => {
    const marker = new kakao.maps.Marker({
      map: map,
      position: position.latlng,
      title: position.title,
    });

    kakao.maps.event.addListener(marker, 'mouseover', () => {
      emit('hover-marker', attractions.value[index].contentId);
      if (marker !== selectedMarker.value) {
        marker.setImage(new kakao.maps.MarkerImage(
          '//t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png',
          new kakao.maps.Size(24, 35)
        ));
      }
    });

    kakao.maps.event.addListener(marker, 'mouseout', () => {
      emit('hover-marker', null);
      if (marker !== selectedMarker.value) {
        marker.setImage(null);
      }
    });

    kakao.maps.event.addListener(marker, 'click', () => {
      if (selectedMarker.value) {
        selectedMarker.value.setImage(null);
      }
      marker.setImage(new kakao.maps.MarkerImage(
        '//t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png',
        new kakao.maps.Size(24, 35)
      ));
      selectedMarker.value = marker;
      emit('click-marker', attractions.value[index].contentId);
      
      const content = `
        <div class="custom-overlay">
          <h3>${attractions.value[index].title}</h3>
          <p>조회수: ${attractions.value[index].views}</p>
        </div>
      `;

      infowindow.value.setContent(content);
      infowindow.value.open(map, marker);
    });

    markers.value.push(marker);
  });

  // 모든 마커가 보이도록 지도 영역 재설정
  const bounds = new kakao.maps.LatLngBounds();
  mapPositions.value.forEach(position => bounds.extend(position.latlng));
  map.setBounds(bounds);
};

watch(mapPositions, (newPositions) => {
  if (newPositions?.length && map) {
    loadMarkers();
  }
}, { deep: true });

onMounted(async () => {
  await initMap();
});

onUnmounted(() => {
  // 컴포넌트 언마운트 시 정리
  if (markers.value.length) {
    markers.value.forEach(marker => marker.setMap(null));
    markers.value = [];
  }
  if (map) {
    map = null;
  }
  if (infowindow.value) {
    infowindow.value.close();
    infowindow.value = null;
  }
});
</script>

<template>
  <div id="map"></div>
</template>

<style scoped>
#map {
  width: 100%;
  height: 850px;
  position: relative;
}

:deep(.custom-overlay) {
  padding: 10px;
  background: white;
  border-radius: 4px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.3);
}

:deep(.custom-overlay h3) {
  margin: 0 0 5px 0;
  font-size: 16px;
}

:deep(.custom-overlay p) {
  margin: 0;
  font-size: 14px;
}
</style>
