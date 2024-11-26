<script setup>
import { onMounted } from "vue";
import { storeToRefs } from 'pinia';
import { useNoticeStore } from "@/stores/noticeStores";
import { useRouter } from 'vue-router';
import PageHeader from "@/components/common/PageHeader.vue";
import BoardList from "@/components/board/BoardList.vue";
import SearchResultInfo from "@/components/common/SearchResultInfo.vue";
import { useAuthStore } from '@/stores/authStores'

const router = useRouter();
const noticeStore = useNoticeStore();
const { notices, currentPage, totalPages, totalElements, searchKeyword } = storeToRefs(noticeStore);
const authStore = useAuthStore()

onMounted(() => {
  noticeStore.fetchNotices();
});

const handlePageChange = (page) => {
  noticeStore.fetchNotices(page, 10, searchKeyword.value);
};

const handleSearch = () => {
  noticeStore.fetchNotices(1, 10, searchKeyword.value);
};

const goToWrite = () => {
  router.push({ name: 'notice-write' });
};

const handleRowClick = (article) => {
  router.push({ 
    name: 'notice-detail', 
    params: { id: article.noticeId.toString() } 
  });
};

const columns = [
  { 
    key: 'noticeId', 
    label: '번호',
    style: 'width: 100px'
  },
  { 
    key: 'title', 
    label: '제목' 
  },
  { 
    key: 'createdAt', 
    label: '작성일',
    style: 'width: 150px'
  }
];
</script>

<template>
  <v-container fluid class="page-container">
    <v-row justify="center">
      <v-col cols="12" class="content-wrapper">
        <div class="inner-content">
          <PageHeader title="공지사항" icon="mdi-clipboard-text" />
          <div class="content-area">
            
            <!-- 검색 영역 -->
            <div class="search-area mb-6">
              <v-row>
                <v-col :cols="authStore.isAdmin ? 8 : 10">
                  <v-text-field
                    v-model="searchKeyword"
                    label="검색어"
                    @keyup.enter="handleSearch"
                    density="compact"
                    variant="outlined"
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
                <v-col v-if="authStore.isAdmin" cols="2">
                  <v-btn
                    color="blue-darken-3"
                    @click="goToWrite"
                    prepend-icon="mdi-plus"
                    block
                    height="40"
                    :title="!authStore.isAdmin ? '관리자만 작성할 수 있습니다' : ''"
                  >
                    작성
                  </v-btn>
                </v-col>
              </v-row>
            </div>

            <!-- 게시글 리스트 영역 -->
            <div class="list-area">
              <SearchResultInfo :total-count="totalElements" />
              <BoardList 
                :articles="notices" 
                :columns="columns"
                :on-row-click="handleRowClick"
              />
              
              <!-- 페이지네이션 -->
              <div class="text-center mt-6">
                <v-pagination
                  v-model="currentPage"
                  :length="totalPages"
                  @update:model-value="handlePageChange"
                ></v-pagination>
              </div>
            </div>
          </div>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.search-area {
  margin-bottom: 32px;
}

.list-area {
  margin-top: 16px;
}
</style>