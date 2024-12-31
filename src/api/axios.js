import axios from "axios";
import { useAuthStore } from "@/stores/authStores";
import router from "@/router";

// const { VITE_VUE_API_URL } = import.meta.env; refactoring 할 때.

const myaxios = axios.create({
  baseURL: "https://inssaroute.shop/api",
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true,
});

myaxios.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      const authStore = useAuthStore();
      authStore.clearLoginState();
      router.push('/login');
    }
    return Promise.reject(error);
  }
);

export default myaxios;
