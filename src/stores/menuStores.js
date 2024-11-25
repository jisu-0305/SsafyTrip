import { defineStore } from 'pinia';

export const useMenuStore = defineStore('menu', {
  state: () => ({
    menuItems: [
      { title: '관광지 조회', icon: 'mdi-map-search', route: { name: 'attract-search' } },
      { title: '여행 계획 세우기', icon: 'mdi-calendar-check', route: { name: 'travel-plan' } },
      { title: '여행 후기', icon: 'mdi-book-open-variant', route: null },
      { title: 'AI기능', icon: 'mdi-robot', route: null },
      { title: '공지사항', icon: 'mdi-bulletin-board', route: { name: 'notice' } },
    ],
    isLoggedIn: false,
    userInfo: null
  }),

  actions: {
    changeMenuState(state) {
      this.isLoggedIn = state;
    },
    setUserInfo(info) {
      this.userInfo = info;
    }
  },
  persist: {
    enabled: true,
    strategies: [
      {
        key: 'menu-store',
        storage: localStorage, // 또는 sessionStorage
        paths: ['isLoggedIn'] // 영속성을 적용할 필드만 지정
      }
    ]
  }
});
