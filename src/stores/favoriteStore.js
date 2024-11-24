import { defineStore } from 'pinia';
import { ref } from 'vue';
import { addFavorite, deleteFavorite } from '@/api/attractApi';
import { useAttractionDetailStore } from '@/stores/attractionDetailStore';

export const useFavoriteStore = defineStore('favorite', () => {
  const favoriteAttractions = ref(new Set());
  const loading = ref(false);
  const error = ref(null);

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
    loading,
    error,
    toggleFavorite,
    setFavoriteStatus,
    clearFavorites
  };
}); 