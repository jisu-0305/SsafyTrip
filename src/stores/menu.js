import { ref } from "vue";
import { defineStore } from "pinia";

export const useMenuStore = defineStore("menuStore", () => {
  // show를 통해 [회원가입, 로그인], [오늘할일, 내정보, 로그아웃]을 토글처럼 보여주는 것이 가능
  const menuList = ref([
    { name: "회원가입", show: true, routeName: "user-join" },
    { name: "로그인", show: true, routeName: "user-login" },
    { name: "마이 페이지", show: false, routeName: "user-mypage" },
    { name: "로그아웃", show: false, routeName: "user-logout" },
  ]);

  const changeMenuState = () => {
    menuList.value = menuList.value.map((item) => ({
      ...item,
      show: !item.show,
    }));
  };

  return {
    menuList,
    changeMenuState,
  };
});
