<script setup>
import { ref, computed,reactive } from "vue";
import { checkEmailDuplicationAxios, registerAxios } from "@/api/member";
import { useRouter } from "vue-router";

const router = useRouter();

// 서버로 보낼 데이터
const form = ref({
  name: "",
  email: "", 
  password: "",
  confirmPassword:"",
  gender: "",  
  birthdate: "",  
  address:"",
  role:"ROLE_USER",
});

// 주소 관련 데이터
const address = ref({
  zipcode: "",
  roadAddress: "",
  detailAddress: "",
});

const emailDuplicateError = ref(false);  // 이메일 중복 여부

const isDuplicateFlag = ref(false); //이메일 중복 확인 체크

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


// 이메일 중복 확인 함수
const checkEmailDuplicateListener = () => {
  console.log("이메일 중복 확인 메소드");
  console.log(form.value.email);
  checkEmailDuplicationAxios(form.value.email,({data})=>{
    if(data == true){
      alert("다른 아이디를 입력해주세요.");
    }else{
      isDuplicateFlag.value = true;
    }
  });

};

// 서버로 보낼 데이터 준비
const prepareDataToSend = () => {
  // 주소 데이터를 하나의 문자열로 결합
  const addressString = `${address.value.zipcode} ${address.value.roadAddress} ${address.value.detailAddress}`;
  const requestData = {
    name: form.value.name,
    email: form.value.email,
    password: form.value.password,
    birthdate: form.value.birthdate,  // 생년월일
    gender: form.value.gender,
    address: addressString,  // 합쳐서 서버로 보낼 주소
    role: form.value.role,  // 역할 예시
  };
  console.log(requestData);
  return requestData;
   
};

// 주소 검색 함수 (Daum API 사용)
const openPostcode = () => {
  new daum.Postcode({
    oncomplete: (data) => {
      address.value.zipcode = data.zonecode;
      address.value.roadAddress = data.roadAddress;
      document.getElementById("sample6_detailAddress").focus();
    },
  }).open();
};

// 폼 제출 처리
const submitForm = () => {
  if (passwordsDontMatch.value) {
    alert("비밀번호가 일치하지 않습니다.");
    return;
  }
  console.log("submitForm");
  const dataToSend = prepareDataToSend();
  console.log(dataToSend);  // 서버로 보낼 데이터 로그
  registerAxios(dataToSend, ({data})=>{
    router.push({ name: 'main' });
  });
};


</script>

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
        <div class="input-group">
          <input
            type="email"
            class="form-control"
            id="email"
            v-model="form.email"
            required
            :disabled="isDuplicateFlag"
          />
          <button type="button" class="btn btn-outline-secondary" @click="checkEmailDuplicateListener">
            중복 확인
          </button>
        </div>
        <div v-if="emailDuplicateError" class="text-danger small">
          이미 사용 중인 이메일입니다.
        </div>
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
          v-model="form.birthdate"
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
            value="M"
            v-model="form.gender"
            class="form-check-input"
            required
          />
          <label for="male" class="form-check-label">남성</label>

          <input
            type="radio"
            id="female"
            value="F"
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
            v-model="address.zipcode"
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
            v-model="address.roadAddress"
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
            v-model="address.detailAddress"
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

<style scoped>
.signUp-input-area {
  margin-bottom: 10px;
}

.input-group {
  display: flex;
  align-items: center;
}

.input-group .form-control {
  border-right: 0;
}

.input-group .btn {
  border-left: 0;
}

.text-danger {
  color: red;
}
</style>