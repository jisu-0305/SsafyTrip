<template>
  <div class="container">
    <h2>회원가입 폼</h2>
    <form @submit.prevent="submitForm">
      <!-- 이름 -->
      <div class="mb-3">
        <label for="name" class="form-label">이름</label>
        <input
          type="text"
          class="form-control"
          id="name"
          v-model="form.name"
          required
        />
      </div>

      <!-- 이메일 -->
      <div class="mb-3">
        <label for="email" class="form-label">이메일</label>
        <input
          type="email"
          class="form-control"
          id="email"
          v-model="form.email"
          required
        />
      </div>

      <!-- 비밀번호 -->
      <div class="mb-3">
        <label for="password" class="form-label">비밀번호</label>
        <input
          type="password"
          class="form-control"
          id="password"
          v-model="form.password"
          required
        />
      </div>

      <!-- 비밀번호 확인 -->
      <div class="mb-3">
        <label for="confirmPassword" class="form-label">비밀번호 확인</label>
        <input
          type="password"
          class="form-control"
          id="confirmPassword"
          v-model="form.confirmPassword"
          required
        />
        <div v-if="passwordsDontMatch" class="text-danger small">
          비밀번호가 일치하지 않습니다.
        </div>
      </div>

      <!-- 생년월일 -->
      <div class="mb-3">
        <label for="dob" class="form-label">생년월일</label>
        <input
          type="date"
          class="form-control"
          id="dob"
          v-model="form.dob"
          required
        />
      </div>

      <!-- 성별 -->
      <div class="mb-3">
        <label class="form-label">성별</label>
        <div>
          <input
            type="radio"
            id="male"
            value="male"
            v-model="form.gender"
            class="form-check-input"
            required
          />
          <label for="male" class="form-check-label">남성</label>

          <input
            type="radio"
            id="female"
            value="female"
            v-model="form.gender"
            class="form-check-input"
            required
          />
          <label for="female" class="form-check-label">여성</label>
        </div>
      </div>

      <!-- 주소 입력 -->
      <div class="mb-3">
        <label for="address" class="form-label">주소</label>

        <!-- 우편번호 입력 -->
        <div class="signUp-input-area">
          <input
            type="text"
            class="form-control"
            v-model="form.zipcode"
            placeholder="우편번호"
            maxlength="6"
            id="sample6_postcode"
            readonly
          />
          <button type="button" @click="openPostcode" class="btn btn-primary">
            검색
          </button>
        </div>

        <!-- 도로명/지번 주소 입력 -->
        <div class="signUp-input-area">
          <input
            type="text"
            class="form-control"
            v-model="form.roadAddress"
            placeholder="도로명/지번 주소"
            id="sample6_address"
            readonly
          />
        </div>

        <!-- 상세 주소 입력 -->
        <div class="signUp-input-area">
          <input
            type="text"
            class="form-control"
            v-model="form.detailAddress"
            placeholder="상세 주소"
            id="sample6_detailAddress"
          />
        </div>
      </div>

      <!-- 제출 버튼 -->
      <button type="submit" class="btn btn-primary">회원가입</button>
    </form>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";

const form = ref({
  name: "",
  gender: "",
  email: "",
  password: "",
  confirmPassword: "",
  dob: "",
  zipcode: "",
  roadAddress: "",
  detailAddress: "",
});

// 비밀번호와 비밀번호 확인이 일치하지 않으면 경고 표시
const passwordsDontMatch = computed(() => {
  return form.value.password !== form.value.confirmPassword;
});

// Daum 주소 검색 API 실행
const openPostcode = () => {
  console.log("실행");
  new daum.Postcode({
    oncomplete: (data) => {
      // 선택된 주소를 폼에 자동 입력
      form.value.zipcode = data.zonecode;
      form.value.roadAddress =
        data.userSelectedType === "R" ? data.roadAddress : data.jibunAddress;
      // 상세 주소 입력란에 커서 이동
      document.getElementById("sample6_detailAddress").focus();
    },
  }).open();
};

// 폼 제출 처리
const submitForm = () => {
  // 폼 제출 처리 (여기서 실제 API 호출이나 서버로 데이터 전송)
  console.log(form.value);
};
</script>

<style scoped></style>
