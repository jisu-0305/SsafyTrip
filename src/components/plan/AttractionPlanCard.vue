<script setup>
import { ref } from 'vue';

// data 배열 정의 (참조형)
const datas = ref([
    { title: '제목1', memo: '메모1' },
    { title: '제목2', memo: '메모2' },
    { title: '제목3', memo: '메모3' },
    { title: '제목4', memo: '메모4' },
    { title: '제목5', memo: '메모5' }
]);

// 일정 추가 핸들러
const addItem = () => {
    // 새로운 일정 항목 추가
    const newItem = { title: `제목${datas.value.length + 1}`, memo: `메모${datas.value.length + 1}` };
    datas.value.push(newItem);
    console.log('Updated data:', datas.value);
};

// 일정 삭제 핸들러
const removeItem = (index) => {
    // 해당 인덱스의 항목을 삭제
    datas.value.splice(index, 1);
    console.log('Updated data after removal:', datas.value);
};

// 부모 컴포넌트로 팝업을 열도록 이벤트 발행
const emit = defineEmits(['addSchedule']);
</script>

<template>
    <v-card class="mx-auto my-4" max-width="400">
        <v-toolbar flat color="black" dark>
            <v-toolbar-title>
                DAY 1 <small class="day-date">2024.11.21</small>
            </v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn icon>
                <v-icon>mdi-delete</v-icon>
            </v-btn>
        </v-toolbar>

        <v-card-text>
            <v-container>
                <!-- 각 일정 항목 반복 렌더링 -->
                <v-row v-for="(item, index) in datas" :key="index" align="center" class="mb-2">
                    <v-col cols="1" style="margin-right: 10px;">
                        <v-avatar color="orange" size="24">
                            <span class="white--text">{{ index + 1 }}</span>
                        </v-avatar>
                    </v-col>
                    <v-col>
                        <div class="font-weight-bold">{{ item.title }}</div>
                        <div>{{ item.memo }}</div>
                    </v-col>
                    <v-col class="text-right" cols="1">
                        <v-btn icon @click="removeItem(index)">
                            <v-icon>mdi-trash-can-outline</v-icon>
                        </v-btn>
                    </v-col>
                    <v-divider></v-divider>
                </v-row>
            </v-container>

            <v-container>
                <v-btn outlined color="primary" block @click="$emit('addSchedule')">
                    일정 추가
                </v-btn>
            </v-container>
        </v-card-text>
    </v-card>
</template>

<style scoped>
.day-date {
    font-size: 14px;
    margin-left: 10px;
    color: #bbb;
}
</style>
