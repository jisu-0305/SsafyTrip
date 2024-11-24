import myaxios from "@/api/axios.js";

// 관광지 검색 API
function listAttractions(params) {
  return myaxios.get('/attractions/search', {
    params: {
      sidoCode: params.sidoCode,
      gugunCode: params.gugunCode,
      type: params.contentTypeId,  
      word: params.keyword,        
      page: params.page || 1,
      size: params.size || 10
    }
  });
}

// 특정 관광지 상세 정보 조회 API
function getAttractionDetail(attractionId) {
  return myaxios.get(`/attractions/detail/${attractionId}`);
}

// 구군 목록 조회 API
function getGugunList(sidoId) {
  return myaxios.get(`/attractions/guguns/${sidoId}`);
}

// 관광지 댓글 작성 API
function createComment(contentId, commentData) {
  return myaxios.post(`/attractions/${contentId}/comments`, commentData);
}

// 관광지 댓글 삭제 API
function deleteComment(contentId, commentId) {
  return myaxios.delete(`/attractions/${contentId}/comments/${commentId}`);
}

// 관광지 좋아요 추가 API
function addFavorite(contentId) {
  return myaxios.post(`/attractions/${contentId}/favorite`);
}

// 관광지 좋아요 취소 API
function removeFavorite(contentId) {
  return myaxios.delete(`/attractions/${contentId}/favorite`);
}

// 관광지 초기 데이터 로드 API
function getInitialAttractions() {
  return myaxios.get('/attractions/init');
}

export {
  listAttractions,
  getAttractionDetail,
  getGugunList,
  createComment,
  deleteComment,
  addFavorite,
  removeFavorite,
  getInitialAttractions
}; 