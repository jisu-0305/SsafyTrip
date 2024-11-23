<template>
  <div class="page-header">
    <TheBreadcrumbs v-if="showBreadcrumbs" />
    <h1 class="page-title">
      <v-icon v-if="icon" left size="32" class="me-2">{{ icon }}</v-icon>
      {{ title }}
    </h1>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import TheBreadcrumbs from './TheBreadcrumbs.vue';

const props = defineProps({
  title: {
    type: String,
    required: true
  },
  icon: {
    type: String,
    default: ''
  }
});

const route = useRoute();
const showBreadcrumbs = computed(() => route.path !== '/');
</script>

<style scoped>
.page-header {
  margin-bottom: 32px;
}

.page-title {
  font-size: 32px;
  font-weight: 700;
  color: rgba(0, 0, 0, 0.87);
  margin-top: 16px;
  display: flex;
  align-items: center;
}
</style> 