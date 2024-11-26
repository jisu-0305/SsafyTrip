<template>
    <div v-if="showPopup" class="popup-container">
        <div class="popup-header">
            <span>일정 추가</span>
            <button class="close-button" @click="$emit('close')">X</button>
        </div>
        <div class="popup-body">
            <!-- 관광지 이름 (읽기 전용) -->
            <v-text-field label="관광지 이름" :value="selectedSpot?.title || '선택된 관광지가 없습니다.'" outlined readonly
                class="input-field" />

            <!-- 방문 시간 -->
            <v-text-field v-model="timeFormatted" label="방문 시간 선택" prepend-icon="mdi-clock" readonly outlined
                class="input-field" @click="timeDialog = true" />
            <v-dialog v-model="timeDialog" persistent width="400">
                <v-card>
                    <v-card-title>방문 시간 선택</v-card-title>
                    <v-time-picker v-model="time" color="blue" header-color="blue darken-2" :landscape="true" scrollable
                        format="24hr" @change="confirmTime" />
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn text @click="timeDialog = false">닫기</v-btn>
                    </v-card-actions>
                </v-card>
            </v-dialog>

            <!-- 메모 -->
            <v-textarea v-model="memo" label="메모" outlined counter="200" rows="3" class="input-field"></v-textarea>

            <!-- 예상 금액 -->
            <v-text-field label="예상 금액 (₩)" v-model="estimatedCost" outlined type="number" class="input-field" />

            <!-- 추가 버튼 -->
            <v-btn block color="primary" class="mt-4" @click="addSchedule">추가</v-btn>
        </div>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { VTimePicker } from 'vuetify/labs/VTimePicker'; // Import VTimePicker from Vuetify labs

// Props와 Emits 정의
const props = defineProps(['selectedSpot', 'showPopup', 'selectedDate']);
const emit = defineEmits(['add', 'close']);

// 데이터 초기화
const time = ref(null); // 방문 시간
const memo = ref(''); // 메모
const estimatedCost = ref(0); // 예상 금액
const timeDialog = ref(false); // 시간 선택 다이얼로그 상태

// 시간 형식 변환 및 디버깅 추가
const visitDateTime = computed(() => {
    if (!time.value || !props.selectedDate) return null;

    const date = new Date(props.selectedDate);
    const [hours, minutes] = time.value.split(':');
    date.setHours(hours, minutes, 0);
    return date.toISOString(); // ISO 형식으로 반환
});


// 시간 확인 후 다이얼로그 닫기
const confirmTime = () => {
    if (!time.value) {
        alert('시간을 선택해주세요.');
        return;
    }
    timeDialog.value = false;
};

// 일정 추가 핸들러
const addSchedule = () => {
    if (!visitDateTime.value) {
        alert('방문 시간을 설정해주세요.');
        return;
    }

    const schedule = {
        attractionId: props.selectedSpot?.id,
        title: props.selectedSpot?.title,
        visitTime: visitDateTime.value, // 결합된 LocalDateTime 값
        memo: memo.value,
        cost: parseInt(estimatedCost.value, 10) || 0,
    };

    console.log("Schedule to be added:", schedule); // 디버깅 로그
    emit('add', schedule); // 부모로 데이터 전달
    emit('close'); // 팝업 닫기
};
</script>

<style scoped>
.popup-container {
    width: 400px;
    padding: 20px;
    background-color: white;
    border: 1px solid #ddd;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.popup-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}

.close-button {
    background: none;
    border: none;
    font-size: 20px;
    cursor: pointer;
}

.popup-body {
    display: flex;
    flex-direction: column;
    gap: 20px;
}

.input-field {
    width: 100%;
}
</style>
