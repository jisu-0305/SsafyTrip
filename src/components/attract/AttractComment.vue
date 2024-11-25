<script setup>
import { ref, onMounted } from 'vue';
import { useCommentStore } from '@/stores/commentStore';
import { useAuthStore } from '@/stores/authStores';
import { useAttractionDetailStore } from '@/stores/attractionDetailStore';
import { storeToRefs } from 'pinia';
import { getInitials, getProfileColor } from '@/utils/profileUtils';

const props = defineProps({
  attractionId: {
    type: Number,
    required: true
  }
});

const commentStore = useCommentStore();
const authStore = useAuthStore();
const attractionDetailStore = useAttractionDetailStore();
const { comments } = storeToRefs(attractionDetailStore);
const { loading, error } = storeToRefs(commentStore);
const commentContent = ref('');

const handleSubmit = async () => {
  if (!commentContent.value.trim()) return;
  
  try {
    await commentStore.addComment(props.attractionId, {
      content: commentContent.value,
      email: authStore.user.email
    });
    commentContent.value = '';
    await attractionDetailStore.fetchAttractionDetail(props.attractionId);
  } catch (error) {
    console.error('댓글 작성 실패:', error);
    alert('댓글 작성에 실패했습니다.');
  }
};

const handleDelete = async (commentId) => {
  if (confirm('댓글을 삭제하시겠습니까?')) {
    try {
      await commentStore.removeComment(props.attractionId, commentId);
      await attractionDetailStore.fetchAttractionDetail(props.attractionId);
    } catch (error) {
      console.error('댓글 삭제 실패:', error);
      alert('댓글 삭제에 실패했습니다.');
    }
  }
};

const isCommentOwner = (commentEmail) => {
  return authStore.user?.email === commentEmail;
};
</script>

<template>
  <v-card>
    <v-card-title class="d-flex align-center">
      댓글 
      <v-chip class="ml-2" color="primary">{{ comments?.length || 0 }}</v-chip>
    </v-card-title>
    
    <v-divider></v-divider>
    
    <!-- 댓글 작성 영역 -->
    <v-card-text class="py-4">
      <v-textarea
        v-model="commentContent"
        :placeholder="authStore.isLoggedIn ? '댓글을 작성해주세요.' : '로그인 후 댓글을 남겨주세요.'"
        :disabled="!authStore.isLoggedIn"
        :rules="[v => v.length <= 1000 || '최대 1000자까지 입력 가능합니다.']"
        counter
        maxlength="1000"
        rows="3"
        variant="outlined"
      ></v-textarea>
      
      <div class="d-flex justify-end mt-2">
        <v-btn
          color="primary"
          :disabled="!authStore.isLoggedIn || !commentContent"
          @click="handleSubmit"
        >
          댓글 작성
        </v-btn>
      </div>
    </v-card-text>
    
    <!-- 댓글 목록 -->
    <v-list v-if="comments?.length">
      <v-list-item
        v-for="comment in comments"
        :key="comment.id"
        class="py-4"
      >
        <template v-slot:prepend>
          <v-avatar :color="getProfileColor(comment.email)">
            {{ getInitials(comment.email) }}
          </v-avatar>
        </template>
        
        <v-list-item-title class="d-flex justify-space-between align-center">
          <span class="font-weight-bold">{{ comment.email }}</span>
          <v-icon
            v-if="isCommentOwner(comment.email)"
            icon="mdi-delete"
            size="small"
            class="cursor-pointer"
            @click="handleDelete(comment.id)"
          ></v-icon>
        </v-list-item-title>
        
        <v-list-item-subtitle class="text-body-1 mb-1">
          {{ comment.content }}
        </v-list-item-subtitle>
        <v-list-item-subtitle class="text-caption">
          {{ new Date(comment.createdAt).toLocaleString() }}
        </v-list-item-subtitle>
      </v-list-item>
    </v-list>
    
    <v-card-text v-else class="text-center py-4">
      작성된 댓글이 없습니다.
    </v-card-text>
  </v-card>
</template>

<style scoped>
.cursor-pointer {
  cursor: pointer;
}
</style>