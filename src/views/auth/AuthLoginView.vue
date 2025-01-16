<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useMenuStore } from '@/stores/menuStores'
import { useAuthStore } from '@/stores/authStores'

const router = useRouter()
const menuStore = useMenuStore()
const authStore = useAuthStore()

const form = ref({
  email: '',
  password: '',
})

const saveId = ref(false)
const formError = ref({
  email: '',
  password: '',
})

// 저장된 이메일 불러오기
onMounted(() => {
  const savedEmail = localStorage.getItem('savedEmail')
  if (savedEmail) {
    form.value.email = savedEmail
    saveId.value = true
  }
})

const validateForm = () => {
  let isValid = true
  formError.value = {
    email: '',
    password: '',
  }

  if (!form.value.email.trim()) {
    formError.value.email = '이메일을 입력해주세요.'
    isValid = false
  }

  if (!form.value.password.trim()) {
    formError.value.password = '비밀번호를 입력해주세요.'
    isValid = false
  }

  return isValid
}

const clearError = (field) => {
  formError.value[field] = ''
}

const login = async () => {
  if (!validateForm()) return;

  try {
    const success = await authStore.login({
      email: form.value.email,
      password: form.value.password
    });

    if (success) {
      if (saveId.value) {
        localStorage.setItem('savedEmail', form.value.email);
      } else {
        localStorage.removeItem('savedEmail');
      }
      
      menuStore.setUserInfo({
        name: authStore.user?.name || 'User'
      });
      
      router.push('/');
    } else {
      form.value.password = '';
    }
  } catch (error) {
    console.error('로그인 실패:', error);
    form.value.password = '';
  }
};
</script>

<template>
  <v-container class="fill-height" fluid>
    <v-row justify="center" align="center">
      <v-col cols="12" sm="8" md="6" lg="4">
        <v-card class="auth-card" elevation="0" :border="true">
          <v-card-item class="px-6 py-6">
            <v-card-title class="auth-title">로그인</v-card-title>
            <v-card-subtitle class="auth-subtitle">
              서비스를 이용하시려면 로그인해주세요.
            </v-card-subtitle>
          </v-card-item>

          <v-form @submit.prevent="login" class="auth-form">
            <v-text-field
              v-model="form.email"
              label="이메일"
              type="email"
              variant="outlined"
              density="comfortable"
              class="mb-2"
              :error-messages="formError.email"
              @focus="clearError('email')"
              ></v-text-field>

            <v-text-field
              v-model="form.password"
              label="비밀번호"
              type="password"
              variant="outlined"
              density="comfortable"
              class="mb-2"
              :error-messages="formError.password"
              @focus="clearError('password')"
              ></v-text-field>

            <v-checkbox
              v-model="saveId"
              label="이메일 저장"
              class="mb-2"
              density="compact"
              color="primary"
            ></v-checkbox>

            <!-- 로그인 에러 메시지 -->
            <v-alert
              v-if="authStore.loginError"
              type="error"
              variant="tonal"
              class="mb-4"
              density="compact"
            >
              {{ authStore.loginError }}
            </v-alert>

            <v-btn
              block
              color="primary"
              type="submit"
              :loading="authStore.isLoading"
              class="auth-btn mb-4"
            >
              로그인
            </v-btn>

            <div class="auth-link-group">
              <v-btn
                variant="text"
                density="comfortable"
                class="auth-link"
                @click="router.push('/join')"
              >
                회원가입
              </v-btn>
              <v-divider vertical class="mx-2"></v-divider>
              <v-btn
                variant="text"
                density="comfortable"
                class="auth-link"
              >
                계정 찾기
              </v-btn>
              <v-divider vertical class="mx-2"></v-divider>
              <v-btn
                variant="text"
                density="comfortable"
                class="auth-link"
              >
                비밀번호 재설정
              </v-btn>
            </div>
          </v-form>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
/* 스타일 제거하고 global.css 사용 */
</style>