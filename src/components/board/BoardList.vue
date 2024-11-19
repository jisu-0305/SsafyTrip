<script setup>
import { defineProps } from "vue";
import { useRouter } from "vue-router";

// props 정의
defineProps({
  articles: {
    type: Array,
    required: true,
  },
});

const router = useRouter();

// 상세 페이지로 이동
const moveToDetail = (id) => {
  router.push({ name: "article-detail", params: { id } }); // id로 이동
};
</script>

<template>
  <div>
    <table class="table table-hover">
      <thead class="table-light">
        <tr>
          <th>글번호</th>
          <th>제목</th>
          <th>작성자</th>
          <th>조회수</th>
          <th>작성일</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="article in articles" :key="article.id">
          <td>{{ article.id }}</td>
          <td>
            <a href="javascript:void(0)" @click="moveToDetail(article.id)">{{ article.title }}</a>
          </td>
          <td>{{ article.author }}</td>
          <td>{{ article.views }}</td>
          <td>{{ article.date }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped>
.table-hover {
  margin-top: 20px;
}
a {
  cursor: pointer;
  text-decoration: underline;
  color: blue;
}
</style>
