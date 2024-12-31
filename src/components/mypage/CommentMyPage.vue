<script setup>
import { ref, onMounted } from "vue";
import { storeToRefs } from "pinia";
import { useAuthStore } from "@/stores/authStores";
import { useFavoriteStore } from "@/stores/favoriteStore";
import { useRouter } from "vue-router";
import PageHeader from "@/components/common/PageHeader.vue";
import SearchResultInfo from "@/components/common/SearchResultInfo.vue";
import { useCommentStore } from "@/stores/commentStore";

const router = useRouter();
const authStore = useAuthStore();
const favoriteStore = useFavoriteStore();
const totalCount = ref(0);

const getMyCommentItems = async () => {
  if (!authStore.isLoggedIn) {
    alert("로그인이 필요한 서비스입니다.");
    router.push({ name: "user-login" });
    return;
  }

  myCommentItems.value = await fetchMyComments();
  totalCount.value = myCommentItems.value.length;
};

const handleFavoriteToggle = async (attractId) => {
  if (!authStore.isLoggedIn) {
    alert("로그인이 필요한 서비스입니다.");
    router.push({ name: "user-login" });
    return;
  }

  await favoriteStore.toggleFavorite(attractId);

  const attraction = favoriteList.value.find((item) => item.no === attractId);
  if (attraction) {
    attraction.hit = favoriteStore.favoriteAttractions.has(attractId)
      ? attraction.hit + 1
      : Math.max(0, attraction.hit - 1);
  }
};

const commentStore = useCommentStore();
const { fetchMyComments, myComments } = commentStore;
const myCommentItems = ref([]);

onMounted(async () => {
  getMyCommentItems(); // 데이터를 가져온 후 콘솔에 찍기
});
</script>

<template>
  <v-container fluid class="page-container">
    <v-row justify="center">
      <v-col cols="12" class="content-wrapper">
        <div class="inner-content">
          <PageHeader title="나의 댓글" icon="mdi-comment" />
          <div class="content-area">
            <div class="list-area">
              <SearchResultInfo :total-count="totalCount" />
              <v-card>
                <v-card-text v-if="!myCommentItems.length" class="py-16">
                  <div class="d-flex flex-column align-center">
                    <v-icon
                      icon="mdi-comment-outline"
                      size="64"
                      color="grey-lighten-1"
                      class="mb-4"
                    ></v-icon>
                    <h3 class="text-h6 text-grey-darken-1 font-weight-medium mb-2">
                      작성한 댓글이 없습니다
                    </h3>
                    <p class="text-body-1 text-grey">
                      여행지에 대한 의견을 공유해보세요!
                    </p>
                    <v-btn
                      color="primary"
                      class="mt-4"
                      @click="$router.push('/attract/search')"
                    >
                      여행지 둘러보기
                    </v-btn>
                  </div>
                </v-card-text>

                <v-list v-else lines="three">
                  <v-list-item
                    v-for="item in myCommentItems"
                    :key="item.id"
                    @click="$router.push(`/attraction/${item.attractionId}`)"
                    style="cursor: pointer"
                  >
                    <template v-slot:prepend>
                      <v-avatar size="100">
                        <v-img :src="item.image" :alt="item.title" cover>
                          <template v-slot:placeholder>
                            <v-avatar color="grey-lighten-2" size="100">
                              <v-icon
                                icon="mdi-image-off"
                                color="grey-darken-2"
                                size="32"
                              ></v-icon>
                            </v-avatar>
                          </template>
                        </v-img>
                      </v-avatar>
                    </template>

                    <v-list-item-title
                      class="text-subtitle-1 font-weight-bold mb-1"
                    >
                      {{ item.content }}
                    </v-list-item-title>

                    <v-list-item-subtitle>
                      <div class="d-flex flex-column gap-2">
                        <div>
                          {{ item.attractionName }}
                        </div>

                        <div class="text-caption text-grey">
                          <!-- {{ item.atrractionName }} -->
                          <v-chip
                            size="small"
                            color="primary"
                            class="font-weight-medium"
                          >
                            여행지
                          </v-chip>
                        </div>

                        <div class="d-flex align-center gap-4"></div>
                      </div>
                    </v-list-item-subtitle>
                  </v-list-item>
                </v-list>

                <v-card-actions class="justify-center"> </v-card-actions>
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
