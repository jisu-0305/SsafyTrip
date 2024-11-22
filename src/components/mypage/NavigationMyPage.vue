<script setup>
  import { ref } from 'vue';
  import { defineProps, defineEmits } from 'vue';
  
  
// 부모 컴포넌트로 이벤트를 전달하기 위해 emits 정의
const emit = defineEmits(['updateFlag']);
// 이벤트 핸들러 함수
function handleActivityClick() {
  emit('updateFlag', 1);
  // alert("나의 활동 버튼 클릭됨");
}

function handleInfoUpdateClick() {
  emit('updateFlag', 2);
}

function handleDeleteClick() {
  emit('updateFlag', 3);
}


</script>

<template>
    <v-container
      class="d-flex flex-column align-center py-5 unified-container"
      style="background-color: #1976d2; height: 100vh"
    >
      <v-avatar size="100" class="mb-4">
        <img src="https://via.placeholder.com/100" alt="Profile Picture" />
      </v-avatar>
      <v-typography class="white--text mb-2" variant="h6">홍길동</v-typography>
      <v-divider class="white--text mb-2" style="width: 100%"></v-divider>
      <button
        class="white--text text-uppercase my-2"
        style="border: none; background: transparent"
        @click="handleActivityClick"
      >
        나의 활동
      </button>
      <v-divider class="white--text mb-2" style="width: 100%"></v-divider>
      <button
        class="white--text text-uppercase my-2"
        style="border: none; background: transparent"
        @click="handleInfoUpdateClick"
      >
        회원정보 수정
      </button>
      <v-divider class="white--text mb-2" style="width: 100%"></v-divider>
      <button
        class="white--text text-uppercase my-2"
        style="border: none; background: transparent"
        @click="handleDeleteClick"
      >
        회원탈퇴
      </button>
      <div class="component-container">
    <component
      :is="currentComponent"
      v-if="currentComponent"
      class="activity-component"
    />
  </div>
    </v-container>
  </template>
  
   
  <style scoped>
  .v-container {
    width: 100%;
    padding: 32px;  /* 패딩을 32px로 수정 */
    margin-right: 0;  /* 기본 마진을 제거 */
    margin-left: 0;   /* 기본 마진을 제거 */
    min-width: 300px;
    min-height: 600px;
    max-width: 300px;
    max-height: 600px;
    }
  .unified-container {
    /* min-width: 300px;
    min-height: 600px;
    max-width: 300px;
    max-height: 600px; */
    /* margin: auto; */
  }
  .pa-4 {
    padding: 0px !important;
}
  .unified-card {
    min-height: 300px; /* 두 컴포넌트의 높이를 맞추기 위해 최소 높이 설정 */
    /* padding: 16px; 통일된 내부 패딩 추가 */
  }
  
  v-avatar img {
    object-fit: cover;
  }
  </style>