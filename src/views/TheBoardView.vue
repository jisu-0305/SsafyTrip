<script setup>
import { ref } from "vue";
import BoardList from "@/components/board/BoardList.vue";

// 게시물 데이터 상태 관리
const articles = ref([
  { id: 1, title: "첫 번째 게시글", author: "user1", views: 10, date: "2024-11-15" },
  { id: 2, title: "두 번째 게시글", author: "user2", views: 20, date: "2024-11-16" },
  { id: 3, title: "세 번째 게시글", author: "user3", views: 30, date: "2024-11-17" },
]);

// 새로운 게시글 추가
const addArticle = (newArticle) => {
  articles.value.unshift(newArticle); // 새 게시글을 목록의 맨 앞에 추가
};
</script>

<template>
  <div class="container mt-5">
    <h1 class="text-center mb-4">게시판</h1>
    <!-- 데이터 전달 -->
    <BoardList :articles="articles" @save-article="addArticle" />
  </div>
</template>

<style scoped>
.container {
  max-width: 800px;
  margin: 0 auto;
}
</style>
