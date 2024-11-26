import myaxios from "@/api/axios.js";

// 날씨 정보 분석 및 코디 추천 API
function analyzeSchedule(scheduleData) {
  return myaxios.post('/ai/weather', {
    schedule: scheduleData.schedule,
    schedulePlacesByDate: scheduleData.schedulePlacesByDate
  }, {
    withCredentials: true
  });
}

export const aiApi = {
  analyzeSchedule,
}; 