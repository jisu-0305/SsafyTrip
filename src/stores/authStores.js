import { ref, computed, onMounted } from 'vue'
import { defineStore } from 'pinia'
import authApi from '@/api/authApi'
import router from '@/router'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(null)
  const isLoggedIn = ref(false)
  const isAdmin = ref(false)
  const loginError = ref(null)
  const isLoading = ref(false)

  const setLoginState = (userData) => {
    user.value = userData
    isLoggedIn.value = true
    isAdmin.value = userData?.role === 'ADMIN'
  }

  const clearLoginState = () => {
    user.value = null
    isLoggedIn.value = false
    isAdmin.value = false
  }

  const login = async (credentials) => {
    try {
      isLoading.value = true
      loginError.value = null
      const response = await authApi.login(credentials)
      setLoginState(response.data)
      return { success: true }
    } catch (error) {
      loginError.value = '로그인에 실패했습니다.'
      return { success: false }
    } finally {
      isLoading.value = false
    }
  }

  const logout = async () => {
    try {
      await authApi.logout()
      clearLoginState()
      return { success: true }
    } catch (error) {
      console.error('로그아웃 실패:', error)
      return { success: false }
    }
  }

  const checkSession = async () => {
    try {
      const response = await authApi.checkSession()
      if (response.data.isValid) {
        setLoginState(response.data.user)
        return true
      } else {
        clearLoginState()
        return false
      }
    } catch (error) {
      clearLoginState()
      return false
    }
  }

  const handleAuthError = (error) => {
    if (error.response?.status === 401 || error.response?.status === 403) {
      clearLoginState()
      router.push('/login')
    }
    throw error
  }

  // 앱 시작 시 세션 체크
  onMounted(() => {
    checkSession()
  })

  return {
    user,
    isLoggedIn,
    isAdmin,
    loginError,
    isLoading,
    login,
    logout,
    checkSession,
    handleAuthError
  }
}, {
  persist: {
    key: 'auth-store',
    storage: sessionStorage,
    paths: ['user', 'isLoggedIn', 'isAdmin']
  }
})