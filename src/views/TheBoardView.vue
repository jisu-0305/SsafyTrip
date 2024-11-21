<script setup>
import { ref } from "vue";
import BoardList from "@/components/board/BoardList.vue";
import SearchResultInfo from "@/components/common/SearchResultInfo.vue";

const articles = ref([
  { id: 1, title: "첫 번째 게시글", author: "user1", views: 10, date: "2024-11-15" },
  { id: 2, title: "두 번째 게시글", author: "user2", views: 20, date: "2024-11-16" },
  { id: 3, title: "세 번째 게시글", author: "user3", views: 30, date: "2024-11-17" },
]);

const addArticle = (newArticle) => {
  articles.value.unshift(newArticle);
};
</script>

<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" md="8">
        <v-card class="mt-5">
          <v-card-title class="text-center text-h4 py-4">
            게시판
          </v-card-title>
          <v-card-text>
            <SearchResultInfo :total-count="articles.length" />
            <BoardList :articles="articles" @save-article="addArticle" />
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>