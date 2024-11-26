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
      path: "/attract/search",
      name: "attract-search",
      component: () => import("@/views/attracts/SearchMapView.vue"),
    },
    {
      path: "/plan",
      name: "travel-plan",
      component: () => import("@/views/plan/AttractionPlan.vue"),
    },
    {
      path: "/notice",
      name: "notice",
      component: () => import("@/views/notices/NoticeListView.vue"),
    },
    {
      path: "/notice/write",
      name: "notice-write",
      component: () => import("@/views/notices/NoticeWriteView.vue"),
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
      component: () => import("@/views/notices/NoticeEditView.vue"),
      props: true,
    },
    {
      path: '/attraction/:id',
      name: 'AttractionDetail',
      component: () => import('@/views/attracts/AttractDetailView.vue')
    },
    {
      path: "/question",
      name: "question",
      component: () => import("@/views/questions/QuestionListView.vue"),
    },
    {
      path: "/question/write",
      name: "question-write",
      component: () => import("@/views/questions/QuestionWriteView.vue"),
    },
    {
      path: "/question/:id",
      name: "question-detail",
      component: () => import("@/views/questions/QuestionDetailView.vue"),
      props: true
    },
    {
      path: "/question/:id/edit",
      name: "question-edit",
      component: () => import("@/views/questions/QuestionEditView.vue"),
      props: true,
    },
    {
      path: "/favorites",
      name: "favorites",
      component: () => import("@/components/mypage/FavoriteMyPage.vue"),
    },
  ],
});

export default router;
