<script setup>
import { ref, computed } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();

const articles = ref([
  { id: 1, title: "첫 번째 게시글", author: "user1", views: 10, date: "2024-11-15", content: "첫 번째 내용" },
  { id: 2, title: "두 번째 게시글", author: "user2", views: 20, date: "2024-11-16", content: "두 번째 내용" },
  { id: 3, title: "세 번째 게시글", author: "user3", views: 30, date: "2024-11-17", content: "세 번째 내용" },
]);

const article = computed(() => articles.value.find((a) => a.id === Number(route.params.id)));

const deleteArticle = () => {
  if (confirm("이 게시물을 삭제하시겠습니까?")) {
    articles.value = articles.value.filter((a) => a.id !== article.value.id);
    alert("삭제되었습니다.");
    router.push({ name: "board" });
  }
};

const editArticle = () => {
  router.push({ name: "article-edit", params: { id: article.value.id } });
};
</script>

<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" md="8">
        <v-card class="mt-5">
          <v-sheet color="grey-lighten-4" class="pa-4 d-flex align-center">
            <span class="text-h5">{{ article?.title }}</span>
            <v-spacer></v-spacer>
            <span class="text-body-2">{{ article?.date }}</span>
          </v-sheet>

          <v-card-text v-if="article">
            <p class="text-body-1 px-4">{{ article.content }}</p>

            <v-card-actions class="justify-end mt-4">
              <v-btn
                variant="tonal"
                color="warning"
                class="me-2"
                @click="editArticle"
                prepend-icon="mdi-pencil"
              >
                수정하기
              </v-btn>
              <v-btn
                variant="tonal" 
                color="error"
                @click="deleteArticle"
                prepend-icon="mdi-delete"
              >
                삭제하기
              </v-btn>
            </v-card-actions>
          </v-card-text>

          <v-card-text v-else>
            <v-alert
              type="warning"
              text="게시물을 찾을 수 없습니다."
            ></v-alert>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>