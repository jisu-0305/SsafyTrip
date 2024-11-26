<script setup>
import { defineProps, defineEmits } from 'vue'
import DOMPurify from 'dompurify'
import { computed } from 'vue'
import { storeToRefs } from 'pinia'
import { useLoadingStore } from '@/stores/loadingStore'

const props = defineProps({
  // 기본 데이터
  article: {
    type: Object,
    required: true
  },
  // 로딩 상태 키
  loadingKey: {
    type: String,
    required: true
  },
  // 추가 필드 설정
  additionalFields: {
    type: Array,
    default: () => []
  },
  // 버튼 설정
  buttons: {
    type: Array,
    default: () => []
  },
  // 버튼 권한 설정
  buttonPermissions: {
    type: Object,
    default: () => ({})
  },
  // 버튼 라벨 커스터마이징
  buttonLabels: {
    type: Object,
    default: () => ({})
  },
  // 답변 영역 설정 (Q&A용)
  answer: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['click-button'])

const loadingStore = useLoadingStore()
const { isComponentLoading } = storeToRefs(loadingStore)

// HTML 컨텐츠 sanitize
const sanitizedContent = computed(() => {
  return DOMPurify.sanitize(props.article?.content || '')
})

const formattedDate = computed(() => {
  if (!props.article?.createdAt) return ''
  return new Date(props.article.createdAt).toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
})

const formatValue = (value, field) => {
  if (!value) return ''
  
  if (field.formatter) {
    return field.formatter(value)
  }
  return value
}

const handleButtonClick = (buttonType) => {
  emit('click-button', buttonType)
}
</script>

<template>
  <v-card elevation="3" class="article-card">
    <template v-if="isComponentLoading(loadingKey)">
      <v-card-text class="d-flex justify-center align-center" style="min-height: 400px">
        <v-progress-circular indeterminate color="primary" size="64" />
      </v-card-text>
    </template>
    <template v-else-if="article">
      <!-- 제목 영역 -->
      <v-card-title class="text-h4 pa-6 title-area">
        <div class="title-wrapper">
          {{ article.title }}
        </div>
        <!-- 추가 필드 표시 -->
        <div class="additional-fields mt-2">
          <div 
            v-for="field in additionalFields" 
            :key="field.key"
            class="text-caption d-inline-block me-4"
          >
            <template v-if="article[field.key]">
              {{ field.label }}: {{ formatValue(article[field.key], field) }}
            </template>
          </div>
          <div class="text-caption d-inline-block">
            작성일: {{ formattedDate }}
          </div>
        </div>
      </v-card-title>
      
      <v-divider />
      
      <!-- 본문 영역 -->
      <v-card-text class="pa-6">
        <div class="ql-snow">
          <div class="ql-editor content-area" v-html="sanitizedContent" />
        </div>
      </v-card-text>
      
      <v-divider />
      
      <!-- 버튼 영역 -->
      <v-card-actions class="pa-4">
        <v-spacer />
        <template v-for="button in buttons" :key="button">
          <v-btn
            v-if="!buttonPermissions[button] || buttonPermissions[button]"
            :color="button === 'delete' ? 'error' : button === 'list' ? 'black' : 'blue'"
            :variant="button === 'list' ? 'outlined' : 'tonal'"
            class="mx-2"
            @click="handleButtonClick(button)"
            :prepend-icon="
              button === 'edit' ? 'mdi-pencil' :
              button === 'delete' ? 'mdi-delete' :
              button === 'answer' ? 'mdi-pencil-plus' :
              'mdi-format-list-bulleted'
            "
          >
            {{ buttonLabels[button] }}
          </v-btn>
        </template>
      </v-card-actions>
    </template>
    <template v-else>
      <v-card-text class="text-center pa-6">
        데이터를 불러올 수 없습니다.
      </v-card-text>
    </template>

    <!-- 답변 영역 (Q&A용) -->
    <template v-if="answer">
      <v-divider />
      <v-card-title class="text-h6 pa-4">
        답변 내용
        <div class="text-caption mt-1">
          답변일: {{ new Date(answer.createdAt).toLocaleDateString() }}
          ({{ answer.authorEmail }})
        </div>
      </v-card-title>
      <v-card-text class="pa-4" v-html="DOMPurify.sanitize(answer.content)" />
    </template>
  </v-card>
</template>

<style scoped>
.article-card {
  border-radius: 8px;
}

.title-area {
  background-color: #f8f9fa;
  white-space: normal;
  word-break: break-word;
}

.title-wrapper {
  line-height: 1.4;
  overflow-wrap: break-word;
}

.content-area {
  min-height: 300px;
  font-size: 16px;
  line-height: 1.8;
}

/* Quill 에디터 스타일 적용 */
:deep(.ql-editor) {
  padding: 0;
}

:deep(.ql-editor h1) {
  font-size: 2em;
  margin-bottom: 1rem;
}

:deep(.ql-editor h2) {
  font-size: 1.5em;
  margin-bottom: 0.8rem;
}

:deep(.ql-editor p) {
  margin-bottom: 1rem;
}

:deep(.ql-editor ul),
:deep(.ql-editor ol) {
  padding-left: 1.5rem;
  margin-bottom: 1rem;
}

:deep(.ql-editor img) {
  max-width: 100%;
  height: auto;
  margin: 1rem 0;
}

:deep(.ql-editor blockquote) {
  border-left: 4px solid #ccc;
  padding-left: 1rem;
  margin: 1rem 0;
  color: #666;
}
</style>
