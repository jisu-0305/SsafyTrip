import myaxios from "@/api/axios.js";

// 여행 일정 생성 API
function createSchedule(scheduleData) {
    return myaxios.post('/schedules', scheduleData);
}

// 여행 일정 삭제 API
function deleteSchedule(scheduleId) {
    return myaxios.delete(`/schedules/${scheduleId}`);
}

// 사용자 여행 일정 목록 조회 API
function getAllSchedules() {
    return myaxios.get('/schedules');
}

// 특정 여행 일정 상세 조회 API
function getScheduleDetail(scheduleId) {
    return myaxios.get(`/schedules/detail/${scheduleId}`);
}

export {
    createSchedule,
    deleteSchedule,
    getAllSchedules,
    getScheduleDetail
};
