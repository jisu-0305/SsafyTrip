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

// 수정 상태 관리
const editedTitle = ref(article.value?.title || "");
const editedContent = ref(article.value?.content || "");

// 수정 저장
const saveChanges = () => {
  if (!editedTitle.value || !editedContent.value) {
    alert("제목과 내용을 모두 입력하세요.");
    return;
  }

  const targetArticle = articles.value.find((a) => a.id === article.value.id);
  if (targetArticle) {
    targetArticle.title = editedTitle.value;
    targetArticle.content = editedContent.value;
    alert("수정이 완료되었습니다.");
    router.push({ name: "article-detail", params: { id: article.value.id } }); // 상세 페이지로 이동
  }
};
</script>

<template>
  <div class="container mt-5">
    <h1 class="text-center mb-4">게시물 수정</h1>
    <div v-if="article">
      <div class="mb-3">
        <label for="title" class="form-label">제목</label>
        <input
          type="text"
          id="title"
          class="form-control"
          v-model="editedTitle"
          placeholder="제목을 입력하세요"
        />
      </div>
      <div class="mb-3">
        <label for="content" class="form-label">내용</label>
        <textarea
          id="content"
          class="form-control"
          v-model="editedContent"
          rows="5"
          placeholder="내용을 입력하세요"
        ></textarea>
      </div>
      <div class="text-end mt-3">
        <button class="btn btn-primary" @click="saveChanges">저장</button>
        <button class="btn btn-secondary" @click="() => router.push({ name: 'article-detail', params: { id: article.id } })">
          취소
        </button>
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
