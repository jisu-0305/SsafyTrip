<script setup>
import { ref, watch, onMounted, defineProps } from "vue";

var map;
const positions = ref([
  {
    title: "카카오",
    latlng: new kakao.maps.LatLng(33.450705, 126.570677),
  },
  {
    title: "생태연못",
    latlng: new kakao.maps.LatLng(33.450936, 126.569477),
  },
  {
    title: "텃밭",
    latlng: new kakao.maps.LatLng(33.450879, 126.56994),
  },
  {
    title: "근린공원",
    latlng: new kakao.maps.LatLng(33.451393, 126.570738),
  },
]);

// Proxy랑 RefImpl이랑 어떻게 다른거지?
// Props로 넘겨 받으면 RefImpl이 Proxy객체로 되네
const props = defineProps(["attractions"]); // props 객체로 저장
console.log(props.attractions); // props를 통해 접근
console.log(positions.value);
const positionImpl = ref(props.attractions);
console.log(positionImpl);

const markers = ref([]);

const initMap = () => {
  const container = document.getElementById("map");
  const options = {
    center: new kakao.maps.LatLng(33.450701, 126.570667),
    level: 3,
  };
  map = new kakao.maps.Map(container, options);

  loadMarkers();
};

onMounted(() => {
  if (window.kakao && window.kakao.maps) {
    initMap();
  } else {
    const script = document.createElement("script");
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${
      import.meta.env.VITE_KAKAO_MAP_SERVICE_KEY
    }&libraries=services,clusterer`;
    script.onload = () => kakao.maps.load(() => initMap());
    document.head.appendChild(script);
  }
});

const deleteMarkers = () => {
  if (markers.value.length > 0) {
    markers.value.forEach((marker) => marker.setMap(null));
  }
};

const loadMarkers = () => {
  deleteMarkers();
  markers.value = [];

  positionImpl.value.forEach((position) => {
    console.log(position.title);

    const marker = new kakao.maps.Marker({
      map: map,
      position: position.latlng,
      title: position.title,
      clickable: true,
    });
    markers.value.push(marker);
  });

  const bounds = positionImpl.value.reduce(
    (bounds, position) => bounds.extend(position.latlng),
    new kakao.maps.LatLngBounds()
  );

  map.setBounds(bounds);
};
</script>

<template>
  <div id="map"></div>
</template>

<style>
#map {
  width: 100%;
  height: 500px;
}
</style>
