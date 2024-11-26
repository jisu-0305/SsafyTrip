<script setup>
import { ref, onMounted } from "vue";
import { storeToRefs } from 'pinia';
import { useAuthStore } from '@/stores/authStores';
import { useFavoriteStore } from '@/stores/favoriteStore';
import { useRouter } from 'vue-router';
import PageHeader from "@/components/common/PageHeader.vue";
import SearchResultInfo from "@/components/common/SearchResultInfo.vue";

const router = useRouter();
const authStore = useAuthStore();
const favoriteStore = useFavoriteStore();
const { favoriteList, totalPages, totalCount } = storeToRefs(favoriteStore);

const page = ref(1);
const search = ref("");

const formatNumber = (num) => {
  return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
};

const fetchAttractions = async () => {
  try {
    if (!authStore.isLoggedIn) {
      alert('로그인이 필요한 서비스입니다.');
      router.push({ name: 'user-login' });
      return;
    }
    await favoriteStore.fetchFavorites({
      page: page.value,
      word: search.value
    });
  } catch (error) {
    if (error.response?.status === 401 || error.response?.status === 403) {
      alert('로그인이 필요한 서비스입니다.');
      router.push('/login');
    }
    console.error("Error fetching attractions:", error);
  }
};

const handleSearch = () => {
  page.value = 1;
  fetchAttractions();
};

const handlePageChange = (newPage) => {
  page.value = newPage;
  fetchAttractions();
};

const handleFavoriteToggle = async (attractId) => {
  if (!authStore.isLoggedIn) {
    alert('로그인이 필요한 서비스입니다.');
    router.push({ name: 'user-login' });
    return;
  }

  await favoriteStore.toggleFavorite(attractId);

    const attraction = favoriteList.value.find(item => item.no === attractId);
    if (attraction) {
      attraction.hit = favoriteStore.favoriteAttractions.has(attractId) 
        ? attraction.hit + 1 
        : Math.max(0, attraction.hit - 1);
    }
  
};

onMounted(fetchAttractions);
</script>

<template>
  <v-container fluid class="page-container">
    <v-row justify="center">
      <v-col cols="12" class="content-wrapper">
        <div class="inner-content">
          <PageHeader title="나의 즐겨찾기" icon="mdi-heart" />
          <div class="content-area">
            <div class="search-area mb-6">
              <v-row>
                <v-col :cols="10">
                  <v-text-field
                    v-model="search"
                    label="검색어"
                    @keyup.enter="handleSearch"
                    density="compact"
                    variant="outlined"
                    prepend-inner-icon="mdi-magnify"
                  ></v-text-field>
                </v-col>
                <v-col cols="2">
                  <v-btn 
                    color="primary" 
                    @click="handleSearch" 
                    block
                    height="40"
                  >
                    검색
                  </v-btn>
                </v-col>
              </v-row>
            </div>

            <div class="list-area">
              <SearchResultInfo :total-count="totalCount" />
              <v-card>
                <v-list lines="three">
                  <v-list-item
                    v-for="item in favoriteList"
                    :key="item.no"
                    @click="$router.push(`/attraction/${item.no}`)"
                    style="cursor: pointer"
                  >
                    <template v-slot:prepend>
                      <v-avatar size="100">
                        <v-img 
                          :src="item.firstImage1" 
                          :alt="item.title"
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

                    <v-list-item-title class="text-subtitle-1 font-weight-bold mb-1">
                      {{ item.title }}
                    </v-list-item-title>

                    <v-list-item-subtitle>
                      <div class="d-flex flex-column gap-2">
                        <div>
                          <v-chip
                            size="small"
                            color="primary"
                            class="font-weight-medium"
                          >
                            여행지
                          </v-chip>
                        </div>
                        
                        <div class="text-caption text-grey">{{ item.address }}</div>
                        
                        <div class="d-flex align-center gap-4">
                          <div class="d-flex align-center">
                            <v-icon 
                              size="small" 
                              :color="favoriteStore.favoriteAttractions.has(item.no) ? 'error' : ''"
                              class="me-1"
                              :icon="favoriteStore.favoriteAttractions.has(item.no) ? 'mdi-heart' : 'mdi-heart-outline'"
                              @click.stop="handleFavoriteToggle(item.no)"
                            >
                              mdi-heart
                            </v-icon>
                            <span class="text-caption">{{ item.hit }}</span>
                          </div>
                        </div>
                      </div>
                    </v-list-item-subtitle>
                  </v-list-item>
                </v-list>

                <v-card-actions class="justify-center">
                  <v-pagination
                    v-model="page"
                    :length="totalPages"
                    :total-visible="7"
                    @update:model-value="handlePageChange"
                  ></v-pagination>
                </v-card-actions>
              </v-card>
            </div>
          </div>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.page-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
}

.v-list-item {
  transition: background-color 0.3s ease;
}

.v-list-item:hover {
  background-color: rgba(var(--v-theme-primary), 0.1);
}
</style>
