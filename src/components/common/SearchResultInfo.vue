<!-- components/common/SearchResultInfo.vue -->
<script setup>
import { computed } from 'vue'

const props = defineProps({
  totalCount: {
    type: Number,
    required: true
  }
})

const resultText = computed(() => `총 ${props.totalCount}건이 검색되었습니다.`)
</script>

<template>
  <v-sheet class="mb-4">
    <v-alert
      color="info"
      variant="tonal"
      border="start"
      density="comfortable"
    >
      {{ resultText }}
    </v-alert>
  </v-sheet>
</template>