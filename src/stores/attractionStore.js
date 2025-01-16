import { defineStore } from 'pinia';
import { ref } from 'vue';
import { listAttractions, getInitialAttractions, getSidoList } from '@/api/attractApi';
import { useFavoriteStore } from '@/stores/favoriteStore';

export const useAttractionStore = defineStore('attraction', () => {
  const attractions = ref([]);
  const totalPages = ref(0);
  const totalCount = ref(0);
  const currentPage = ref(1);
  const loading = ref(false);
  const mapPositions = ref([]);
  const sidoList = ref([]);
  const contentTypeList = ref([]);
  const searchParams = ref({
    sidoCode: null,
    gugunCode: null,
    contentTypeId: null,
    keyword: '',
    sortBy: 'name'
  });

  const setMapPositions = () => {
    if (!window.kakao?.maps || !attractions.value.length) return;
    
    mapPositions.value = attractions.value.map(attraction => ({
      title: attraction.title,
      latlng: new kakao.maps.LatLng(
        attraction.latitude || 37.5665,
        attraction.longitude || 126.9780
      )
    }));
  };

  const fetchAttractions = async (params = {}, resetPage = false) => {
    try {
      loading.value = true;
      
      if (resetPage) {
        currentPage.value = 1;
        params = {
          ...params,
          page: 1
        };
      } else {
        params = {
          ...params,
          page: params.page || currentPage.value
        };
      }
      
      const mergedParams = {
        ...searchParams.value,
        ...params
      };
      
      searchParams.value = mergedParams;
      
      const response = await listAttractions(mergedParams);
      
      attractions.value = response.data.attractionList || [];
      totalPages.value = response.data.totalPages || 0;
      totalCount.value = response.data.totalCount || 0;
      
      setMapPositions();
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
      currentPage.value = 1;
      const response = await getInitialAttractions();
      
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
      } else {
        mapPositions.value = [];
      }
    } catch (error) {
      console.error('초기 관광지 목록 조회 실패:', error);
    } finally {
      loading.value = false;
    }
  };

  const fetchSidoList = async () => {
    try {
      const response = await getSidoList();
      sidoList.value = response.data || [];
      return sidoList.value;
    } catch (error) {
      console.error('시도 목록 조회 실패:', error);
      return [];
    }
  };

  const fetchPopularAttractions = async (sidoCode = null) => {
    try {
      loading.value = true;
      const params = {
        sortBy: 'likes',
        size: 4
      };
      
      if (sidoCode) {
        params.sidoCode = sidoCode;
      }
      
      const response = await listAttractions(params);
      const sidoName = sidoList.value.find(sido => sido.code === sidoCode)?.name || '전국';
      
      return {
        attractions: response.data.attractionList || [],
        sidoName
      };
    } catch (error) {
      console.error('인기 관광지 조회 실패:', error);
      return {
        attractions: [],
        sidoName: '전국'
      };
    } finally {
      loading.value = false;
    }
  };

  const handleFavoriteToggle = async (attractionId) => {
    const favoriteStore = useFavoriteStore();
    await favoriteStore.toggleFavorite(attractionId);
    
    // 목록에서 해당 관광지 찾아서 상태 업데이트
    const attraction = attractions.value.find(item => item.no === attractionId);
    if (attraction) {
      attraction.isLike = !attraction.isLike;
      attraction.hit = attraction.isLike ? attraction.hit + 1 : Math.max(0, attraction.hit - 1);
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
    searchParams,
    fetchAttractions,
    fetchInitialAttractions,
    fetchSidoList,
    fetchPopularAttractions,
    setMapPositions,
    handleFavoriteToggle,
  };
}); 