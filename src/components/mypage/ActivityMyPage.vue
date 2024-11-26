<template>
  <div class="pa-8">
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
          color="primary"
          variant="tonal"
          class="mb-3"
          @click="handleClick(item.title)"
        >
          <v-icon size="32">{{ item.icon }}</v-icon>
        </v-btn>
        <div>{{ item.title }}</div>
      </v-col>
    </v-row>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();
const activityItems = [
  { title: '찜목록', icon: 'mdi-bookmark-outline', route: 'favorites' },
  { title: '여행톡', icon: 'mdi-chat-outline', route: 'reviews' },
  { title: '여행코스', icon: 'mdi-map-outline', route: 'courses' },
  { title: '1:1문의', icon: 'mdi-help-circle-outline', route: 'question' }
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