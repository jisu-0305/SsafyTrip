<template>
  <div class="pa-8">
    <v-card-title class="text-h5 font-weight-bold mb-8">회원정보 수정</v-card-title>

    <v-form ref="form" v-model="valid">
      <v-row>
        <v-col cols="12">
          <v-text-field label="이메일" v-model="formData.email" readonly variant="outlined"
            density="comfortable"></v-text-field>
        </v-col>

        <v-col cols="12">
          <v-text-field label="이름" v-model="formData.name" readonly variant="outlined"
            density="comfortable"></v-text-field>
        </v-col>

        <v-col cols="12">
          <v-radio-group v-model="formData.gender" inline>
            <label class="text-subtitle-1 mb-2">성별</label>
            <v-radio label="남성" value="male"></v-radio>
            <v-radio label="여성" value="female"></v-radio>
          </v-radio-group>
        </v-col>

        <v-col cols="12">
          <!-- 주소 입력 -->
          <div class="mb-3">
            <div class="d-flex mb-2">
              <v-text-field v-model="address.zipcode" label="우편번호" readonly variant="outlined" density="comfortable"
                class="mb-2" append-inner-icon="mdi-magnify" @click:append-inner="openPostcode"></v-text-field>
            </div>

            <v-text-field v-model="address.roadAddress" label="도로명/지번 주소" readonly variant="outlined"
              density="comfortable" class="mb-2"></v-text-field>

            <v-text-field ref="detailAddressRef" v-model="address.detailAddress" label="상세 주소" variant="outlined"
              density="comfortable" class="mb-2"></v-text-field>
          </div>
        </v-col>
      </v-row>
    </v-form>

    <v-row class="mt-4">
      <v-col cols="12" class="d-flex justify-space-between">
        <v-btn color="grey-darken-1" variant="outlined" @click="cancelEdit">
          회원 탈퇴
        </v-btn>
        <v-btn color="primary" @click="saveChanges"> 저장하기 </v-btn>
      </v-col>
    </v-row>
  </div>
</template>

<script setup>
import { ref } from "vue";

const valid = ref(false);
const formData = ref({
  email: "user1@naver.com",
  name: "홍길동",
  gender: "private",
  address: "",
});

// 주소 관련 데이터
const address = ref({
  zipcode: "",
  roadAddress: "",
  detailAddress: "",
});

const openPostcode = () => {
  new daum.Postcode({
    oncomplete: (data) => {
      address.value.zipcode = data.zonecode
      address.value.roadAddress = data.roadAddress
      // ref를 통해 상세주소 입력 필드에 접근
      if (detailAddressRef.value) {
        detailAddressRef.value.$el.querySelector('input').focus()
      }
    },
  }).open()
}

const cancelEdit = () => {
  alert("취소되었습니다.");
};

const saveChanges = () => {
  console.log();
  alert("저장되었습니다.");
};
</script>
