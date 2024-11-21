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
      component: () => import("@/views/auth/AuthLoginView.vue"),
    },
    {
      path: "/join",
      name: "user-join",
      component: () => import("@/views/auth/AuthRegisterView.vue"),
    },
    {
      path: "/mypage",
      name: "user-mypage",
      component: () => import("@/views/users/MyPageView.vue"),
    },
    {
      path: "/attract/search", // 여행지 맵을 표시하는 경로
      name: "attract-search", // 라우트 이름
      component: () => import("@/views/attracts/SerchMapView.vue"),
    },
    {
      path: "/notice",
      name: "notice",
      component: () => import("@/views/notices/NoticeListView.vue"),
    },
    {
      path: "/notice/write",
      name: "notice-write",
      component: () => import("@/views/notices/NoticeWrite.vue"),
    },
    {
      path: "/notice/:id",
      name: "notice-detail",
      component: () => import("@/views/notices/NoticeDetailView.vue"),
      props: true
    },
    {
      path: "/notice/:id/edit",
      name: "notice-edit",
      component: () => import("@/views/notices/NoticeEditView.vue"), // 수정 페이지 컴포넌트
      props: true,
    }
  ],
});

export default router;
