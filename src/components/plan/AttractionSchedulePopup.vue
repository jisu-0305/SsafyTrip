<template>
    <div>
        <div class="popup-header">
            <span>일정 추가</span>
            <button class="close-button" @click="$emit('close')">X</button>
        </div>
        <div class="popup-body">
            <label>관광지 이름:</label>
            <input v-model="touristSpot" type="text" />

            <label>시간:</label>
            <input v-model="time" type="text" />

            <label>메모:</label>
            <textarea v-model="memo"></textarea>

            <label>예상 금액:</label>
            <input v-model="estimatedCost" type="number" />

            <button @click="addSchedule">추가</button>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';

const touristSpot = ref('');
const time = ref('');
const memo = ref('');
const estimatedCost = ref('');

const addSchedule = () => {
    const schedule = {
        touristSpot: touristSpot.value,
        time: time.value,
        memo: memo.value,
        estimatedCost: estimatedCost.value,
    };
    $emit('add', schedule);
};
</script>

<style scoped>
.popup-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
}

.close-button {
    background: none;
    border: none;
    font-size: 20px;
    cursor: pointer;
}

.popup-body label {
    display: block;
    margin-top: 10px;
}

.popup-body input,
.popup-body textarea {
    width: calc(100% - 20px);
    margin-top: 5px;
    padding: 5px;
}

button {
    margin-top: 20px;
}
</style>
