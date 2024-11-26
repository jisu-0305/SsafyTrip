import { defineStore } from 'pinia';

export const useMenuStore = defineStore('menu', {
  state: () => ({
    menuItems: [
      { title: '관광지 조회', icon: 'mdi-map-search', route: { name: 'attract-search' } },
      { title: '여행 일정 만들기', icon: 'mdi-calendar-check', route: { name: 'travel-plan' } },
      { title: '여행 후기', icon: 'mdi-book-open-variant', route: null },
      { title: '공지사항', icon: 'mdi-bulletin-board', route: { name: 'notice' } },
    ],
    userInfo: null
  }),

  actions: {
    setUserInfo(info) {
      this.userInfo = info;
    }
  }
});
