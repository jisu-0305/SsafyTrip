import { ref, computed } from "vue";
import { defineStore } from "pinia";
import authApi from "@/api/authApi";
import router from "@/router";

export const useAuthStore = defineStore(
  "auth",
  () => {
    const user = ref(null);
    const isLoggedIn = ref(false);
    const isAdmin = ref(false);
    const loginError = ref(null);
    const isLoading = ref(false);

    const setLoginState = (userData) => {
      user.value = userData;
      isLoggedIn.value = true;
      isAdmin.value = userData?.role === "ROLE_ADMIN";
    };

    const clearLoginState = () => {
      user.value = null;
      isLoggedIn.value = false;
      isAdmin.value = false;
    };

    const login = async (credentials) => {
      try {
        isLoading.value = true;
        loginError.value = null;
        const response = await authApi.login(credentials);
        setLoginState(response.data);
        return { success: true };
      } catch (error) {
        loginError.value = error.response?.data?.message || "로그인에 실패했습니다.";
        return { success: false };
      } finally {
        isLoading.value = false;
      }
    };

    const logout = async () => {
      try {
        await authApi.logout();
        clearLoginState();
        return { success: true };
      } catch (error) {
        const errorMessage = error.response?.data?.message || "로그아웃 중 오류가 발생했습니다.";
        alert(errorMessage);
        return { success: false };
      }
    };

    const checkSession = async () => {
      if (!isLoggedIn.value) return;
      
      try {
        const response = await authApi.checkSession();
        if (response.status >= 200 && response.status < 300) {
          return;
        }
        throw new Error("세션이 만료되었습니다.");
      } catch (error) {
        const errorMessage = error.response?.data?.message || "세션이 만료되었습니다. 다시 로그인해주세요.";
        alert(errorMessage);
        clearLoginState();
      }
    };

    const handleAuthError = (error) => {
      const errorMessage = error.response?.data?.message || "인증 오류가 발생했습니다.";
      alert(errorMessage);
      if (error.response?.status === 401 || error.response?.status === 403) {
        clearLoginState();
        router.push("/login");
      }
      throw error;
    };

    return {
      user,
      isLoggedIn,
      isAdmin,
      loginError,
      isLoading,
      login,
      logout,
      checkSession,
      handleAuthError,
    };
  },
  {
    persist: {
      key: "auth-store",
      storage: sessionStorage,
      paths: ["user", "isLoggedIn", "isAdmin"],
    },
  }
);
