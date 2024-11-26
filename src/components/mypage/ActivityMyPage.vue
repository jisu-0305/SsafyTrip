<template>
  <div class="pa-8">
    <v-card
      variant="tonal"
      color="grey"
      class="mb-12 rounded-lg"
    >
      <v-row align="center" no-gutters>
        <v-col cols="8">
          <div class="pa-6">
            <div class="text-h5 font-weight-bold text-black">
              반갑습니다 {{ authStore.user?.name }}님!
            </div>
            <v-btn
              variant="text"
              color="primary"
              class="px-0 mt-2"
              @click="router.push({ name: 'attract-plan' })"
            >
              <v-icon start>mdi-arrow-right</v-icon>
              새로운 여행을 준비하러 가실래요?
            </v-btn>
          </div>
        </v-col>
        <v-col cols="4" class="d-flex justify-end">
          <v-img
            src="/img/dog.png"
            max-width="150"
            contain
            class="ma-4"
          ></v-img>
        </v-col>
      </v-row>
    </v-card>

    <v-card-title class="text-h5 font-weight-bold mb-8">나의 활동</v-card-title>
    
    <v-row justify="space-around" align="center">
      <v-col 
        v-for="item in activityItems" 
        :key="item.title" 
        cols="6" 
        sm="3"
        class="text-center"
      >
        <v-btn
          icon
          size="x-large"
          variant="tonal"
          color="blue"
          class="mb-3"
          @click="handleClick(item.title)"
        >
          <v-icon size="32">{{ item.icon }}</v-icon>
        </v-btn>
        <div class="font-weight-bold">{{ item.title }}</div>
      </v-col>
    </v-row>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/authStores';

const router = useRouter();
const authStore = useAuthStore();

const activityItems = [
  { title: '찜 목록', icon: 'mdi-bookmark-outline', route: 'favorites' },
  { title: '나의 댓글', icon: 'mdi-chat-outline', route: 'reviews' },
  { title: '여행 일정', icon: 'mdi-map-outline', route: 'courses' },
  { title: '1:1 문의', icon: 'mdi-help-circle-outline', route: 'question' }
];

const handleClick = (activity) => {
  const item = activityItems.find(item => item.title === activity);
  if (item) {
    if (item.route === 'question') {
      router.push({ name: 'question' });
    } else if (item.route === 'favorites') {
      router.push({ name: 'favorites' });
    }  
    else {
      alert(`${activity} 버튼이 클릭되었습니다.`);
    }
  }
};
</script>