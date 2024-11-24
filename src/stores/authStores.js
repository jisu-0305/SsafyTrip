import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import authApi from '@/api/authApi'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(null)
  const loginError = ref('')
  const isLoading = ref(false)
  const loginAttempts = ref(parseInt(sessionStorage.getItem('loginAttempts') || '0'))
  const MAX_LOGIN_ATTEMPTS = 5

  // Computed
  const isAdmin = computed(() => user.value?.role === 'ROLE_ADMIN')
  const isLoggedIn = computed(() => !!user.value)

  // Actions
  const login = async (credentials) => {
    if (loginAttempts.value >= MAX_LOGIN_ATTEMPTS) {
      loginError.value = '로그인 시도 횟수를 초과했습니다. 잠시 후 다시 시도해주세요.'
      return { success: false }
    }

    try {
      isLoading.value = true
      loginError.value = ''
      
      const response = await authApi.login(credentials)
      user.value = response.data
      loginAttempts.value = 0
      sessionStorage.removeItem('loginAttempts')
      
      // 로그인 성공 시 세션 체크 시작
      startSessionCheck()
      return { success: true }

    } catch (error) {
      loginAttempts.value++
      sessionStorage.setItem('loginAttempts', loginAttempts.value.toString())
      
      if (error.response) {
        switch (error.response.status) {
          case 401:
            loginError.value = '아이디 또는 비밀번호가 일치하지 않습니다.'
            break
          default:
            loginError.value = '로그인 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.'
        }
      } else {
        loginError.value = '서버와의 연결이 원활하지 않습니다.'
      }
      
      return { success: false }
    } finally {
      isLoading.value = false
    }
  }

  const logout = async () => {
    try {
      await authApi.logout()
      user.value = null
      stopSessionCheck()
      return { success: true }
    } catch (error) {
      console.error('로그아웃 실패:', error)
      return { success: false }
    }
  }

  // 세션 체크 관련
  let sessionCheckInterval = null

  const startSessionCheck = () => {
    // 5분마다 세션 체크
    sessionCheckInterval = setInterval(async () => {
      try {
        const response = await authApi.checkSession()
        if (!response.data.isValid) {
          await logout()
        }
      } catch (error) {
        await logout()
      }
    }, 5 * 60 * 1000)
  }

  const stopSessionCheck = () => {
    if (sessionCheckInterval) {
      clearInterval(sessionCheckInterval)
      sessionCheckInterval = null
    }
  }

  return {
    user,
    loginError,
    isLoading,
    isAdmin,
    isLoggedIn,
    login,
    logout
  }
})