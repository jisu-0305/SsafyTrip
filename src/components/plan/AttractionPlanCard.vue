<template>
    <v-card class="mx-auto my-4" max-width="400">
        <v-toolbar 
            flat 
            color="primary" 
            dark
            :title="`DAY ${day}`"
        >
            <v-spacer></v-spacer>
            <v-btn
                icon="mdi-delete"
                @click="emitRemoveDay"
                aria-label="일정 삭제"
            ></v-btn>
        </v-toolbar>

        <v-card-text>
            <v-list>
                <v-hover v-for="(item, index) in schedules" :key="index" v-slot="{ isHovering, props }">
                    <v-list-item
                        v-bind="props"
                        :class="{ 'on-hover': isHovering }"
                    >
                        <template v-slot:prepend>
                            <v-avatar 
                                color="primary" 
                                size="32"
                                class="white--text"
                            >
                                {{ index + 1 }}
                            </v-avatar>
                        </template>

                        <v-list-item-title class="font-weight-bold">
                            {{ item.title }}
                        </v-list-item-title>
                        
                        <v-list-item-subtitle>
                            {{ item.memo }}
                        </v-list-item-subtitle>

                        <template v-slot:append>
                            <v-btn
                                icon="mdi-trash-can-outline"
                                variant="text"
                                size="small"
                                @click="removeSchedule(index)"
                                aria-label="관광지 삭제"
                            ></v-btn>
                        </template>
                    </v-list-item>
                </v-hover>
            </v-list>

            <v-divider class="my-4"></v-divider>
            
            <v-btn
                block
                color="primary"
                variant="outlined"
                prepend-icon="mdi-plus"
                @click="emitAddSchedule"
            >
                관광지 추가
            </v-btn>
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
.on-hover {
    background-color: rgba(var(--v-theme-primary), 0.04);
}
</style>
