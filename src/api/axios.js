import axios from "axios";

// const { VITE_VUE_API_URL } = import.meta.env; refactoring 할 때.

export default axios.create({
  baseURL: "https://inssaroute.shop/api",
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true,
});
