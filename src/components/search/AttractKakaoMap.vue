<script setup>
import { ref, watch, onMounted } from "vue";
import { storeToRefs } from 'pinia';
import { useAttractionStore } from '@/stores/attractionStore';

const emit = defineEmits(['hover-marker', 'click-marker']);
const attractionStore = useAttractionStore();
const { mapPositions, attractions } = storeToRefs(attractionStore);

let map;
const markers = ref([]);
const infowindow = ref(null);
const selectedMarker = ref(null);

const initMap = () => {
  const container = document.getElementById("map");
  const options = {
    center: new kakao.maps.LatLng(36.3504119, 127.3845475),
    level: 13,
  };
  map = new kakao.maps.Map(container, options);
  infowindow.value = new kakao.maps.InfoWindow({ zIndex: 1 });
};

const loadMarkers = () => {
  markers.value.forEach(marker => marker.setMap(null));
  markers.value = [];

  if (!mapPositions.value?.length) return;

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

  const bounds = new kakao.maps.LatLngBounds();
  mapPositions.value.forEach(position => bounds.extend(position.latlng));
  map.setBounds(bounds);
};

watch(mapPositions, loadMarkers, { deep: true });

onMounted(() => {
  if (window.kakao?.maps) {
    initMap();
  } else {
    const script = document.createElement("script");
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${
      import.meta.env.VITE_KAKAO_MAP_SERVICE_KEY
    }`;
    script.onload = () => kakao.maps.load(initMap);
    document.head.appendChild(script);
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
