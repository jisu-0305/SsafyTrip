import { defineStore } from 'pinia';
import { ref } from 'vue';
import { aiApi } from '@/api/aiApi';

export const useAiStore = defineStore('ai', () => {
  // 상태 정의
  const weatherList = ref([]);
  const clothesURL = ref('');
  const supplies = ref('');
  const isLoading = ref(false);
  const error = ref(null);

  // 여행 일정 분석 함수
  const analyzeSchedule = async (scheduleData) => {
    isLoading.value = true;
    error.value = null;
    
    try {
      const response = await aiApi.analyzeSchedule(scheduleData);
      
      // 응답 데이터 저장
      weatherList.value = response.data.weatherList;
      clothesURL.value = response.data.clothesURL;
      supplies.value = response.data.supplies;
      
      return response.data;
    } catch (err) {
      console.error('일정 분석 중 오류 발생:', err);
      error.value = err.message || '일정 분석에 실패했습니다.';
      throw err;
    } finally {
      isLoading.value = false;
    }
  };

  // 준비물 카테고리별 파싱 함수
  const getParsedSupplies = () => {
    if (!supplies.value) return {};
    
    const categories = supplies.value.split('\n\n');
    const parsedSupplies = {};
    
    categories.forEach(category => {
      const [categoryName, ...items] = category.split('\n');
      const categoryKey = categoryName.replace(':', '').trim();
      parsedSupplies[categoryKey] = items.map(item => item.trim());
    });
    
    return parsedSupplies;
  };

  return {
    // 상태
    weatherList,
    clothesURL,
    supplies,
    isLoading,
    error,
    
    // 액션
    analyzeSchedule,
    
    // 게터
    getParsedSupplies,
  };
}); 