import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { getFavorites } from '@/api/attractApi';
import * as scheduleApi from '@/api/scheduleApi';

export const usePlanStore = defineStore('plan', () => {
  // 찜 목록 관련 상태
  const wishList = ref([]);
  const showWishList = ref(false);
  const selectedSpot = ref(null);
  
  // 일정 추가 팝업 관련 상태
  const showSchedulePopup = ref(false);
  const scheduleTime = ref(null);
  const scheduleMemo = ref('');
  const estimatedCost = ref(0);
  const timeDialog = ref(false);
  
  // computed
  const timeFormatted = computed(() => {
    if (!scheduleTime.value) return '';
    try {
      // 입력된 시간이 HH:mm 형식인지 확인
      const [hours, minutes] = scheduleTime.value.split(':');
      const time = new Date();
      time.setHours(parseInt(hours));
      time.setMinutes(parseInt(minutes));
      
      return time.toLocaleTimeString('ko-KR', { 
        hour: '2-digit', 
        minute: '2-digit',
        hour12: false 
      });
    } catch (error) {
      console.error('시간 포맷팅 오류:', error);
      return scheduleTime.value;
    }
  });
  
  // 찜 목록 관련 액션
  const fetchWishList = async () => {
    try {
      const data = await getFavorites();
      wishList.value = data.attractionList || [];
    } catch (error) {
      console.error('찜 목록 데이터를 불러오는 중 오류:', error);
    }
  };

  const setShowWishList = (value) => {
    showWishList.value = value;
  };

  const setSelectedSpot = (spot) => {
    selectedSpot.value = spot;
  };
  
  // 일정 추가 팝업 관련 액션
  const setShowSchedulePopup = (value) => {
    showSchedulePopup.value = value;
  };
  
  const setScheduleTime = (time) => {
    scheduleTime.value = time;
  };
  
  const setTimeDialog = (value) => {
    timeDialog.value = value;
  };
  
  const resetScheduleForm = () => {
    scheduleTime.value = null;
    scheduleMemo.value = '';
    estimatedCost.value = 0;
    selectedSpot.value = null;
    selectedDate.value = null;
    timeDialog.value = false;
  };

  // 여행 기본 정보 상태
  const travelTitle = ref('');
  const startDate = ref(null);
  const endDate = ref(null);
  const memo = ref('');
  const plans = ref([]);
  const currentDay = ref(null);
  const selectedDate = ref(null);

  // 여행 기본 정보 상태에 allSchedules 추가
  const allSchedules = ref([]);

  // 모든 일정 조회 함수 추가
  const fetchAllSchedules = async () => {
    try {
      const response = await scheduleApi.getAllSchedules();
      allSchedules.value = response.data;
      console.log('불러온 일정 목록:', allSchedules.value);
      return allSchedules.value;
    } catch (error) {
      console.error('일정 목록을 불러오는 중 오류 발생:', error);
      throw error;
    }
  };

  // 여행 기본 정보 관련 액션
  const setTravelInfo = ({ title, start, end, memoText }) => {
    travelTitle.value = title;
    startDate.value = start;
    endDate.value = end;
    memo.value = memoText;
  };

  // 일정 관리 액션
  const addNewPlan = () => {
    plans.value.push({ day: plans.value.length + 1, schedules: [] });
  };

  const removePlan = (day) => {
    plans.value = plans.value.filter(plan => plan.day !== day);
    plans.value = plans.value.map((plan, index) => ({
      ...plan,
      day: index + 1
    }));
  };

  const updateSchedules = (day, newSchedules) => {
    const planIndex = plans.value.findIndex(plan => plan.day === day);
    if (planIndex !== -1) {
      plans.value[planIndex].schedules = newSchedules;
    }
  };

  const addSchedule = () => {
    const planIndex = plans.value.findIndex(plan => plan.date === selectedDate.value);
    if (planIndex !== -1) {
      const newSchedule = {
        title: selectedSpot.value.title,
        time: scheduleTime.value,
        memo: scheduleMemo.value,
        estimatedCost: Number(estimatedCost.value),
        attractionId: selectedSpot.value.no,
        date: selectedDate.value
      };
      
      // 시간순 정렬을 위한 로직
      const updatedSchedules = [...plans.value[planIndex].schedules, newSchedule]
        .sort((a, b) => {
          if (!a.time) return 1;
          if (!b.time) return -1;
          return a.time.localeCompare(b.time);
        });
      
      plans.value[planIndex].schedules = updatedSchedules;
      console.log(`Added schedule to date ${selectedDate.value}:`, newSchedule);
    }
  };

  const setCurrentDay = (day) => {
    console.log('Setting current day:', day);
    currentDay.value = day;
  };

  // 총 비용 계산
  const calculateTotalCost = computed(() => {
    return plans.value.reduce((total, plan) => {
      return total + plan.schedules.reduce((daySum, schedule) => {
        return daySum + (schedule.estimatedCost || 0);
      }, 0);
    }, 0);
  });

  // 특정 날짜의 총 비용을 계산하는 함수 추가
  const calculateDayTotalCost = (date) => {
    const plan = plans.value.find(p => p.date === date);
    if (!plan) return 0;
    
    return plan.schedules.reduce((sum, schedule) => {
      return sum + (schedule.estimatedCost || 0);
    }, 0);
  };

  // 현재 일정 관련 computed
  const currentSchedules = computed(() => {
    if (!currentDay.value || plans.value.length === 0) return [];
    
    const currentPlan = plans.value.find(plan => plan.day === currentDay.value);
    console.log('Current Plan:', currentPlan);
    return currentPlan?.schedules || [];
  });

  const currentDayTotalCost = computed(() => {
    return currentSchedules.value.reduce((sum, schedule) => {
      return sum + (schedule.estimatedCost || 0);
    }, 0);
  });

  // 통화 포맷팅 함수
  const formatCurrency = (value) => {
    return new Intl.NumberFormat('ko-KR', {
      style: 'currency',
      currency: 'KRW'
    }).format(value || 0);
  };

  // 일정 삭제 함수
  const removeSchedule = (day, index) => {
    const planIndex = plans.value.findIndex(plan => plan.day === day);
    if (planIndex !== -1) {
      plans.value[planIndex].schedules.splice(index, 1);
    }
  };

  // 날짜 관련 함수들 추가
  const initializePlans = () => {
    if (!startDate.value || !endDate.value) return;
    
    const start = new Date(startDate.value);
    const end = new Date(endDate.value);
    const dayCount = Math.ceil((end - start) / (1000 * 60 * 60 * 24)) + 1;
    
    const existingPlans = [...plans.value];
    
    plans.value = Array.from({ length: dayCount }, (_, index) => {
      const currentDate = new Date(start);
      currentDate.setDate(start.getDate() + index);
      
      const year = currentDate.getFullYear();
      const month = String(currentDate.getMonth() + 1).padStart(2, '0');
      const day = String(currentDate.getDate()).padStart(2, '0');
      const dateStr = `${year}-${month}-${day}`;
      
      const existingPlan = existingPlans.find(plan => plan.date === dateStr);
      return {
        day: index + 1,
        date: dateStr,
        schedules: existingPlan ? existingPlan.schedules : []
      };
    });

    if (plans.value.length > 0) {
      setCurrentDay(1);
    }
    
    console.log('Initialized plans with correct dates:', plans.value);
  };

  const startDateFormatted = computed(() => {
    if (!startDate.value) return '';
    return new Date(startDate.value).toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  });

  const endDateFormatted = computed(() => {
    if (!endDate.value) return '';
    return new Date(endDate.value).toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  });

  const setStartDate = (date) => {
    console.log('Setting start date:', date);
    startDate.value = date;
    if (endDate.value && new Date(date) > new Date(endDate.value)) {
      endDate.value = date;
    }
    initializePlans();
  };

  const setEndDate = (date) => {
    console.log('Setting end date:', date);
    if (!startDate.value || new Date(date) >= new Date(startDate.value)) {
      endDate.value = date;
      initializePlans();
    } else {
      alert('도착 날짜는 출발 날짜보다 이후여야 합니다.');
    }
  };

  // 선택된 날짜 설정 함
  const setSelectedDate = (date) => {
    selectedDate.value = date;
    console.log('Selected date for new schedule:', date);
  };

  // 일정 데이터 포맷팅 함수 추가
  const formatScheduleData = () => {
    // 모든 일정을 날짜순으로 정렬
    const allSchedules = plans.value.flatMap(plan => 
      plan.schedules.map(schedule => ({
        ...schedule,
        date: plan.date
      }))
    ).sort((a, b) => new Date(a.date) - new Date(b.date));

    return {
      scheduleInformation: {
        title: travelTitle.value,
        memo: memo.value,
        totalCost: calculateTotalCost.value,
        startDate: startDate.value,
        endDate: endDate.value
      },
      schedulePlaces: allSchedules.map((schedule, index) => ({
        attractionId: schedule.attractionId,
        visitTime: `${schedule.date}T${schedule.time}:00.000Z`,
        memo: schedule.memo,
        cost: schedule.estimatedCost,
        visitOrder: index + 1
      }))
    };
  };

  // 모든 데이터 초기화 함수 추가
  const resetAllData = () => {
    // 여행 기본 정보 초기화
    travelTitle.value = '';
    startDate.value = null;
    endDate.value = null;
    memo.value = '';
    plans.value = [];
    currentDay.value = null;
    selectedDate.value = null;

    // 찜 목록 관련 상태 초기화
    showWishList.value = false;
    selectedSpot.value = null;

    // 일정 추가 팝업 관련 상태 초기화
    showSchedulePopup.value = false;
    scheduleTime.value = null;
    scheduleMemo.value = '';
    estimatedCost.value = 0;
    timeDialog.value = false;
  };

  // 일정 상세 조회 함수 수정
  const fetchScheduleDetail = async (scheduleId) => {
    try {
      const response = await scheduleApi.getScheduleDetail(scheduleId);
      return response.data;
    } catch (error) {
      console.error('일정 상세 정보를 불러오는 중 오류 발생:', error);
      throw error;
    }
  };

  return {
    // 찜 목록 관련
    wishList,
    showWishList,
    selectedSpot,
    fetchWishList,
    setShowWishList,
    setSelectedSpot,
    
    // 일정 추가 팝업 관련
    showSchedulePopup,
    scheduleTime,
    scheduleMemo,
    estimatedCost,
    timeDialog,
    timeFormatted,
    setShowSchedulePopup,
    setScheduleTime,
    setTimeDialog,
    resetScheduleForm,

    // 여행 기본 정보
    travelTitle,
    startDate,
    endDate,
    memo,
    plans,
    currentDay,
    calculateTotalCost,

    // 액션들
    setTravelInfo,
    addNewPlan,
    removePlan,
    updateSchedules,
    addSchedule,
    setCurrentDay,
    currentSchedules,
    currentDayTotalCost,
    formatCurrency,
    removeSchedule,
    initializePlans,
    startDateFormatted,
    endDateFormatted,
    setStartDate,
    setEndDate,
    selectedDate,
    setSelectedDate,
    calculateDayTotalCost,
    formatScheduleData,
    resetAllData,

    // 새로운 상태와 함수 추가
    allSchedules,
    fetchAllSchedules,
    fetchScheduleDetail,
  };
}); 