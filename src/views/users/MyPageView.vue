<script setup>
import { ref } from 'vue';
import { storeToRefs } from 'pinia';
import PageHeader from '@/components/common/PageHeader.vue';
import NavigationMyPage from '@/components/mypage/NavigationMyPage.vue';
import ActivityMyPage from '@/components/mypage/ActivityMyPage.vue';
import EditMyPage from '@/components/mypage/EditMyPage.vue';
import DeleteMyPage from '@/components/mypage/DeleteMyPage.vue';
import { useLoadingStore } from '@/stores/loadingStore';

const currentCompFlag = ref(1);
const loadingStore = useLoadingStore();
const { isComponentLoading } = storeToRefs(loadingStore);

async function updateCurrentCompHanlder(params) {
  try {
    loadingStore.startLoading('mypage-content');
    currentCompFlag.value = params;
    await new Promise(resolve => setTimeout(resolve, 300));
  } finally {
    loadingStore.endLoading('mypage-content');
  }
}
</script>

<template>
  <v-container fluid class="page-container">
    <v-row justify="center">
      <v-col cols="12" class="content-wrapper">
        <div class="inner-content">
          <PageHeader title="마이페이지" icon="mdi-account-circle-outline" />
          <div class="content-area">
            <v-card class="mt-4 mypage-card">
              <v-row no-gutters style="min-height: 600px;">
                <!-- 네비게이션 영역 -->
                <v-col cols="4" style="background-color: #096DD9;">
                  <NavigationMyPage 
                    @updateFlag="updateCurrentCompHanlder" 
                    :currentFlag="currentCompFlag"
                  />
                </v-col>
                
                <!-- 컨텐츠 영역 -->
                <v-col cols="8">
                  <v-card flat class="content-card">
                    <template v-if="isComponentLoading('mypage-content')">
                      <div class="d-flex justify-center align-center" style="height: 600px">
                        <v-progress-circular
                          indeterminate
                          color="primary"
                          size="64"
                        ></v-progress-circular>
                      </div>
                    </template>
                    <template v-else>
                      <component 
                        :is="currentCompFlag === 1 ? ActivityMyPage : 
                             currentCompFlag === 2 ? EditMyPage : DeleteMyPage"
                      />
                    </template>
                  </v-card>
                </v-col>
              </v-row>
            </v-card>
          </div>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.page-wrapper {
  width: 100%;
}

.content-wrapper {
  margin-top: 12px;
}
</style>