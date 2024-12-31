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

// 실제 라우팅 가능한 경로와 타이틀 매핑
const routeMap = {
  'main': { title: 'Home', path: '/' },
  'attraction': { title: '관광지', path: '/attraction' },
  'notice': { title: '공지사항', path: '/notice' },
  'mypage': { title: '마이페이지', path: '/mypage' },
  'board': { title: '게시판', path: '/board' },
  'search': { title: '검색', path: '/search' },
  'schedules': { title: '여행 일정', path: '/schedules' },
  'question': { title: '1:1 문의', path: '/question' }
};

// 경로 변경 감지하여 브레드크럼 업데이트
watch(() => route.path, () => {
  const pathArray = route.path.split('/').filter(x => x);
  
  // 홈 항상 추가
  items.value = [{
    title: 'Home',
    disabled: false,
    href: '/',
  }];

  // 중간 경로들 처리
  let currentPath = '';
  pathArray.forEach((path, index) => {
    currentPath += `/${path}`;
    
    // 마지막 항목이거나 routeMap에 없는 경로는 클릭 불가능하게 처리
    const isLastItem = index === pathArray.length - 1;
    const mappedRoute = routeMap[path];
    
    items.value.push({
      title: getBreadcrumbTitle(path),
      disabled: isLastItem || !mappedRoute,
      href: mappedRoute ? mappedRoute.path : undefined
    });
  });
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
    'board': '게시판',
    'search': '검색',
    'schedules': '여행 일정',
    'question': '1:1 문의'
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