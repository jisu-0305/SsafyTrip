<script setup>
import { useMenuStore } from "@/stores/menuStores";
import { storeToRefs } from "pinia";
import { useRouter } from 'vue-router';
import { ref, computed } from 'vue';

const menuStore = useMenuStore();
const router = useRouter();
const drawer = ref(false);

const { menuItems } = storeToRefs(menuStore);

// 사용자 이니셜 계산
const userInitial = computed(() => {
  return menuStore.userInfo?.name?.[0] || 'U';
});

const handleMenuClick = (route) => {
  if (route) router.push(route);
  drawer.value = false;
};

const goToLogin = () => {
  router.push({ name: 'user-login' });
};

const goToSignup = () => {
  router.push({ name: 'user-join' });
};

const goToMyPage = () => {
  router.push({ name: 'user-mypage' });
};

</script>

<template>
  <div class="nav-wrapper">
    <v-app-bar
      color="white"
      height="82"
      class="nav-container"
    >
      <!-- 모바일 메뉴 토글 버튼 -->
      <v-app-bar-nav-icon
        @click="drawer = !drawer"
        class="hidden-md-and-up"
      ></v-app-bar-nav-icon>

      <!-- 로고 -->
      <router-link :to="{ name: 'main' }" class="logo-link">
        <v-img
          src="src/assets/logo.png"
          alt="Logo"
          :width="200"
          :height="64.13"
          :aspect-ratio="200/64.13"
          contain
          class="logo"
        ></v-img>
      </router-link>

      <!-- 데스크톱 메뉴 -->
      <v-spacer></v-spacer>
      <div class="hidden-sm-and-down menu-container">
        <v-btn
          v-for="item in menuItems"
          :key="item.title"
          variant="text"
          class="menu-item"
          @click="handleMenuClick(item.route)"
        >
          {{ item.title }}
        </v-btn>
      </div>
      <v-spacer></v-spacer>

      <!-- 로그인/회원가입 버튼 -->
      <div class="auth-buttons hidden-sm-and-down">
        <template v-if="!menuStore.isLoggedIn">
          <v-btn
            class="signup-btn"
            @click="goToSignup"
          >
            회원가입
          </v-btn>
          <v-btn
            class="login-btn"
            variant="outlined"
            @click="goToLogin"
          >
            로그인
          </v-btn>
        </template>
        <template v-else>
          <v-btn
            class="mypage-btn"
            @click="goToMyPage"
          >
          마이페이지
            <!-- <v-icon size="14">{{ userInitial }}</v-icon> -->
          </v-btn>
        </template>
      </div>
    </v-app-bar>

    <!-- 모바일 네비게이션 드로어 -->
    <v-navigation-drawer v-model="drawer" temporary location="left">
      <v-list>
        <v-list-item
          v-for="item in menuItems"
          :key="item.title"
          @click="handleMenuClick(item.route)"
        >
          <template v-slot:prepend>
            <v-icon>{{ item.icon }}</v-icon>
          </template>
          <v-list-item-title>{{ item.title }}</v-list-item-title>
        </v-list-item>

        <v-divider class="my-2"></v-divider>

        <!-- 모바일에서만 보이는 로그인/회원가입 버튼 -->
        <template v-if="!menuStore.isLoggedIn">
        <v-list-item @click="goToLogin">
          <template v-slot:prepend>
            <v-icon>mdi-login</v-icon>
          </template>
          <v-list-item-title>로그인</v-list-item-title>
        </v-list-item>

        <v-list-item @click="goToSignup">
          <template v-slot:prepend>
            <v-icon>mdi-account-plus</v-icon>
          </template>
          <v-list-item-title>회원가입</v-list-item-title>
        </v-list-item>
      </template>
      <template v-else>
        <v-list-item @click="goToMyPage">
          <template v-slot:prepend>
            <v-icon>mdi-account-plus</v-icon>
          </template>
          <v-list-item-title>마이페이지</v-list-item-title>
        </v-list-item>
      </template>
      </v-list>
    </v-navigation-drawer>
  </div>
</template>

<style scoped>
.nav-wrapper {
  position: relative;
  width: 100%;
}

.nav-container {
  position: absolute;
  width: 1440px;
  height: 82px;
  left: -18px;
  top: 14px;
  background: white;
  border-bottom: 1px solid rgba(0, 0, 0, 0.3);
}

.logo-link {
  text-decoration: none;
  display: flex;
  align-items: center;
}

.logo {
  margin-left: 40px;
  transition: all 0.3s ease;
}

.menu-container {
  display: flex;
  align-items: center;
  gap: 32px;
  transition: gap 0.3s ease;
}

.menu-item {
  font-family: 'Roboto', sans-serif;
  font-style: normal;
  font-weight: 400;
  font-size: 14px;
  line-height: 22px;
  color: rgba(0, 0, 0, 0.85);
  text-transform: none;
  letter-spacing: normal;
  white-space: nowrap;
}

.auth-buttons {
  display: flex;
  gap: 8px;
  margin-right: 40px;
  white-space: nowrap;
}

.signup-btn, .login-btn {
  height: 32px;
  padding: 4px 15px;
  font-family: 'Roboto', sans-serif;
  font-size: 14px;
  line-height: 22px;
  border-radius: 2px;
  text-transform: none;
  letter-spacing: normal;
}

.signup-btn {
  background: #262626;
  color: white;
  border: 1px solid #262626;
  box-shadow: 0px 2px 0px rgba(0, 0, 0, 0.043);
}

.login-btn {
  background: white;
  color: rgba(0, 0, 0, 0.85);
  border: 1px solid #D9D9D9;
  box-shadow: 0px 2px 0px rgba(0, 0, 0, 0.016);
}

@media (max-width: 1400px) {
  .nav-container {
    width: 100%;
    left: 0;
  }
  
  .menu-container {
    gap: 24px;
  }
  
  .auth-buttons {
    margin-right: 40px;
  }
}

@media (max-width: 1200px) {
  .menu-container {
    gap: 16px;
  }
  
  .auth-buttons {
    margin-right: 20px;
  }
}

@media (max-width: 1100px) {
  .menu-container {
    gap: 8px;
  }
  
  .menu-item {
    padding: 0 8px;
  }
}

@media (max-width: 960px) {
  .nav-container {
    top: 0;
  }

  .menu-container, .auth-buttons {
    display: none;
  }
  
  .logo-link {
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
  }
  
  .logo {
    margin-left: 0;
  }
}

.v-navigation-drawer {
  .v-list-item {
    font-family: 'Roboto', sans-serif;
    font-size: 14px;
    line-height: 22px;
    color: rgba(0, 0, 0, 0.85);
  }
}
</style>