<script setup>
import { onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useNoticeStore } from '@/stores/noticeStores'
import BoardWrite from '@/components/board/BoardWrite.vue'

const route = useRoute()
const router = useRouter()
const noticeStore = useNoticeStore()

onMounted(async () => {
  await noticeStore.fetchNoticeById(route.params.id)
})

const handleSubmit = async (formData) => {
  try {
    const updateData = {
      title: formData.title,
      content: formData.content,
      imageUrl: formData.imageUrl || null
    }
    
    const response = await noticeStore.updateNotice(route.params.id, updateData)
    
    if (response.status === 'success') {
      alert(response.message || '공지사항이 수정되었습니다.')
      router.push({ name: 'notice-detail', params: { id: route.params.id } })
    } else {
      throw new Error(response.message || '수정 실패')
    }
  } catch (error) {
    alert(error.message || '수정 중 오류가 발생했습니다.')
  }
}
</script>

<template>
  <BoardWrite
    type="notice"
    title="공지사항"
    :initial-data="noticeStore.currentNotice"
    :is-edit="true"
    @submit="handleSubmit"
  />
</template>