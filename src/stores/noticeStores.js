import { defineStore } from 'pinia';
import { 
  getNoticesAxios, 
  getNoticeByIdAxios, 
  createNoticeAxios, 
  updateNoticeAxios, 
  deleteNoticeAxios 
} from '@/api/noticeApi';

export const useNoticeStore = defineStore('notice', {
  state: () => ({
    notices: [],
    currentNotice: null,
    totalPages: 0,
    totalElements: 0,
    currentPage: 1,
    searchKeyword: '',
  }),

  actions: {
    async fetchNotices(page = 1, size = 10, keyword = '') {
      try {
        const response = await getNoticesAxios(page, size, keyword);
        this.notices = response.data.noticeList;
        this.totalPages = response.data.totalPages;
        this.totalElements = response.data.totalElements;
        this.currentPage = page;
        this.searchKeyword = keyword;
      } catch (error) {
        console.error('공지사항 목록 조회 실패:', error);
        throw error;
      }
    },

    async fetchNoticeById(noticeId) {
      try {
        const response = await getNoticeByIdAxios(noticeId);
        this.currentNotice = response.data;
        return response.data;
      } catch (error) {
        console.error('공지사항 상세 조회 실패:', error);
        throw error;
      }
    },

    async createNotice(noticeData) {
      try {
        const response = await createNoticeAxios(noticeData);
        return response.data;
      } catch (error) {
        console.error('공지사항 생성 실패:', error);
        throw error;
      }
    },

    async updateNotice(noticeId, updateData) {
      try {
        const response = await updateNoticeAxios(noticeId, updateData);
        return response.data;
      } catch (error) {
        console.error('공지사항 수정 실패:', error);
        throw error;
      }
    },

    async deleteNotice(noticeId) {
      try {
        const response = await deleteNoticeAxios(noticeId);
        return response.data;
      } catch (error) {
        console.error('공지사항 삭제 실패:', error);
        throw error;
      }
    }
  }
});