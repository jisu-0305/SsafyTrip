<script setup>
import { ref, computed, reactive } from "vue";
import authApi from "@/api/authApi";
import { useRouter } from "vue-router";

const router = useRouter();

// 서버로 보낼 데이터
const form = ref({
  name: "",
  email: "",
  password: "",
  confirmPassword: "",
  gender: "",
  birthdate: "",
  address: "",
  role: "ROLE_USER",
});

// 주소 관련 데이터
const address = ref({
  zipcode: "",
  roadAddress: "",
  detailAddress: "",
});

const emailDuplicateError = ref(false); // 이메일 중복 여부

const isDuplicateFlag = ref(false); //이메일 중복 확인 체크

const emailMessage = ref(""); // 이메일 확인 메시지
const emailChecked = ref(false); // 이메일 확인 여부

const isLoading = ref(false); // 로딩 상태 추가

// function test(){
//   console.log("test");
//   registerAxios({
//   "name": "홍",
//   "gender": "M",
//   "email": "mong4552",
//   "password": "123",
//   "birthdate": "1990-05-11",
//   "address": "서울특별시 강남구 역삼동 123-45",
//   "role": "ROLE_USER"
// });
// }
// test();

// 비밀번호와 비밀번호 확인이 일치하는지 여부를 확인하는 computed 속성
const passwordsDontMatch = computed(() => {
  return form.value.password !== form.value.confirmPassword;
});

// 이메일 중복 확인 함수 수정
const checkEmailDuplicateListener = async () => {
  if (!form.value.email) {
    emailMessage.value = "이메일을 입력해주세요.";
    return;
  }

  // 이메일 정규식
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(form.value.email)) {
    alert("유효한 이메일 형식이 아닙니다.");
    return;
  }

  try {
    const response = await authApi.checkEmailDuplication(form.value.email);
    emailChecked.value = true;

    if (response.data) {
      emailMessage.value = "중복된 이메일입니다.";
    } else {
      emailMessage.value = "사용할 수 있는 이메일입니다.";
    }
  } catch (error) {
    console.error("이메일 중복 확인 실패:", error);
    emailMessage.value = "이메일 중복 확인에 실패했습니다.";
  }
};

// 이메일 입력 시 확인 상태 초기화
const resetEmailCheck = () => {
  emailChecked.value = false;
  emailMessage.value = "";
};

// 서버로 보낼 데이터 준비
const prepareDataToSend = () => {
  // 주소를 컴마로 구분하여 하나의 문자열로 결합
  const addressString = [
    address.value.zipcode,
    address.value.roadAddress,
    address.value.detailAddress,
  ]
    .filter(Boolean)
    .join(", "); // 빈 값을 제거하고 컴마와 공백으로 구분

  return {
    name: form.value.name,
    gender: form.value.gender, // 'M' 또는 'F'
    email: form.value.email,
    password: form.value.password,
    birthdate: form.value.birthdate,
    address: addressString, // "12345, 서울시 강남구, 101동 1001호" 형식
    role: "USER",
  };
};

// 주소 검색 함수 수정
const detailAddressRef = ref(null);

const openPostcode = () => {
  new daum.Postcode({
    oncomplete: (data) => {
      address.value.zipcode = data.zonecode;
      address.value.roadAddress = data.roadAddress;
      // ref를 통해 상세주소 입력 필드에 접근
      if (detailAddressRef.value) {
        detailAddressRef.value.$el.querySelector("input").focus();
      }
    },
  }).open();
};

// 폼 제출 처리 수정
const submitForm = async () => {
  if (isLoading.value) return; // 이미 제출 중이면 중복 제출 방지
  if (!validateForm()) return;

  try {
    isLoading.value = true; // 로딩 시작
    const registerData = prepareDataToSend();

    const response = await authApi.register(registerData);
    alert("회원가입이 완료되었습니다.");
    router.push({ name: "user-login" });
  } catch (error) {
    console.error("회원가입 실패:", error);
    alert("회원가입에 실패했습니다. 다시 시도해주세요.");
  } finally {
    isLoading.value = false; // 로딩 종료
  }
};

