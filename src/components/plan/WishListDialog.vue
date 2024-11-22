<template>
    <div v-if="showWishList" class="wishlist-popup">
        <v-card>
            <v-card-title>찜 목록</v-card-title>
            <v-card-text>
                <v-row>
                    <!-- 더미 데이터를 반복 렌더링 -->
                    <v-col v-for="(spot, index) in dummyWishList" :key="index" cols="12" md="6">
                        <v-card>
                            <v-img :src="spot.image" height="150"></v-img>
                            <v-card-title>{{ spot.title }}</v-card-title>
                            <v-card-subtitle>{{ spot.location }}</v-card-subtitle>
                            <v-btn color="primary" block @click="selectSpot(spot)">선택</v-btn>
                        </v-card>
                    </v-col>
                </v-row>
            </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn text @click="closeDialog">닫기</v-btn>
            </v-card-actions>
        </v-card>
    </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';

const props = defineProps(['showWishList']);
const emit = defineEmits(['update:showWishList', 'spotSelected']);

// 더미 데이터 정의
const dummyWishList = [
    {
        title: '관광지명 1',
        location: '서울특별시 강동구 구천면길 1',
        image: 'https://via.placeholder.com/150'
    },
    {
        title: '관광지명 2',
        location: '서울특별시 강동구 구천면길 2',
        image: 'https://via.placeholder.com/150'
    }
];

// 다이얼로그 닫기 핸들러
const closeDialog = () => {
    emit('update:showWishList', false);
};

// 관광지 선택 핸들러
const selectSpot = (spot) => {
    emit('spotSelected', spot);
};
</script>

<style scoped>
.wishlist-popup {
    position: fixed;
    top: 20%;
    right: 5%;
    width: 20%;
    background-color: #fff;
    padding: 20px;
    border: 1px solid #ddd;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    z-index: 1000;
}
</style>
