<script setup>
import { useMenuStore } from "@/stores/menuStores";
import { storeToRefs } from "pinia";
import { useRouter } from 'vue-router';
import { ref, computed } from 'vue';
import { useAuthStore } from '@/stores/authStores'

const menuStore = useMenuStore();
const router = useRouter();
const drawer = ref(false);
const authStore = useAuthStore();

const { menuItems } = storeToRefs(menuStore);

// 사용자 이니셜 계산 - authStore에서 가져오도록 수정
const userInitial = computed(() => {
  return authStore.user?.name?.[0] || 'U';
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

const handleLogout = async () => {
  const { success } = await authStore.logout();
  if (success) {
    router.push({ name: 'main' });
  } else {
    alert('로그아웃 중 오류가 발생했습니다.');
  }
};
</script>

<template>
  <div class="nav-wrapper">
    <v-app-bar
      color="white"
      height="82"
      class="nav-container"
      elevation="0"
    >
      <!-- 모바일 메뉴 토글 버튼 -->
      <v-app-bar-nav-icon @click="drawer = !drawer" class="hidden-md-and-up"></v-app-bar-nav-icon>

      <!-- 로고 -->
      <router-link :to="{ name: 'main' }" class="logo-link">
        <v-img
          src="/img/logo2.png"
          alt="Logo"
          :width="180"
          :height="64.13"
          :aspect-ratio="200/64.13"
          contain
          class="logo"
        ></v-img>
      </router-link>

      <!-- 데스크톱 메뉴 -->
      <v-spacer></v-spacer>
      <div class="hidden-sm-and-down menu-container">
        <v-btn v-for="item in menuItems" :key="item.title" variant="text" :ripple="false" class="menu-item"
          :active-class="'active-menu-item'" :class="{ 'v-btn--active': $route.name === item.route }"
          @click="handleMenuClick(item.route)" density="comfortable" min-height="82px">
          <span class="menu-text">{{ item.title }}</span>
        </v-btn>
      </div>
      <v-spacer></v-spacer>

      <!-- 로그인/회원가입/마이페이지 버튼 -->
      <div class="auth-buttons hidden-sm-and-down">
        <template v-if="!authStore.isLoggedIn">
          <v-btn class="signup-btn" @click="goToSignup">
            회원가입
          </v-btn>
          <v-btn class="login-btn" variant="outlined" @click="goToLogin">
            로그인
          </v-btn>
        </template>
        <template v-else>
          <v-btn class="mypage-btn" @click="goToMyPage">
            마이페이지
          </v-btn>
          <v-btn class="logout-btn" variant="outlined" @click="handleLogout">
            로그아웃
          </v-btn>
        </template>
      </div>
    </v-app-bar>

    <!-- 모바일 네비게이션 드로어 -->
    <v-navigation-drawer v-model="drawer" temporary location="left">
      <v-list>
        <v-list-item v-for="item in menuItems" :key="item.title" @click="handleMenuClick(item.route)">
          <template v-slot:prepend>
            <v-icon>{{ item.icon }}</v-icon>
          </template>
          <v-list-item-title>{{ item.title }}</v-list-item-title>
        </v-list-item>

        <v-divider class="my-2"></v-divider>

        <!-- 모바일에서만 보이는 로그인/회원가입 버튼 -->
        <template v-if="!authStore.isLoggedIn">
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
          <v-list-item @click="handleLogout">
            <template v-slot:prepend>
              <v-icon>mdi-logout</v-icon>
            </template>
            <v-list-item-title>로그아웃</v-list-item-title>
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
  composes: content-wrapper-wide from '@/assets/styles/base.css';
  height: var(--navbar-height);
  background: var(--color-background);
  border-bottom: 1px solid var(--color-border);
}


.logo-link {
  text-decoration: none;
  display: flex;
  align-items: center;
  padding-left: var(--spacing-lg);
}



.menu-container {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--spacing-lg);
  height: 100%;
}

.menu-item {
  composes: hover-effect from '@/assets/styles/common.css';
  height: 100%;
  background: transparent !important;
}

.menu-text {
  position: relative;
  padding: var(--spacing-xs) 0;
  color: var(--color-text);
  font-size: var(--menu-font-size);
  font-weight: var(--menu-font-weight);
}

/* 밑줄 효과 */
.menu-text::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 0;
  height: 2px;
  background-color: #1890FF;
  transition: width 0.3s ease;
}

/* hover 시 텍스트 색상 변경 및 밑줄 표시 */
.menu-item:hover .menu-text {
  color: #1890FF;
}

.menu-item:hover .menu-text::after {
  width: 100%;
}

/* active 상태 스타일 */
.active-menu-item .menu-text,
.v-btn--active .menu-text {
  color: var(--color-primary);
}

.active-menu-item .menu-text::after,
.v-btn--active .menu-text::after {
  width: 100%;
}

.auth-buttons {
  display: flex;
  gap: var(--spacing-xs);
  margin-right: var(--spacing-lg);
  white-space: nowrap;
}

/* 버튼 공통 스타일 */
.auth-btn {
  height: var(--button-height);
  padding: var(--spacing-xs) var(--spacing-md);
  font-family: var(--font-family);
  border-radius: var(--border-radius-sm);
  text-transform: none;
  letter-spacing: normal;
}

.signup-btn {
  composes: auth-btn;
  background: var(--color-primary-dark);
  color: var(--color-background);
}

.login-btn,
.logout-btn {
  composes: auth-btn;
  background: var(--color-background);
  color: var(--color-text);
  border: 1px solid var(--color-border);
}


@media (max-width: 1200px) {
  .menu-container {
    gap: var(--spacing-md);
  }

  .auth-buttons {
    margin-right: var(--spacing-md);
  }
}

@media (max-width: 960px) {
  .nav-container {
    padding: 0 var(--spacing-sm);
  }

  .menu-container,
  .auth-buttons {
    display: none;
  }

  .logo-link {
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
    padding-left: 0;
  }
}

/* 드로어 스타일 */
:deep(.v-navigation-drawer) {
  .v-list-item {
    font-size: var(--font-size-sm);
    line-height: var(--line-height-base);
    color: var(--color-text);
  }
}
</style>