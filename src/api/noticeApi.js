import myaxios from "@/api/axios.js";

// 공지사항 목록 조회 API (페이징)
function getNoticesAxios(page = 1, size = 10, keyword = '') {
  return myaxios.get('/notices', {
    params: {
      page,
      size,
      keyword
    }
  });
}

// 특정 공지사항 조회 API
function getNoticeByIdAxios(noticeId) {
  return myaxios.get(`/notices/${noticeId}`);
}

// 공지사항 작성 API
function createNoticeAxios(noticeData) {
  return myaxios.post('/notices', {
    title: noticeData.title,
    content: noticeData.content,
    imageUrl: noticeData.imageUrl,
    userId: noticeData.userId,
  });
}

// 공지사항 수정 API
function updateNoticeAxios(noticeId, updateData) {
  return myaxios.put(`/notices/${noticeId}`, {
    title: updateData.title,
    content: updateData.content,
    imageUrl: updateData.imageUrl
  });
}

// 공지사항 삭제 API
function deleteNoticeAxios(noticeId) {
  return myaxios.delete(`/notices/${noticeId}`);
}

export {
  getNoticesAxios,
  getNoticeByIdAxios,
  createNoticeAxios,
  updateNoticeAxios,
  deleteNoticeAxios
};