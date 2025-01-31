<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const quickLinks = [
  { title: '여행 일정', icon: 'mdi-calendar-outline', route: 'schedules' },
  { title: '관광지', icon: 'mdi-map-marker-outline', route: 'attract-search' },
  { title: '공지사항', icon: 'mdi-bell-outline', route: 'notice' },
  { title: '1:1 문의', icon: 'mdi-help-circle-outline', route: 'question' }
];

const handleLinkClick = (route) => {
  router.push({ name: route });
};
</script>

<template>
  <v-footer class="bg-grey-lighten-4">
    <v-container class="py-6" style="max-width: 90rem; margin: 0 auto;">
      <v-row justify="center">
        <!-- 회사 정보 섹션 -->
        <v-col cols="12" sm="6" md="4" class="mb-6 text-center">
          <div class="d-flex flex-column align-center">
            <v-btn variant="plain" class="pa-0 mb-4" @click="router.push('/')">
              <v-img src="/img/logo2.png" width="140" height="50" contain></v-img>
            </v-btn>
            <p class="text-body-2 text-grey-darken-1">
              당신의 여행을 더 특별하게<br>
              AI 기반 스마트 여행 플래너
            </p>
          </div>
        </v-col>

        <!-- 빠른 링크 섹션 -->
        <v-col cols="12" sm="6" md="4" class="mb-6 text-center">
          <h3 class="text-subtitle-1 font-weight-bold mb-4">서비스</h3>
          <div class="d-flex flex-column gap-2 align-center">
            <v-btn
              v-for="(link, i) in quickLinks"
              :key="i"
              variant="text"
              density="compact"
              class="justify-center px-0"
              @click="handleLinkClick(link.route)"
            >
              <v-icon start size="small" color="grey-darken-1">
                {{ link.icon }}
              </v-icon>
              <span class="text-body-2 text-grey-darken-1">{{ link.title }}</span>
            </v-btn>
          </div>
        </v-col>

        <!-- 연락처 섹션 -->
        <v-col cols="12" sm="6" md="4" class="mb-6 text-center">
          <h3 class="text-subtitle-1 font-weight-bold mb-4">고객센터</h3>
          <div class="d-flex flex-column gap-2 text-body-2 text-grey-darken-1 align-center">
            <div class="d-flex align-center justify-center">
              <v-icon size="small" class="mr-2">mdi-account</v-icon>
              <span>김도희, 김지수, 이학준</span>
            </div>
            <div class="d-flex align-center justify-center">
              <v-icon size="small" class="mr-2">mdi-email-outline</v-icon>
              <span>support@inssaroute.com</span>
            </div>
            <div class="d-flex align-center justify-center">
              <v-icon size="small" class="mr-2">mdi-phone-outline</v-icon>
              <span>0000-0000</span>
            </div>
            <div class="d-flex align-center justify-center">
              <v-icon size="small" class="mr-2">mdi-clock-outline</v-icon>
              <span>평일 09:00 - 18:00</span>
            </div>
          </div>
        </v-col>
      </v-row>

      <!-- 저작권 표시 -->
      <v-divider class="my-4"></v-divider>
      <div class="text-center pb-4 text-body-2 text-grey-darken-1">
        © {{ new Date().getFullYear() }} 인싸루트. All rights reserved.
      </div>
    </v-container>
  </v-footer>
</template>

<style scoped>
.v-footer {
  border-top: 1px solid var(--color-border);
  margin-top: 50px;
  width: 100%;
}

:deep(.v-btn) {
  text-transform: none;
  letter-spacing: normal;
}

:deep(.v-btn:hover) {
  background-color: transparent;
  opacity: 0.8;
}

.v-container {
  max-width: 90rem;
  margin: 0 auto;
}

@media (max-width: 600px) {
  .v-container {
    padding: 16px;
  }
  
  .text-subtitle-1 {
    font-size: 1rem !important;
    text-align: center;
  }
  
  .text-body-2 {
    font-size: 0.875rem !important;
  }
}
</style>