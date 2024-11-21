<template>
  <v-container class="pa-4" max-width="600">
    <v-card class="card-size1">
      <v-card-title> 회원정보 수정 </v-card-title>
      <v-card-text>
        <v-form ref="form" v-model="valid">
          <!-- 이메일 -->
          <div class="form-group">
            <label class="form-label" for="email">이메일</label>
            <input
              type="email"
              id="email"
              class="form-input"
              value="user1@naver.com"
              readonly
            />
          </div>

          <!-- 이름 -->
          <div class="form-group">
            <label class="form-label" for="name">이름</label>
            <input
              type="text"
              id="name"
              class="form-input"
              value="홍길동"
              readonly
            />
          </div>

          <!-- 성별 -->
          <div class="form-group">
            <label class="form-label">성별</label>
            <div class="form-radio-group">
              <label>
                <input type="radio" name="gender" value="male" />
                남성
              </label>
              <label>
                <input type="radio" name="gender" value="female" />
                여성
              </label>
              <label>
                <input type="radio" name="gender" value="private" />
                비공개
              </label>
            </div>
          </div>

          <!-- 주소 -->
          <div class="form-group">
            <label class="form-label" for="address">주소지</label>
            <input type="text" id="address" class="form-input" />
          </div>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-btn color="grey" @click="cancelEdit" class="custom-btn"
          >회원 탈퇴</v-btn
        >
        <v-spacer></v-spacer>
        <v-btn color="grey" @click="saveChanges" class="custom-btn"
          >저장하기</v-btn
        >
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script></script>

<style scoped>
.v-container {
  /* min-width: 300px;
    min-height: 600px;
    max-width: 300px;
    max-height: 600px; */
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
.v-btn {
  margin-left: 10px;
}
.custom-input {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
}

.form-container {
  max-width: 400px;
  margin: 0 auto;
}

.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  color: #333;
}

.form-input,
.form-radio-group input {
  width: 100%;
  padding: 10px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.form-input[readonly] {
  background-color: #f5f5f5;
}

.form-radio-group {
  display: flex;
  gap: 20px;
  align-items: center;
}

.form-radio-group label {
  margin-left: 5px;
  font-size: 14px;
}

.form-radio-group input {
  width: auto;
}

.button-container {
  display: flex;
  gap: 10px; /* 버튼 간 간격 */
}

.custom-btn {
  padding: 10px 20px;
  font-size: 14px;
  font-weight: bold;
  border-radius: 8px;
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
</style>