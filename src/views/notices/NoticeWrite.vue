<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";

// Vue의 emit 메서드 선언
const emit = defineEmits(["save-article"]);

const title = ref("");
const content = ref("");
const router = useRouter();

const saveArticle = () => {
  if (!title.value || !content.value) {
    alert("제목과 내용을 모두 입력해주세요!");
    return;
  }

 //emit을 통한 데이터 전달
  const newArticle = {
    id: Date.now(), // 임시 고유 ID
    title: title.value,
    author: "현재 사용자", 
    views: 0,
    date: new Date().toISOString().slice(0, 10),
  };
  emit("save-article", newArticle); 
  router.push({ name: "notice" }); 
};
</script>

<template>
  <div class="container mt-5">
    <h1 class="text-center mb-4">글쓰기</h1>
    <div class="mb-3">
      <label for="title" class="form-label">제목</label>
      <input
        type="text"
        id="title"
        class="form-control"
        v-model="title"
        placeholder="제목을 입력하세요"
      />
    </div>
    <div class="mb-3">
      <label for="content" class="form-label">내용</label>
      <textarea
        id="content"
        class="form-control"
        v-model="content"
        rows="5"
        placeholder="내용을 입력하세요"
      ></textarea>
    </div>
    <div class="text-end">
      <button class="btn btn-success" @click="saveArticle">저장</button>
    </div>
  </div>
</template>

<style scoped>
.container {
  max-width: 800px;
  margin: 0 auto;
}
</style>
