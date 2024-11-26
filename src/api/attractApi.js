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
      size: params.size || 5,
      sortBy: params.sortBy || 'name',
    }
  });
}

// 특정 관광지 상세 정보 조회 API
function getAttractionDetail(attractionId) {
  return myaxios.get(`/attractions/detail/${attractionId}`);
}

// 구군 목록 조회 API
function getSidoList() {
  return myaxios.get(`/attractions/sidos`);
}

// 구군 목록 조회 API
function getGugunList(sidoId) {
  return myaxios.get(`/attractions/guguns/${sidoId}`);
}

// 관광지 초기 데이터 로드 API
function getInitialAttractions() {
  return myaxios.get('/attractions/init');
}

// 관광지 좋아요 추가 API
function addFavorite(attractionId) {
  return myaxios.post(`/favorites/${attractionId}`);
}

// 관광지 좋아요 취소 API
function deleteFavorite(attractionId) {
  return myaxios.delete(`/favorites/${attractionId}`);
}

export {
  listAttractions,
  getAttractionDetail,
  getGugunList,
  addFavorite,
  deleteFavorite,
  getInitialAttractions,
  getSidoList
}; 