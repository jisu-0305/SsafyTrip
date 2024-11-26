<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const quickLinks = [
  { title: '공지사항', icon: 'mdi-bell-outline', route: 'notice' },
  { title: '질문하기', icon: 'mdi-help-circle-outline', route: 'question' },
  { title: '이용약관', icon: 'mdi-file-document-outline', route: 'terms' },
  { title: '개인정보처리방침', icon: 'mdi-shield-outline', route: 'privacy' }
];

const handleLinkClick = (route) => {
  router.push({ name: route });
};
</script>

<template>
  <v-footer class="bg-grey-lighten-4">
    <v-container>
      <v-row>
        <!-- 회사 정보 섹션 -->
        <v-col cols="12" md="4" class="text-center text-md-left mb-6 mb-md-0">
          <h3 class="text-h6 font-weight-bold mb-4">
            <v-btn variant="plain" class="pa-0" @click="router.push('/')">
              인싸루트
            </v-btn>
          </h3>
          <p class="text-body-2 text-grey-darken-1">
            당신의 여행을 더 특별하게<br>
            AI 기반 스마트 여행 플래너
          </p>
        </v-col>

        <!-- 빠른 링크 섹션 -->
        <v-col cols="12" md="4" class="text-center mb-6 mb-md-0">
          <h3 class="text-subtitle-1 font-weight-bold mb-4">빠른 링크</h3>
          <v-list density="compact" bg-color="transparent">
            <v-list-item
              v-for="(link, i) in quickLinks"
              :key="i"
              :title="link.title"
              class="px-0"
              density="compact"
              @click="handleLinkClick(link.route)"
              :class="'quick-link-item'"
            >
              <template v-slot:prepend>
                <v-icon size="small" color="grey-darken-1">{{ link.icon }}</v-icon>
              </template>
            </v-list-item>
          </v-list>
        </v-col>

        <!-- 연락처 섹션 -->
        <v-col cols="12" md="4" class="text-center text-md-right">
          <h3 class="text-subtitle-1 font-weight-bold mb-4">고객센터</h3>
          <div class="text-body-2 text-grey-darken-1">
            <p class="contact-info">이메일: support@inssaroute.com</p>
            <p class="contact-info">전화: 1544-0000</p>
            <p class="contact-info">운영시간: 평일 09:00 - 18:00</p>
          </div>
        </v-col>
      </v-row>

      <!-- 저작권 표시 -->
      <v-row class="mt-6">
        <v-col cols="12">
          <v-divider class="mb-4"></v-divider>
          <div class="text-center text-body-2 text-grey-darken-1">
            © {{ new Date().getFullYear() }} 인싸루트. All rights reserved.
          </div>
        </v-col>
      </v-row>
    </v-container>
  </v-footer>
</template>

<style scoped>
.v-footer {
  border-top: 1px solid #e0e0e0;
  margin-top: 50px;
}

:deep(.v-list-item) {
  min-height: 32px;
  cursor: pointer;
  transition: all 0.3s ease;
}

:deep(.v-list-item:hover) {
  background-color: rgba(0, 0, 0, 0.04);
}

:deep(.v-list-item__content) {
  padding: 0;
}

.contact-info {
  margin-bottom: 8px;
}

.quick-link-item {
  border-radius: 4px;
}
</style>

