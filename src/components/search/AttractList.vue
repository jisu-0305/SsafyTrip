<script setup>
import { storeToRefs } from 'pinia';
import { useAttractionStore } from '@/stores/attractionStore';
import { useFavoriteStore } from '@/stores/favoriteStore';
import { useAuthStore } from '@/stores/authStores';
import { useRouter } from 'vue-router';
import { onMounted } from 'vue';

const router = useRouter();
const attractionStore = useAttractionStore();
const favoriteStore = useFavoriteStore();
const authStore = useAuthStore();
const { attractions, currentPage, totalPages, totalCount } = storeToRefs(attractionStore);
const { favoriteAttractions } = storeToRefs(favoriteStore);

// 천 단위 콤마 포맷팅 함수
const formatNumber = (num) => {
  return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
};

const emit = defineEmits(['update:page']);

const handlePageChange = (page) => {
  emit('update:page', page);
};

const handleFavoriteToggle = async (event, attractId) => {
  event.stopPropagation();
  
  if (!authStore.isLoggedIn) {
    alert('로그인이 필요한 서비스입니다.');
    router.push({ name: 'user-login' });
    return;
  }

  await favoriteStore.toggleFavorite(attractId);
  
  // 좋아요 수 업데이트
  const attraction = attractions.value.find(item => item.no === attractId);
  if (attraction) {
    attraction.isLike = !attraction.isLike;
    attraction.hit = attraction.isLike ? attraction.hit + 1 : Math.max(0, attraction.hit - 1);
  }
};

const props = defineProps({
  hoveredMarkerId: Number
});

onMounted(() => {
  // 각 관광지의 isLike 상태를 favoriteAttractions에 반영
  attractions.value.forEach(attraction => {
    if (attraction.isLike) {
      favoriteStore.setFavoriteStatus(attraction.no, true);
    }
  });
});
</script>

<template>
  <v-card>
    <v-card-title class="d-flex justify-space-between align-center px-4">
      <span>검색 결과</span>
      <span class="text-subtitle-2">총 {{ formatNumber(totalCount) }}건</span>
    </v-card-title>

    <v-divider></v-divider>

    <v-list lines="three">
      <v-list-item
        v-for="attraction in attractions"
        :key="attraction.no"
        :class="{ 'highlighted': attraction.contentId === hoveredMarkerId }"
        @click="$router.push(`/attraction/${attraction.no}`)"
        style="cursor: pointer"
      >
        <template v-slot:prepend>
          <v-avatar size="100">
            <v-img 
              :src="attraction.firstImage1" 
              :alt="attraction.title"
              cover
            >
              <template v-slot:placeholder>
                <v-avatar color="grey-lighten-2" size="100">
                  <v-icon icon="mdi-image-off" color="grey-darken-2" size="32"></v-icon>
                </v-avatar>
              </template>
            </v-img>
          </v-avatar>
        </template>


        <div>
          <v-chip
            size="small"
            color="primary"
            class="font-weight-medium mb-1"
          >
            {{ attraction.contentType ? attraction.contentType : '기타' }}
          </v-chip>
        </div>

        <v-list-item-title class="text-subtitle-1 font-weight-bold">
          {{ attraction.title }}
        </v-list-item-title>


        <v-list-item-subtitle>
          <div class="d-flex flex-column gap-2">
            
            <div class="text-caption text-grey-darken-3">{{ attraction.address }}</div>
            <div class="d-flex align-center gap-4">
              <div class="d-flex align-center">
                <v-btn
                  icon
                  variant="text"
                  size="small"
                  :color="attraction.isLike ? 'error' : ''"
                  :loading="favoriteStore.loading"
                  @click="(e) => handleFavoriteToggle(e, attraction.no)"
                >
                  <v-icon>
                    {{ attraction.isLike ? 'mdi-heart' : 'mdi-heart-outline' }}
                  </v-icon>
                </v-btn>
                <span class="text-caption">{{ attraction.hit }}</span>
              </div>
              
              <div class="d-flex align-center">
                <v-icon size="small" color="grey" class="me-1">mdi-eye</v-icon>
                <span class="text-caption">{{ attraction.views }}</span>
              </div>
            </div>
          </div>
        </v-list-item-subtitle>
      </v-list-item>
    </v-list>

    <v-card-actions class="justify-center">
      <v-pagination
        v-model="currentPage"
        :length="totalPages"
        :total-visible="7"
        @update:model-value="handlePageChange"
      ></v-pagination>
    </v-card-actions>
  </v-card>
</template>

<style scoped>
.highlighted {
  position: relative;
  background-color: rgba(var(--v-theme-primary), 0.1);
  transition: all 0.3s ease;
}

.highlighted::after {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  width: 4px;
  height: 100%;
  background-color: rgb(var(--v-theme-primary));
}

.v-list-item {
  transition: background-color 0.3s ease;
}
</style>