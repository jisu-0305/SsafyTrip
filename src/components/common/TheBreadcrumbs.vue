<template>
  <v-breadcrumbs
    :items="items"
    divider="/"
    class="custom-breadcrumbs"
  >
    <template v-slot:title="{ item }">
      <v-breadcrumbs-item
        :title="item.title"
        :disabled="item.disabled"
        :href="item.href"
        class="custom-breadcrumb-item"
      >
        {{ item.title }}
      </v-breadcrumbs-item>
    </template>
  </v-breadcrumbs>
</template>

<script setup>
import { ref, watch } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const items = ref([]);

// 경로 변경 감지하여 브레드크럼 업데이트
watch(() => route.path, () => {
  const pathArray = route.path.split('/').filter(x => x);
  items.value = [
    {
      title: 'Home',
      disabled: false,
      href: '/',
    },
    ...pathArray.map((path, index) => ({
      title: getBreadcrumbTitle(path),
      disabled: index === pathArray.length - 1,
      href: `/${pathArray.slice(0, index + 1).join('/')}`,
    }))
  ];
}, { immediate: true });

// 경로명을 표시명으로 변환
function getBreadcrumbTitle(path) {
  const titleMap = {
    'notice': '공지사항',
    'write': '작성',
    'edit': '수정',
    'detail': '상세보기',
    'mypage': '마이페이지',
    'attraction': '관광지',
    'board': '게시판'
  };
  return titleMap[path] || path;
}
</script>

<style scoped>
.custom-breadcrumbs {
  font-family: 'Noto Sans KR', sans-serif;
  font-size: 14px;
  color: rgba(0, 0, 0, 0.6);
  padding: 16px 0;
  width: 100%;
  display: flex;
}

.custom-breadcrumb-item {
  color: inherit;
  text-decoration: none;
}

:deep(.v-breadcrumbs-item--disabled) {
  color: rgba(0, 0, 0, 0.87) !important;
  font-weight: 500;
  opacity: 1 !important;
}

:deep(.v-breadcrumbs__divider) {
  color: rgba(0, 0, 0, 0.38);
  padding: 0 8px;
}
</style> 