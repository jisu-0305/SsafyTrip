<template>
    <v-container class="input-container" style="width: 60%; margin: 0 auto;">
        <v-row class="input-row">
            <!-- 제목 입력란 -->
            <v-col cols="12">
                <v-text-field label="여행 제목" v-model="travelTitle" outlined class="input-field"></v-text-field>
            </v-col>
        </v-row>

        <v-row class="input-row">
            <!-- 출발 날짜 선택란 -->
            <v-col cols="12" md="6">
                <v-text-field v-model="startDateFormatted" label="출발 날짜 선택" prepend-icon="mdi-calendar" readonly
                    outlined class="input-field" @click="startDialog = true"></v-text-field>

                <!-- 출발 날짜 선택 다이얼로그 -->
                <v-dialog v-model="startDialog" width="290">
                    <v-card>
                        <v-date-picker v-model="startDate" scrollable @change="handleStartDateChange"></v-date-picker>
                    </v-card>
                </v-dialog>
            </v-col>

            <!-- 도착 날짜 선택란 -->
            <v-col cols="12" md="6">
                <v-text-field v-model="endDateFormatted" label="도착 날짜 선택" prepend-icon="mdi-calendar" readonly outlined
                    class="input-field" @click="endDialog = true"></v-text-field>

                <!-- 도착 날짜 선택 다이얼로그 -->
                <v-dialog v-model="endDialog" width="290">
                    <v-card>
                        <v-date-picker v-model="endDate" scrollable @change="handleEndDateChange"></v-date-picker>
                    </v-card>
                </v-dialog>
            </v-col>
        </v-row>
    </v-container>
</template>

<script setup>
import { ref, computed } from 'vue';

// 데이터 정의
const travelTitle = ref(''); // 여행 제목
const startDate = ref(null); // 출발 날짜 (초기값을 null로 설정)
const endDate = ref(null);   // 도착 날짜 (초기값을 null로 설정)
const startDialog = ref(false); // 출발 날짜 선택 다이얼로그 상태 초기값을 false로 설정
const endDialog = ref(false);   // 도착 날짜 선택 다이얼로그 상태 초기값을 false로 설정

// 날짜 형식을 YYYY-MM-DD로 변환하는 계산된 속성
const startDateFormatted = computed(() => {
    return startDate.value ? startDate.value.toLocaleDateString() : '';
});
const endDateFormatted = computed(() => {
    return endDate.value ? endDate.value.toLocaleDateString() : '';
});

// 날짜 선택 후 다이얼로그 닫기
const handleStartDateChange = () => {
    startDialog.value = false;
};

const handleEndDateChange = () => {
    endDialog.value = false;
};
</script>

<style scoped>
.input-container {
    margin-bottom: 20px;
}

.input-row {
    margin-bottom: 10px;
}

.input-field {
    height: 30px;
}
</style>
