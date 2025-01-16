import { defineStore } from "pinia";
import { ref } from "vue";
import { getAttractionDetail } from "@/api/attractApi";
import { useFavoriteStore } from "@/stores/favoriteStore";

export const useAttractionDetailStore = defineStore("attractionDetail", () => {
  const attraction = ref(null);
  const comments = ref([]);
  const loading = ref(false);

  const fetchAttractionDetail = async (attractId) => {
    try {
      loading.value = true;
      const response = await getAttractionDetail(attractId);
      attraction.value = response.data.attract;
      comments.value = response.data.commentList;

      // 좋아요 상태 설정
      const favoriteStore = useFavoriteStore();
      if (attraction.value?.isLike) {
        favoriteStore.setFavoriteStatus(attractId, true);
      }
    } catch (error) {
      console.error("관광지 상세 정보 조회 실패:", error);
      attraction.value = null;
      comments.value = [];
    } finally {
      loading.value = false;
    }
  };

  return {
    attraction,
    comments,
    loading,
    fetchAttractionDetail,
  };
});
