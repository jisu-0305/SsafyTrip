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

const handleMenuClick = (event) => {
  const allMenuItems = document.querySelectorAll(".menu-item");
  allMenuItems.forEach((item) => {
    item.classList.remove("selected");
  });
  event.currentTarget.classList.add("selected");
};
</script>

<template>
  <v-app>
    <!-- App Bar -->
    <v-app-bar app dense>
      <!-- 로고 (왼쪽) -->
      <v-img
        src="src/assets/logo.png"
        alt="Logo"
        max-width="150"
        class="logo"
      ></v-img>

      <!-- 중앙 텍스트 -->
      <v-spacer></v-spacer>
      <v-btn class="menu-item" @click="handleMenuClick">관광지 조회</v-btn>
      
      <router-link :to="{ name: 'board' }" class="nav-link">
        <v-btn class="menu-item" @click="handleMenuClick">여행 계획 세우기</v-btn>
      </router-link>
      
      <v-btn class="menu-item" @click="handleMenuClick">여행 후기</v-btn>
      
      <v-btn class="menu-item" @click="handleMenuClick">AI기능</v-btn>
      
      <v-btn class="menu-item" @click="handleMenuClick">공지사항</v-btn>
      
      <v-spacer></v-spacer>

      <!-- 오른쪽 버튼 -->
      <v-btn
        class="button-signup"
        style="background-color: black; color: white; border: 1px solid black;"
      >
        회원가입
      </v-btn>
      <v-btn
        outlined
        class="button-login"
        style="border: 1px solid black; color: black;"
      >
        로그인
      </v-btn>
    </v-app-bar>
  </v-app>
</template>

<style scoped>
/* 로고 스타일 */
.logo {
  height: 64px;
  margin-left: 50px;
}

/* 메뉴 스타일 */
.menu-item {
  font-family: "Roboto", sans-serif;
  font-size: 15px;
  line-height: 26px;
  color: black;
  transition: color 0.3s ease;
}

.menu-item.selected {
  color: #1890ff;
}

/* 버튼 스타일 */
.button-signup {
  font-family: "Roboto", sans-serif;
  font-size: 15px;
  line-height: 26px;
  border-radius: 2px;
  padding: 6px 12px;
  margin-right: 10px;
  margin-left: 50px;
}

.button-login {
  font-family: "Roboto", sans-serif;
  font-size: 15px;
  line-height: 26px;
  border-radius: 2px;
  padding: 6px 12px;
  margin-right: 50px;
}
</style>