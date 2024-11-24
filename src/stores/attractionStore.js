import { defineStore } from 'pinia';
import { ref } from 'vue';
import { listAttractions, getInitialAttractions } from '@/api/attractApi';

export const useAttractionStore = defineStore('attraction', () => {
  const attractions = ref([]);
  const totalPages = ref(0);
  const totalCount = ref(0);
  const currentPage = ref(1);
  const loading = ref(false);
  const mapPositions = ref([]);
  const sidoList = ref([]);
  const contentTypeList = ref([]);

  const fetchAttractions = async (params) => {
    try {
      loading.value = true;
      const response = await listAttractions({
        page: currentPage.value,
        size: 5,
        ...params
      });
      
      attractions.value = response.data.attractionList || [];
      totalPages.value = response.data.totalPages || 0;
      totalCount.value = response.data.totalCount || 0;
      
      if (attractions.value.length > 0) {
        mapPositions.value = attractions.value.map(attraction => ({
          title: attraction.title,
          latlng: new kakao.maps.LatLng(
            attraction.latitude || 37.5665,
            attraction.longitude || 126.9780
          )
        }));
      } else {
        mapPositions.value = [];
      }
    } catch (error) {
      console.error('관광지 목록 조회 실패:', error);
      attractions.value = [];
      mapPositions.value = [];
      totalPages.value = 0;
      totalCount.value = 0;
    } finally {
      loading.value = false;
    }
  };

  const fetchInitialAttractions = async () => {
    try {
      loading.value = true;
      const response = await getInitialAttractions();
      
      // 시도 목록과 컨텐츠 타입 목록 저장
      sidoList.value = response.data.sidoList || [];
      contentTypeList.value = response.data.contentTypeList || [];
      
      attractions.value = response.data.attractList || [];
      totalPages.value = response.data.totalPages || 0;
      totalCount.value = response.data.totalCount || 0;
      
      if (attractions.value.length > 0) {
        mapPositions.value = attractions.value.map(attraction => ({
          title: attraction.title,
          latlng: new kakao.maps.LatLng(
            attraction.latitude || 37.5665,
            attraction.longitude || 126.9780
          )
        }));
      }
    } catch (error) {
      console.error('초기 관광지 목록 조회 실패:', error);
      attractions.value = [];
      mapPositions.value = [];
      totalPages.value = 0;
      totalCount.value = 0;
    } finally {
      loading.value = false;
    }
  };

  return {
    attractions,
    totalPages,
    totalCount,
    currentPage,
    loading,
    mapPositions,
    sidoList,
    contentTypeList,
    fetchAttractions,
    fetchInitialAttractions
  };
}); 