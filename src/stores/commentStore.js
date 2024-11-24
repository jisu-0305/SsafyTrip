import { defineStore } from 'pinia';
import { ref } from 'vue';
import { commentApi } from '@/api/commentApi';

export const useCommentStore = defineStore('comment', () => {
  const comments = ref([]);
  const loading = ref(false);
  const error = ref(null);

  // 댓글 목록 조회
  const fetchComments = async (attractionId) => {
    try {
      loading.value = true;
      error.value = null;
      const response = await commentApi.getComments(attractionId);
      comments.value = response.data;
    } catch (err) {
      console.error('댓글 목록 조회 실패:', err);
      error.value = '댓글을 불러오는데 실패했습니다.';
      comments.value = [];
    } finally {
      loading.value = false;
    }
  };

  // 댓글 작성
  const addComment = async (attractionId, commentData) => {
    try {
      loading.value = true;
      error.value = null;
      await commentApi.createComment(attractionId, commentData);
      await fetchComments(attractionId); // 댓글 목록 새로고침
    } catch (err) {
      console.error('댓글 작성 실패:', err);
      error.value = '댓글 작성에 실패했습니다.';
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // 댓글 삭제
  const removeComment = async (attractionId, commentId) => {
    try {
      loading.value = true;
      error.value = null;
      await commentApi.deleteComment(commentId);
      await fetchComments(attractionId); // 댓글 목록 새로고침
    } catch (err) {
      console.error('댓글 삭제 실패:', err);
      error.value = '댓글 삭제에 실패했습니다.';
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // 상태 초기화
  const clearComments = () => {
    comments.value = [];
    error.value = null;
    loading.value = false;
  };

  return {
    comments,
    loading,
    error,
    fetchComments,
    addComment,
    removeComment,
    clearComments
  };
}); 