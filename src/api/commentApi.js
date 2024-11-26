import myaxios from "@/api/axios.js";

// 댓글 작성 API
function createComment(attractionId, commentData) {
  return myaxios.post(`/comments/${attractionId}`, {
    email: commentData.email,
    content: commentData.content
  }, {
    withCredentials: true
  });
}

// 댓글 삭제 API
function deleteComment(commentId) {
  return myaxios.delete(`/comments/${commentId}`);
}

//내 댓글 조회 API
function getMyComments() {
  console.log("getMyComments API 호출");
  return myaxios.get('/mypage/comments', {
  });
}

export const commentApi = {
  createComment,
  deleteComment,
  getMyComments,
};

