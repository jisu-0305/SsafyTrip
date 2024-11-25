import { defineStore } from 'pinia';
import { ref } from 'vue';
import { getAttractionDetail } from '@/api/attractApi';

export const useAttractionDetailStore = defineStore('attractionDetail', () => {
  const attraction = ref(null);
  const comments = ref([]);
  const loading = ref(false);

  const fetchAttractionDetail = async (attractId) => {
    try {
      loading.value = true;
      const response = await getAttractionDetail(attractId);
      attraction.value = response.data.attract;
      comments.value = response.data.commentList;
    } catch (error) {
      console.error('관광지 상세 정보 조회 실패:', error);

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
    fetchAttractionDetail
  };
}); 