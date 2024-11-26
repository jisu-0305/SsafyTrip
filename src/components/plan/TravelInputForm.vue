<template>
    <v-container class="input-container">
        <v-row class="input-row">
            <!-- 여행 제목 -->
            <v-col cols="12">
                <v-text-field label="여행 제목" v-model="travelTitle" outlined class="input-field"></v-text-field>
            </v-col>
        </v-row>
        <v-row class="input-row">
            <!-- 출발 날짜 -->
            <v-col cols="12" md="6">
                <v-text-field v-model="startDateFormatted" label="출발 날짜 선택" prepend-icon="mdi-calendar" readonly
                    outlined class="input-field" @click="startDialog = true" />
                <v-dialog v-model="startDialog" width="290">
                    <v-card>
                        <v-date-picker v-model="startDate" scrollable @change="handleStartDateChange"></v-date-picker>
                    </v-card>
                </v-dialog>
            </v-col>
            <!-- 도착 날짜 -->
            <v-col cols="12" md="6">
                <v-text-field v-model="endDateFormatted" label="도착 날짜 선택" prepend-icon="mdi-calendar" readonly outlined
                    class="input-field" @click="endDialog = true" />
                <v-dialog v-model="endDialog" width="290">
                    <v-card>
                        <v-date-picker v-model="endDate" scrollable @change="handleEndDateChange"></v-date-picker>
                    </v-card>
                </v-dialog>
            </v-col>
        </v-row>
        <v-row class="input-row">
            <!-- 총 비용 -->
            <v-col cols="12">
                <v-text-field label="총 비용 (₩)" :value="totalCost" readonly outlined class="input-field" />
            </v-col>
        </v-row>
        <v-row class="input-row">
            <!-- 메모 -->
            <v-col cols="12">
                <v-textarea v-model="memo" label="메모" outlined counter="300" class="input-field"></v-textarea>
            </v-col>
        </v-row>
    </v-container>
</template>

<script setup>
import { ref, computed, watch } from 'vue';

const props = defineProps(['totalCost']);
const emit = defineEmits(['update:travelTitle', 'update:startDate', 'update:endDate', 'update:memo']);

const travelTitle = ref('');
const startDate = ref(null);
const endDate = ref(null);
const memo = ref('');

const startDialog = ref(false);
const endDialog = ref(false);

const startDateFormatted = computed({
    get: () => (startDate.value ? startDate.value.toLocaleDateString() : ''),
    set: (value) => {
        startDate.value = new Date(value);
        emit('update:startDate', startDate.value);
    },
});

const endDateFormatted = computed({
    get: () => (endDate.value ? endDate.value.toLocaleDateString() : ''),
    set: (value) => {
        endDate.value = new Date(value);
        emit('update:endDate', endDate.value);
    },
});

watch(memo, (newMemo) => {
    emit('update:memo', newMemo);
});

const handleStartDateChange = () => {
    startDialog.value = false;
    emit('update:startDate', startDate.value);
};

const handleEndDateChange = () => {
    endDialog.value = false;
    emit('update:endDate', endDate.value);
};
</script>

<style scoped>
.input-container {
    margin: 0 auto;
    text-align: center;
    /* 중앙 정렬 */
}

.input-row {
    margin-bottom: 20px;
}

.input-field {
    width: 100%;
}
</style>
