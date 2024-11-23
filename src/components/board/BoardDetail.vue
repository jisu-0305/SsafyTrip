<script setup>
import { defineProps, defineEmits } from 'vue'
import DOMPurify from 'dompurify'
import { computed } from 'vue'

const props = defineProps({
  type: {
    type: String,
    required: true,
    validator: (value) => ['notice', 'qna', 'review'].includes(value)
  },
  article: {
    type: Object,
    required: true
  },
  isAdmin: {
    type: Boolean,
    default: false
  },
  // 추가 필드 설정
  additionalFields: {
    type: Array,
    default: () => []
    // [{ key: 'author', label: '작성자' }, { key: 'status', label: '상태' }]
  },
  // 버튼 커스터마이징
  buttons: {
    type: Array,
    default: () => ['edit', 'delete', 'list']
  },
  // 버튼 라벨 커스터마이징
  buttonLabels: {
    type: Object,
    default: () => ({
      edit: '수정',
      delete: '삭제',
      list: '목록'
    })
  }
})

const emit = defineEmits(['edit', 'delete', 'list'])

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
  if (field.formatter) {
    return field.formatter(value)
  }
  return value
}
</script>

<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" md="8">
        <v-card elevation="3" class="article-card">
          <!-- 제목 영역 -->
          <v-card-title class="text-h4 pa-6 title-area">
            <div class="title-wrapper">
              {{ article?.title }}
            </div>
            <!-- 추가 필드 표시 -->
            <div class="additional-fields mt-2">
              <div 
                v-for="field in additionalFields" 
                :key="field.key"
                class="text-caption d-inline-block me-4"
              >
                {{ field.label }}: {{ formatValue(article[field.key], field) }}
              </div>
              <div class="text-caption d-inline-block">
                작성일: {{ formattedDate }}
              </div>
            </div>
          </v-card-title>
          
          <v-divider></v-divider>
          
          <!-- 본문 영역 -->
          <v-card-text class="pa-6">
            <div class="ql-snow">
              <div 
                class="ql-editor content-area"
                v-html="sanitizedContent"
              ></div>
            </div>
          </v-card-text>
          
          <v-divider></v-divider>
          
          <!-- 버튼 영역 -->
          <v-card-actions class="pa-4">
            <v-spacer></v-spacer>
            <template v-if="isAdmin && buttons.includes('edit')">
              <v-btn
                color="primary"
                variant="outlined"
                class="mx-2"
                @click="emit('edit')"
                prepend-icon="mdi-pencil"
              >
                {{ buttonLabels.edit }}
              </v-btn>
            </template>
            <template v-if="isAdmin && buttons.includes('delete')">
              <v-btn
                color="error"
                variant="outlined"
                class="mx-2"
                @click="emit('delete')"
                prepend-icon="mdi-delete"
              >
                {{ buttonLabels.delete }}
              </v-btn>
            </template>
            <template v-if="buttons.includes('list')">
              <v-btn
                color="grey"
                variant="outlined"
                class="mx-2"
                @click="emit('list')"
                prepend-icon="mdi-format-list-bulleted"
              >
                {{ buttonLabels.list }}
              </v-btn>
            </template>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
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
