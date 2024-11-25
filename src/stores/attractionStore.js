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
  const searchParams = ref({
    sidoCode: null,
    gugunCode: null,
    contentTypeId: null,
    keyword: '',
    sortBy: 'name'
  });
  const isInitialized = ref(localStorage.getItem('attraction-initialized') === 'true');

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
      
      sidoList.value = response.data.sidoList || [];
      contentTypeList.value = response.data.contentTypeList || [];
      
      localStorage.setItem('sido-list', JSON.stringify(sidoList.value));
      localStorage.setItem('content-type-list', JSON.stringify(contentTypeList.value));
      
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
      
      isInitialized.value = true;
      localStorage.setItem('attraction-initialized', 'true');
    } catch (error) {
      console.error('초기 관광지 목록 조회 실패:', error);
    } finally {
      loading.value = false;
    }
  };

  const saveState = () => {
    const state = {
      attractions: attractions.value,
      totalPages: totalPages.value,
      currentPage: currentPage.value,
      searchParams: searchParams.value,
      totalCount: totalCount.value,
      mapPositions: mapPositions.value
    };
    sessionStorage.setItem('attraction-state', JSON.stringify(state));
  };

  const restoreState = () => {
    const savedState = sessionStorage.getItem('attraction-state');
    if (savedState) {
      try {
        const state = JSON.parse(savedState);
        attractions.value = state.attractions || [];
        totalPages.value = state.totalPages || 0;
        currentPage.value = state.currentPage || 1;
        searchParams.value = state.searchParams || {};
        totalCount.value = state.totalCount || 0;
        mapPositions.value = state.mapPositions || [];
        
        return true;
      } catch (error) {
        console.error('상태 복원 실패:', error);
        return false;
      }
    }
    return false;
  };

  const initializeStore = () => {
    const savedSidoList = localStorage.getItem('sido-list');
    const savedContentTypeList = localStorage.getItem('content-type-list');
    
    if (savedSidoList) {
      sidoList.value = JSON.parse(savedSidoList);
    }
    
    if (savedContentTypeList) {
      contentTypeList.value = JSON.parse(savedContentTypeList);
    }
  };

  const fetchPopularAttractions = async (sidoCode = null) => {
    try {
      loading.value = true;
      const params = {
        sortBy: 'views',
      };
      
      if (sidoCode) {
        params.sidoCode = sidoCode;
      }
      
      const response = await listAttractions(params);
      return response.data.attractionList || [];
    } catch (error) {
      console.error('인기 관광지 조회 실패:', error);
      return [];
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
    searchParams,
    isInitialized,
    fetchAttractions,
    fetchInitialAttractions,
    initializeStore,
    saveState,
    restoreState,
    fetchPopularAttractions
  };
}); 