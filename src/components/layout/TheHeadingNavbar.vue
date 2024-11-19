<script setup>
import { useMenuStore } from "@/stores/menu";
import { storeToRefs } from "pinia";

const menuStore = useMenuStore();

// 반응형을 유지하면서 스토어에서 속성을 추출하려면, storeToRefs()를 사용
const { menuList } = storeToRefs(menuStore);
const { changeMenuState } = menuStore;

const getRoute = (routeName) => {
  switch (routeName) {
    case "user-login":
      return { name: "user-login" };
    case "user-join":
      return { name: "user-join" };
    case "user-mypage":
      return { name: "user-mypage" };
    case "user-logout":
      return "/"; // 로그아웃은 기본 경로
    default:
      return "/";
  }
};

const logout = () => {
  changeMenuState();
};
</script>

<template>
  <nav class="navbar navbar-expand-lg bg-body-tertiary sticky-top">
    <div class="container-fluid">
      <router-link :to="{ name: 'main' }" class="navbar-brand">
        <img
          src="@/assets/enjoyTrip.png"
          class="rounded mx-auto d-block"
          alt="..."
          width="50px"
        />
      </router-link>

      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarScroll"
        aria-controls="navbarScroll"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarScroll">
        <ul
          class="navbar-nav me-auto my-2 my-lg-0 navbar-nav-scroll"
          style="--bs-scroll-height: 100px"
        >
          <li class="nav-item">
            <router-link :to="{ name: 'SearchMap' }" class="nav-link">
              관광지 정보
            </router-link>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">여행 계획(일정코스)</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">AI 기능</a>
          </li>
          <li class="nav-item">
              <router-link :to="{ name: 'board' }" class="nav-link">
              게시판
            </router-link>
          </li>
        </ul>

        <ul
          class="navbar-nav ms-auto my-2 my-lg-0 navbar-nav-scroll"
          style="--bs-scroll-height: 100px"
        >
          <template v-for="menu in menuList" :key="menu.routeName">
            <li v-if="menu.show" class="nav-item">
              <router-link
                :to="getRoute(menu.routeName)"
                @click.prevent="
                  menu.routeName === 'user-logout' ? logout() : null
                "
                class="nav-link"
              >
                {{ menu.name }}
              </router-link>
            </li>
          </template>
        </ul>
      </div>
    </div>
  </nav>
</template>

<style scoped></style>
