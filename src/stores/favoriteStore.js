import { defineStore } from 'pinia';
import { ref } from 'vue';
import { addFavorite, deleteFavorite } from '@/api/attractApi';
import { getFavorites } from '@/api/mypageApi';
import { useAttractionDetailStore } from '@/stores/attractionDetailStore';

export const useFavoriteStore = defineStore('favorite', () => {
  const favoriteAttractions = ref(new Set());
  const loading = ref(false);
  const error = ref(null);
  const favoriteList = ref([]);
  const totalPages = ref(1);
  const totalCount = ref(0);

  const fetchFavorites = async (params) => {
    try {
      loading.value = true;
      error.value = null;
      const response = await getFavorites(params);
      favoriteList.value = response.data.attractionList || [];
      totalPages.value = response.data.totalPages;
      totalCount.value = response.data.totalCount;

      // 좋아요 상태 동기화
      favoriteList.value.forEach(item => {
        favoriteAttractions.value.add(item.no);
      });

      return response.data;
    } catch (err) {
      console.error('즐겨찾기 목록 조회 실패:', err);
      error.value = '즐겨찾기 목록을 불러오는데 실패했습니다.';
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const toggleFavorite = async (attractionId) => {
    try {
      loading.value = true;
      error.value = null;
      const attractionDetailStore = useAttractionDetailStore();

      if (favoriteAttractions.value.has(attractionId)) {
        await deleteFavorite(attractionId);
        favoriteAttractions.value.delete(attractionId);
        if (attractionDetailStore.attraction) {
          attractionDetailStore.attraction.hit = Math.max(0, attractionDetailStore.attraction.hit - 1);
          attractionDetailStore.attraction.isFavorite = false;
        }
      } else {
        await addFavorite(attractionId);
        favoriteAttractions.value.add(attractionId);
        if (attractionDetailStore.attraction) {
          attractionDetailStore.attraction.hit++;
          attractionDetailStore.attraction.isFavorite = true;
        }
      }
    } catch (err) {
      error.value = '좋아요 처리 중 오류가 발생했습니다.';
      console.error('좋아요 토글 실패:', err);
    } finally {
      loading.value = false;
    }
  };

  const setFavoriteStatus = (attractionId, status) => {
    if (status) {
      favoriteAttractions.value.add(attractionId);
    } else {
      favoriteAttractions.value.delete(attractionId);
    }
  };

  const clearFavorites = () => {
    favoriteAttractions.value.clear();
  };

  return {
    favoriteAttractions,
    favoriteList,
    totalPages,
    totalCount,
    loading,
    error,
    fetchFavorites,
    toggleFavorite,
    setFavoriteStatus,
    clearFavorites
  };
}); 