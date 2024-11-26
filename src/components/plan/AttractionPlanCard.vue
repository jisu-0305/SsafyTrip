<template>
    <v-card class="mx-auto my-4" max-width="400">
        <v-toolbar flat color="black" dark>
            <v-toolbar-title>
                DAY {{ day }}
            </v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn icon @click="emitRemoveDay">
                <v-icon>mdi-delete</v-icon>
            </v-btn>
        </v-toolbar>

        <v-card-text>
            <v-container>
                <!-- 일정 항목 반복 렌더링 -->
                <v-row v-for="(item, index) in schedules" :key="index" align="center" class="mb-2">
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
                        <v-btn icon @click="removeSchedule(index)">
                            <v-icon>mdi-trash-can-outline</v-icon>
                        </v-btn>
                    </v-col>
                </v-row>
            </v-container>

            <v-container>
                <v-btn outlined color="primary" block @click="emitAddSchedule">
                    관광지 추가
                </v-btn>
            </v-container>
        </v-card-text>
    </v-card>
</template>

<script setup>
import { ref } from 'vue';

const props = defineProps(['day', 'schedules']);
const emit = defineEmits(['addSchedule', 'updateSchedules', 'removeDay']);

const removeSchedule = (index) => {
    const updatedSchedules = [...props.schedules];
    updatedSchedules.splice(index, 1);
    emit('updateSchedules', updatedSchedules);
};

const emitAddSchedule = () => {
    emit('addSchedule', props.day);
};

const emitRemoveDay = () => {
    emit('removeDay', props.day);
};
</script>

<style scoped>
.day-date {
    font-size: 14px;
    margin-left: 10px;
    color: #bbb;
}
</style>
