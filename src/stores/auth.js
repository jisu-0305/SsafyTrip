import { ref } from 'vue'
import { defineStore } from 'pinia'
import { loginAPI } from '@/api/member'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(null)
  const loginError = ref('')
  const isLoading = ref(false)
  const loginAttempts = ref(parseInt(sessionStorage.getItem('loginAttempts') || '0'))
  const MAX_LOGIN_ATTEMPTS = 5

  const login = async (credentials) => {
    if (loginAttempts.value >= MAX_LOGIN_ATTEMPTS) {
      loginError.value = '로그인 시도 횟수를 초과했습니다. 잠시 후 다시 시도해주세요.'
      return { success: false }
    }

    try {
      isLoading.value = true
      loginError.value = ''
      
      const response = await loginAPI.login(credentials)
      user.value = response.data
      loginAttempts.value = 0
      sessionStorage.removeItem('loginAttempts')
      
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

  const logout = () => {
    user.value = null
  }

  return {
    user,
    loginError,
    isLoading,
    login,
    logout
  }
})