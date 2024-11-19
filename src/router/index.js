import { createRouter, createWebHistory } from "vue-router";
import TheMainView from "../views/TheMainView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),

  routes: [
    {
      path: "/",
      name: "main",
      component: TheMainView,
    },
    {
      path: "/login",
      name: "user-login",
      component: () => import("@/views/users/UserLogin.vue"),
    },
    {
      path: "/join",
      name: "user-join",
      component: () => import("@/views/users/UserRegister.vue"),
    },
    {
      path: "/mypage",
      name: "user-mypage",
      component: () => import("@/views/users/UserMyPage.vue"),
    },
    {
      path: "/serch-map", // 여행지 맵을 표시하는 경로
      name: "SearchMap", // 라우트 이름
      component: () => import("@/views/SerchMapView.vue"),
    },
    {
      path: "/board",
      name: "board",
      component: () => import("@/views/TheBoardView.vue"),
    },
    {
      path: "/board/write",
      name: "article-write",
      component: () => import("@/views/WriteArticleView.vue"),
    },
    {
      path: "/board/:id",
      name: "article-detail",
      component: () => import("@/views/DetailView.vue"),
      props: true
    },
    {
      path: "/board/:id/edit",
      name: "article-edit",
      component: () => import("@/views/EditArticleView.vue"), // 수정 페이지 컴포넌트
      props: true,
    }
  ],
});

export default router;
