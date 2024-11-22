<template>
    <v-container class="pa-4" max-width="600">
      <v-card class="card-size1">
        <!-- 타이틀 -->
        <v-card-title class="text-h5 font-weight-bold">회원 탈퇴</v-card-title>
        <v-card-text>
          <!-- 경고 메시지 -->
          <div class="warning-section mb-6">
            <p class="text-h6 font-weight-bold">주의하세요!</p>
            <p class="text-body-2">
              회원 탈퇴 시 개인정보 및 모든 데이터는 삭제됩니다.
              <br />
              한번 삭제된 정보는 복구가 불가능합니다.
            </p>
          </div>
  
          <!-- 비밀번호 입력 폼 -->
          <v-form ref="form" v-model="valid">
            <div class="form-group">
              <label class="form-label" for="address">비밀번호</label>
              <input type="text" id="address" class="form-input" />
            </div>
            <div class="form-group">
              <label class="form-label" for="address">비밀번호 확인</label>
              <input type="text" id="address" class="form-input" />
            </div>
          </v-form>
        </v-card-text>
  
        <!-- 버튼 -->
        <v-card-actions>
          <v-btn outlined color="grey" class="custom-btn" @click="cancelAction">
            취소
          </v-btn>
          <v-spacer></v-spacer>
          <v-btn dark color="primary" class="custom-btn" @click="confirmAction">
            탈퇴하기
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-container>
  </template>
  
  <script>
  export default {
    data() {
      return {
        valid: false,
        formData: {
          password: "",
          confirmPassword: "",
        },
      };
    },
    methods: {
      cancelAction() {
        alert("취소 버튼이 클릭되었습니다.");
      },
      confirmAction() {
        if (
          this.formData.password &&
          this.formData.password === this.formData.confirmPassword
        ) {
          alert("회원 탈퇴가 진행됩니다.");
        } else {
          alert("비밀번호를 확인해주세요.");
        }
      },
    },
  };
  </script>
  
  <style scoped>
  .v-container {
    width: 100%;
    padding: 32px;  /* 패딩을 32px로 수정 */
    margin-right: 0;  /* 기본 마진을 제거 */
    margin-left: 0;   /* 기본 마진을 제거 */
    }
    .card-size1{
  min-width: 500px;
    min-height: 600px;
    max-width: 500px;
    max-height: 600px;
}
    .pa-4 {
    padding: 0px !important;
    }
  .warning-section {
    color: #d32f2f; /* 경고 메시지 텍스트 색상 (빨간색) */
  }
  
  .form-input,
  .form-radio-group input {
    width: 100%;
    padding: 10px;
    font-size: 14px;
    border: 1px solid #ccc;
    border-radius: 4px;
  }
  
  .custom-btn {
    padding: 10px 20px;
    font-size: 14px;
    font-weight: bold;
    border-radius: 4px;
    border: 1px solid #ccc; /* 기본 테두리 */
    background-color: transparent !important; /* Vuetify의 기본 배경색 덮어쓰기 */
    color: #131212 !important; /* Vuetify의 기본 텍스트 색 덮어쓰기 */
    cursor: pointer;
    transition: all 0.3s ease; /* 호버 시 애니메이션 */
  }
  
  .custom-btn:hover {
    background-color: #000 !important; /* 검정 배경 */
    color: #fff !important; /* 흰색 텍스트 */
  }
  
  .v-btn[outlined] {
    border: 1px solid #ccc;
  }
  </style>