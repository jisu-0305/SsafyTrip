<script setup>
import { ref } from "vue";
import { useMenuStore } from "@/stores/menu";
import { useRouter } from "vue-router";

const { changeMenuState } = useMenuStore();
const router = useRouter();

const form = ref({
  username: "",
  password: "",
  rememberMe: false,
});

const login = () => {
  changeMenuState();
  router.push("/");
};
</script>

<template>
  <div class="login-container">
    <h2>로그인</h2>

    <form @submit.prevent="handleLogin">
      <!-- 아이디 -->
      <div class="mb-3">
        <label for="username" class="form-label">아이디</label>
        <input
          type="text"
          class="form-control"
          id="username"
          v-model="form.username"
          placeholder="아이디를 입력하세요"
          required
        />
      </div>

      <!-- 비밀번호 -->
      <div class="mb-3">
        <label for="password" class="form-label">비밀번호</label>
        <input
          type="password"
          class="form-control"
          id="password"
          v-model="form.password"
          placeholder="비밀번호를 입력하세요"
          required
        />
      </div>

      <!-- 아이디 저장 -->
      <div class="mb-3 form-check">
        <input
          type="checkbox"
          class="form-check-input"
          id="rememberMe"
          v-model="form.rememberMe"
        />
        <label class="form-check-label" for="rememberMe">아이디 저장</label>
      </div>

      <!-- 로그인 버튼 -->
      <button type="submit" class="btn btn-primary" @click="login">
        로그인
      </button>
    </form>

    <!-- 로그인 처리 상태 (선택 사항) -->
    <div v-if="errorMessage" class="alert alert-danger mt-3">
      {{ errorMessage }}
    </div>
  </div>
</template>
<style scoped></style>
