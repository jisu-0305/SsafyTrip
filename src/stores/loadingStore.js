import { defineStore } from 'pinia'

export const useLoadingStore = defineStore('loading', {
  state: () => ({
    isLoading: false,
    componentLoading: {}  // 컴포넌트별 로딩 상태
  }),
  
  actions: {
    startLoading(componentId = 'global') {
      if (componentId === 'global') {
        this.isLoading = true
      } else {
        this.componentLoading[componentId] = true
      }
    },
    
    endLoading(componentId = 'global') {
      if (componentId === 'global') {
        this.isLoading = false
      } else {
        this.componentLoading[componentId] = false
      }
    }
  },
  
  getters: {
    isComponentLoading: (state) => (componentId) => {
      return state.componentLoading[componentId] || false
    }
  }
}) 