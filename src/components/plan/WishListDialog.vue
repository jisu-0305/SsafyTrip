<template>
    <div v-if="showWishList" class="wishlist-popup">
        <v-card>
            <v-card-title>
                찜 목록
                <v-btn icon @click="closeDialog">
                    <v-icon>mdi-close</v-icon>
                </v-btn>
            </v-card-title>
            <v-card-text>
                <v-row>
                    <!-- 찜 목록 데이터를 반복 렌더링 -->
                    <v-col v-for="(spot, index) in wishList" :key="index" cols="12" md="6">
                        <v-card>
                            <v-img :src="spot.firstImage1 || 'https://via.placeholder.com/150'" height="150"
                                alt="관광지 이미지"></v-img>
                            <v-card-title>{{ spot.title }}</v-card-title>
                            <v-card-subtitle>{{ spot.address }}</v-card-subtitle>
                            <v-btn color="primary" block @click="selectSpot(spot)">
                                선택
                            </v-btn>
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
import { ref, onMounted } from 'vue';
import { defineProps, defineEmits } from 'vue';
import { getFavorites } from '@/api/attractApi'; // API 호출 모듈

// Props와 Emits 정의
const props = defineProps(['showWishList']);
const emit = defineEmits(['update:showWishList', 'spotSelected']);

// 찜 목록 데이터 관리
const wishList = ref([]);

// API를 통해 찜 목록 데이터 불러오기
const fetchWishList = async () => {
    try {
        const data = await getFavorites(); // API 호출
        wishList.value = data.attractionList || []; // 데이터 저장
    } catch (error) {
        console.error('찜 목록 데이터를 불러오는 중 오류:', error);
    }
};

// 다이얼로그 닫기 핸들러
const closeDialog = () => {
    emit('update:showWishList', false); // 부모에게 상태 업데이트 요청
};

// 관광지 선택 핸들러
const selectSpot = (spot) => {
    emit('spotSelected', spot); // 부모에게 선택된 관광지 전달
};

// 컴포넌트 마운트 시 찜 목록 데이터 로드
onMounted(() => fetchWishList());
</script>

<style scoped>
.wishlist-popup {
    position: fixed;
    top: 20%;
    right: 5%;
    width: 30%;
    background-color: #fff;
    padding: 20px;
    border: 1px solid #ddd;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    z-index: 1000;
}

v-card {
    margin-bottom: 10px;
}

v-img {
    border-radius: 5px;
}
</style>
