<script setup>
import { ref } from "vue";
import { useMenuStore } from "@/stores/menu";
import { useRouter } from "vue-router";

const { changeMenuState } = useMenuStore();
const router = useRouter();

const form = ref({
  username: "",
  password: "",
  rememberMe: false,
});

const showPassword = ref(false);
const valid = ref(true);
const errorMessage = ref("");

// 유효성 검사 규칙
const rules = {
  username: [v => !!v || '아이디를 입력해주세요'],
  password: [v => !!v || '비밀번호를 입력해주세요']
};

const login = () => {
  if (valid.value) {
    changeMenuState();
    router.push("/");
  }
};
</script>

<template>
  <v-container class="fill-height">
    <v-row justify="center" align="center">
      <v-col cols="12" sm="8" md="6" lg="4">
        <v-card class="elevation-12 pa-8">
          <v-card-title class="text-h4 text-center mb-4">
            로그인
          </v-card-title>

          <v-form
            v-model="valid"
            @submit.prevent="login"
          >
            <!-- 아이디 -->
            <v-text-field
              v-model="form.username"
              :rules="rules.username"
              label="아이디"
              prepend-inner-icon="mdi-account"
              variant="outlined"
              required
            ></v-text-field>

            <!-- 비밀번호 -->
            <v-text-field
              v-model="form.password"
              :rules="rules.password"
              :type="showPassword ? 'text' : 'password'"
              label="비밀번호"
              prepend-inner-icon="mdi-lock"
              :append-inner-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
              variant="outlined"
              required
              @click:append-inner="showPassword = !showPassword"
            ></v-text-field>

            <!-- 아이디 저장 -->
            <v-checkbox
              v-model="form.rememberMe"
              label="아이디 저장"
              color="primary"
              hide-details
              class="mb-4"
            ></v-checkbox>

            <!-- 로그인 버튼 -->
            <v-btn
              type="submit"
              color="primary"
              block
              size="large"
              :disabled="!valid"
              @click="login"
            >
              로그인
            </v-btn>

            <!-- 에러 메시지 -->
            <v-alert
              v-if="errorMessage"
              type="error"
              class="mt-4"
              variant="tonal"
            >
              {{ errorMessage }}
            </v-alert>
          </v-form>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>