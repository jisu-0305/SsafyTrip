<template>
  <div class="pa-8">
    <v-card-title class="text-h5 font-weight-bold mb-8">회원 탈퇴</v-card-title>

    <v-alert type="warning" variant="tonal" class="mb-6">
      <div class="text-h6 font-weight-bold mb-2">주의하세요!</div>
      <div class="text-body-1">
        회원 탈퇴 시 개인정보 및 모든 데이터는 삭제됩니다.<br />
        한번 삭제된 정보는 복구가 불가능합니다.
      </div>
    </v-alert>

    <v-form ref="form" v-model="valid">
      <v-row>
        <v-col cols="12">
          <v-text-field
            v-model="formData.password"
            label="비밀번호"
            type="password"
            variant="outlined"
            density="comfortable"
          ></v-text-field>
        </v-col>

        <v-col cols="12">
          <v-text-field
            v-model="formData.confirmPassword"
            label="비밀번호 확인"
            type="password"
            variant="outlined"
            density="comfortable"
          ></v-text-field>
        </v-col>
      </v-row>
    </v-form>

    <v-row class="mt-4">
      <v-col cols="12" class="d-flex justify-space-between">
        <v-btn color="grey-darken-1" variant="outlined" @click="cancelAction">
          취소
        </v-btn>
        <v-btn color="error" @click="confirmAction"> 탈퇴하기 </v-btn>
      </v-col>
    </v-row>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useAuthStore } from "@/stores/authStores";
import { deleteUser } from "@/api/mypageApi";
import { useMenuStore } from "@/stores/menuStores";
import { useRouter } from "vue-router";
const router = useRouter();

const valid = ref(false);
const formData = ref({
  password: "",
  confirmPassword: "",
});

const cancelAction = () => {
  alert("취소 버튼이 클릭되었습니다.");
};

const confirmAction = () => {
  if (
    formData.value.password &&
    formData.value.password === formData.value.confirmPassword
  ) {
    const authStore = useAuthStore();
    const user = authStore.user;
    const email = user.email;

    console.log(email);
    console.log(formData.value.password);

    deleteUser({ email, password: formData.value.password })
      .then((response) => {
        console.log("유저 삭제 성공:", response.data);
        const menuStore = useMenuStore();

        menuStore.changeMenuState(false); // 메뉴 상태 변경
        router.push({ name: "main" }); // 메인 페이지로 이동
      })
      .catch((error) => {
        console.error("유저 삭제 실패:", error);
      });

    alert("회원 탈퇴가 진행됩니다.");
  } else {
    alert("비밀번호를 확인해주세요.");
  }
};
</script>
