  <script setup>
  import { ref, computed, onMounted, watch } from 'vue'
  import Quill from 'quill'
  import 'quill/dist/quill.snow.css'
  import PageHeader from "@/components/common/PageHeader.vue"

  const props = defineProps({
    type: {
      type: String,
      required: true,
      validator: (value) => ['notice', 'qna', 'review'].includes(value)
    },
    title: {
      type: String,
      required: true
    },
    initialData: {
      type: Object,
      default: () => null
    },
    isEdit: {
      type: Boolean,
      default: false
    },
    // 추가 필드 설정
    additionalFields: {
      type: Array,
      default: () => []
      // [{ key: 'category', label: '카테고리', type: 'select', options: [] }]
    },
    // 에디터 설정 커스터마이징
    editorOptions: {
      type: Object,
      default: () => ({
        placeholder: '내용을 입력하세요...',
        height: 300
      })
    },
    // 버튼 라벨 커스터마이징
    buttonLabels: {
      type: Object,
      default: () => ({
        submit: '등록',
        edit: '수정',
        cancel: '취소'
      })
    }
  })

  const emit = defineEmits(['submit'])
  const editorElement = ref(null)
  const quillEditor = ref(null)

  const form = ref(null)
  const title = ref('')
  const content = ref('')
  const imageUrl = ref('')

  const formData = computed(() => ({
    title: title.value,
    content: content.value,
    imageUrl: imageUrl.value
  }))

  // Quill 에디터 옵션 설정
  const toolbarOptions = [
    ['bold', 'italic', 'underline', 'strike'],
    ['blockquote', 'code-block'],
    [{ 'header': 1 }, { 'header': 2 }],
    [{ 'list': 'ordered'}, { 'list': 'bullet' }],
    [{ 'script': 'sub'}, { 'script': 'super' }],
    [{ 'indent': '-1'}, { 'indent': '+1' }],
    [{ 'direction': 'rtl' }],
    [{ 'size': ['small', false, 'large', 'huge'] }],
    [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
    [{ 'color': [] }, { 'background': [] }],
    [{ 'font': [] }],
    [{ 'align': [] }],
  ]

  onMounted(() => {
    // Quill 인스턴스 생성
    quillEditor.value = new Quill(editorElement.value, {
      modules: {
        toolbar: toolbarOptions
      },
      placeholder: '내용을 입력하세요...',
      theme: 'snow'
    })

    // 에디터 내용 변경 감지
    quillEditor.value.on('text-change', () => {
      content.value = quillEditor.value.root.innerHTML
    })
  })

  const isFormValid = computed(() => {
    return title.value.trim() && content.value.trim()
  })

  const handleSubmit = () => {
    if (!isFormValid.value) {
      alert('제목과 내용을 모두 입력해주세요!')
      return
    }
    emit('submit', formData.value)
  }

  // 수정 모드일 때 초기 데이터 설정
  watch(() => props.initialData, (newData) => {
    if (newData && props.isEdit) {
      title.value = newData.title
      imageUrl.value = newData.imageUrl
      // Quill 에디터에 내용 설정
      if (quillEditor.value) {
        quillEditor.value.root.innerHTML = newData.content
        content.value = newData.content
      }
    }
  }, { immediate: true })
  </script>

  <template>
            <v-card class="mt-6">
              <v-card-text>
                <v-form ref="form" @submit.prevent="handleSubmit">
                  <!-- v-model을 직접 ref에 바인딩 -->
                  <v-text-field
                    v-model="title"
                    label="제목"
                    required
                    :rules="[v => !!v || '제목을 입력해주세요']"
                    variant="outlined"
                    class="mb-4"
                    hide-details="auto"
                  />

                  <!-- Quill 에디터 -->
                  <div class="editor-wrapper mb-4">
                    <div ref="editorElement"></div>
                  </div>

                  <v-text-field
                    v-model="imageUrl"
                    label="대표 이미지 URL (선택사항)"
                    variant="outlined"
                    class="mb-6"
                    hide-details="auto"
                  />

                  <div class="d-flex justify-end gap-2">
                    <v-btn
                      color="error"
                      variant="outlined"
                      :to="{ name: type }"
                    >
                      {{ buttonLabels.cancel }}
                    </v-btn>
                    <v-btn
                      color="primary"
                      type="submit"
                      :disabled="!isFormValid"
                    >
                      {{ isEdit ? buttonLabels.edit : buttonLabels.submit }}
                    </v-btn>
                  </div>
                </v-form>
              </v-card-text>
            </v-card>
  </template>

  <style scoped>
  .editor-wrapper {
    height: 400px;
    border-radius: 4px;
  }

  :deep(.ql-container) {
    height: 300px;
    font-size: 16px;
    font-family: inherit;
  }

  :deep(.ql-toolbar) {
    border-top-left-radius: 4px;
    border-top-right-radius: 4px;
  }

  :deep(.ql-container) {
    border-bottom-left-radius: 4px;
    border-bottom-right-radius: 4px;
  }

  .gap-2 {
    gap: 8px;
  }
  </style>