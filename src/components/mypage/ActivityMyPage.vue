<template>
    <v-container class="pa-4 unified-container"
    style="margin: 0;">
    <!-- unified-card -->
      <v-card class="card-size1">
        <!-- 타이틀 -->
        <v-card-title class="text-h5 font-weight-bold">나의 활동</v-card-title>
        <v-card-text>
          <!-- 활동 버튼 그룹 -->
          <v-row class="activity-section" dense>
            <v-col cols="3" class="activity-item">
              <v-btn icon @click="handleClick('찜목록')">
                <v-icon>mdi-bookmark-outline</v-icon>
              </v-btn>
              <span>찜목록</span>
            </v-col>
            <v-col cols="3" class="activity-item">
              <v-btn icon @click="handleClick('여행톡')">
                <v-icon>mdi-chat-outline</v-icon>
              </v-btn>
              <span>여행톡</span>
            </v-col>
            <v-col cols="3" class="activity-item">
              <v-btn icon @click="handleClick('여행코스')">
                <v-icon>mdi-map-outline</v-icon>
              </v-btn>
              <span>여행코스</span>
            </v-col>
            <v-col cols="3" class="activity-item">
              <v-btn icon @click="handleClick('1:1문의')">
                <v-icon>mdi-help-circle-outline</v-icon>
              </v-btn>
              <span>1:1문의</span>
            </v-col>
          </v-row>
        </v-card-text>
      </v-card>
    </v-container>
  </template>
  
  <script>
  export default {
    methods: {
      handleClick(activity) {
        alert(`${activity} 버튼이 클릭되었습니다.`);
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
  .unified-container {
    max-width: 600px;
    /* margin: auto; */
  }
  
  .unified-card {
    min-height: 300px; /* 두 컴포넌트의 높이를 맞추기 위해 최소 높이 설정 */
    padding: 16px; /* 통일된 내부 패딩 추가 */
  }
  
  .activity-section {
    /* padding: 16px 0; */
    display: flex;
    justify-content: space-around;
  }
  
  .activity-item {
    text-align: center;
  }
  
  .activity-item span {
    display: block;
    margin-top: 8px;
    font-size: 14px;
    color: #666;
  }
  </style>