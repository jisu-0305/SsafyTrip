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

    // 커스텀 오버레이 생성
    const content = `
      <div class="wrap">
        <div class="info">
          <div class="title">
            ${attractions.value[index].title}
            <div class="close" title="닫기" onclick="this.parentElement.parentElement.parentElement.style.display='none'"></div>
          </div>
          <div class="body">
            <div class="desc">
              <div class="ellipsis">${attractions.value[index].addr1 || ''}</div>
              <div class="info-stats">
                <span>조회수: ${attractions.value[index].views}</span>
                <span>좋아요: ${attractions.value[index].hit}</span>
              </div>
            </div>
          </div>
        </div>
      </div>`;

    const overlay = new kakao.maps.CustomOverlay({
      content: content,
      position: position.latlng,
      xAnchor: 0.5,
      yAnchor: 1.3,
      zIndex: 3
    });

    // 마커 이벤트 리스너
    kakao.maps.event.addListener(marker, 'mouseover', () => {
      emit('hover-marker', attractions.value[index].contentId);
      overlay.setMap(map);
    });

    kakao.maps.event.addListener(marker, 'mouseout', () => {
      emit('hover-marker', null);
      overlay.setMap(null);
    });

    kakao.maps.event.addListener(marker, 'click', () => {
      emit('click-marker', attractions.value[index].contentId);
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
}

:deep(.wrap) {
  position: absolute;
  left: 0;
  bottom: 40px;
  width: 288px;
  height: auto;
  margin-left: -144px;
  text-align: left;
  font-size: 12px;
  font-family: 'Malgun Gothic', dotum, '돋움', sans-serif;
  line-height: 1.5;
}

:deep(.wrap .info) {
  width: 286px;
  height: auto;
  border-radius: 5px;
  border-bottom: 2px solid #ccc;
  border-right: 1px solid #ccc;
  overflow: hidden;
  background: #fff;
  box-shadow: 0px 1px 2px #888;
}

:deep(.info .title) {
  padding: 5px 0 0 10px;
  height: 30px;
  background: #eee;
  border-bottom: 1px solid #ddd;
  font-size: 14px;
  font-weight: bold;
  position: relative;
}

:deep(.info .close) {
  position: absolute;
  top: 7px;
  right: 10px;
  width: 17px;
  height: 17px;
  background: url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/overlay_close.png');
  cursor: pointer;
}

:deep(.info .body) {
  position: relative;
  overflow: hidden;
  padding: 15px;
}

:deep(.info .desc) {
  position: relative;
}

:deep(.desc .ellipsis) {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 5px;
}

:deep(.info-stats) {
  margin-top: 5px;
  color: #666;
}

:deep(.info-stats span) {
  margin-right: 10px;
}

:deep(.info:after) {
  content: '';
  position: absolute;
  margin-left: -12px;
  left: 50%;
  bottom: -12px;
  width: 22px;
  height: 12px;
  background: url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/vertex_white.png');
}
</style>
