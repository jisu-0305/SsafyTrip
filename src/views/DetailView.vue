<script setup>
import { ref, computed } from "vue";
import { useRoute, useRouter } from "vue-router";

// 라우터 초기화
const route = useRoute();
const router = useRouter();

// 게시물 데이터 (더미 데이터)
const articles = ref([
  { id: 1, title: "첫 번째 게시글", author: "user1", views: 10, date: "2024-11-15", content: "첫 번째 내용" },
  { id: 2, title: "두 번째 게시글", author: "user2", views: 20, date: "2024-11-16", content: "두 번째 내용" },
  { id: 3, title: "세 번째 게시글", author: "user3", views: 30, date: "2024-11-17", content: "세 번째 내용" },
]);

// 현재 게시물 가져오기
const article = computed(() => articles.value.find((a) => a.id === Number(route.params.id)));

// 게시물 삭제
const deleteArticle = () => {
  if (confirm("이 게시물을 삭제하시겠습니까?")) {
    articles.value = articles.value.filter((a) => a.id !== article.value.id);
    alert("삭제되었습니다.");
    router.push({ name: "board" }); // 삭제 후 게시판 목록으로 이동
  }
};

// 수정 페이지로 이동
const editArticle = () => {
  router.push({ name: "article-edit", params: { id: article.value.id } });
};
</script>

<template>
  <div class="container mt-5">
    <h1 class="text-center mb-4">{{ article?.title }}</h1>
    <div v-if="article">
      <p><strong>작성자:</strong> {{ article.author }}</p>
      <p><strong>작성일:</strong> {{ article.date }}</p>
      <p><strong>조회수:</strong> {{ article.views }}</p>
      <hr />
      <p>{{ article.content }}</p>
      <div class="text-end mt-3">
        <button class="btn btn-warning me-2" @click="editArticle">수정하기</button>
        <button class="btn btn-danger" @click="deleteArticle">삭제하기</button>
      </div>
    </div>
    <div v-else>
      <p>게시물을 찾을 수 없습니다.</p>
    </div>
  </div>
</template>

<style scoped>
.container {
  max-width: 800px;
  margin: 0 auto;
}
</style>