// submitForm 함수 위에 추가
const validateForm = () => {
  let isValid = true;

  // 필수 필드 검증
  if (!form.value.name) {
    alert("이름을 입력해주세요.");
    return false;
  }

  if (!form.value.email) {
    alert("이메일을 입력해주세요.");
    return false;
  }

  if (!emailChecked.value) {
    alert("이메일 중복 확인을 해주세요.");
    return false;
  }

  if (!form.value.password) {
    alert("비밀번호를 입력해주세요.");
    return false;
  }

  if (passwordsDontMatch.value) {
    alert("비밀번호가 일치하지 않습니다.");
    return false;
  }

  if (!form.value.birthdate) {
    alert("생년월일을 입력해주세요.");
    return false;
  }

  if (!form.value.gender) {
    alert("성별을 선택해주세요.");
    return false;
  }

  if (!address.value.roadAddress) {
    alert("주소를 입력해주세요.");
    return false;
  }

  return isValid;
};
</script>

<template>
  <v-container class="fill-height">
    <v-row justify="center">
      <v-col cols="12" sm="8" md="6">
        <v-card width="400" style="margin: 0 auto">
          <v-card-item class="px-6 py-6">
            <v-card-title class="text-h5 font-weight-bold mb-2">
              회원가입
            </v-card-title>
            <v-card-subtitle
              class="text-body-2"
              style="color: #64748b; font-family: Inter"
            >
              여행을 시작하기 위한 첫 걸음
            </v-card-subtitle>
          </v-card-item>
          <!-- Form Content -->
          <v-form @submit.prevent="submitForm" class="px-6">
            <v-text-field
              v-model="form.name"
              label="이름"
              variant="outlined"
              density="comfortable"
              class="mb-3"
              hide-details
              style="border-radius: 6px"
              bg-color="white"
              type="email"
            ></v-text-field>

            <div class="d-flex align-center mb-3">
              <v-text-field
                v-model="form.email"
                label="이메일"
                variant="outlined"
                density="comfortable"
                hide-details
                class="mr-2"
                style="border-radius: 6px"
                bg-color="white"
                @input="resetEmailCheck"
              ></v-text-field>
              <v-btn
                @click="checkEmailDuplicateListener"
                color="#0F172A"
                min-width="80"
                height="40"
                style="border-radius: 6px"
              >
                중복 확인
              </v-btn>
            </div>

            <!-- 이메일 확인 메시지 -->
            <div
              :class="[
                'text-body-2 mb-3',
                emailMessage.includes('사용') ? 'text-success' : 'text-error',
              ]"
              v-if="emailMessage"
            >
              {{ emailMessage }}
            </div>

            <v-text-field
              v-model="form.password"
              label="비밀번호"
              type="password"
              variant="outlined"
              density="comfortable"
              class="mb-3"
              hide-details
              style="border-radius: 6px"
              bg-color="white"
            ></v-text-field>

            <v-text-field
              v-model="form.confirmPassword"
              label="비밀번호 확인"
              type="password"
              variant="outlined"
              density="comfortable"
              class="mb-3"
              :error-messages="
                passwordsDontMatch ? '비밀번호가 일치하지 않습니다' : ''
              "
              style="border-radius: 6px"
              bg-color="white"
            ></v-text-field>

            <v-text-field
              v-model="form.birthdate"
              label="생년월일"
              type="date"
              variant="outlined"
              density="comfortable"
              class="mb-3"
              hide-details
              style="border-radius: 6px"
              bg-color="white"
              placeholder=""
            ></v-text-field>

            <v-radio-group
              v-model="form.gender"
              inline
              class="mb-3"
              hide-details
            >
              <v-radio label="남성" value="M"></v-radio>
              <v-radio label="여성" value="F"></v-radio>
            </v-radio-group>

            <!-- 주소 입력 -->
            <div class="mb-3">
              <v-text-field
                v-model="address.zipcode"
                label="우편번호"
                readonly
                variant="outlined"
                density="comfortable"
                append-inner-icon="mdi-magnify"
                @click:append-inner="openPostcode"
              ></v-text-field>

              <v-text-field
                v-model="address.roadAddress"
                label="도로명/지번 주소"
                readonly
                variant="outlined"
                density="comfortable"
              ></v-text-field>

              <v-text-field
                ref="detailAddressRef"
                v-model="address.detailAddress"
                label="상세 주소"
                variant="outlined"
                density="comfortable"
              ></v-text-field>
            </div>

            <!-- 회원가입 버튼 수정 -->
            <v-btn
              :loading="isLoading"
              :disabled="isLoading"
              type="submit"
              block
              color="#0F172A"
              height="40"
              class="mb-3"
              style="border-radius: 6px"
            >
              회원가입
            </v-btn>

            <!-- 안내 문구 추가 -->
            <div class="text-body-2 text-grey-darken-1 text-center mb-4">
              사용중인 메일로 인증 후 회원가입을 실시하여 주세요
            </div>
          </v-form>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped></style>
