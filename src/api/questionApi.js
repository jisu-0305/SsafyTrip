import myaxios from "@/api/axios.js";

export const questionApi = {
  // 질문 목록 조회
  getQuestions: () => {
    return myaxios.get('/mypage/questions')
  },

  // 질문 생성
  createQuestion: (questionData) => {
    return myaxios.post('/mypage/questions', {
      title: questionData.title,
      content: questionData.content
    })
  },

  // 답변 등록
  createQuestionAnswer: (questionId, answerData) => {
    return myaxios.post(`/mypage/questions/${questionId}/answer`, {
      content: answerData.content
    })
  },

  // 질문 상세 조회
  getQuestionById: (questionId) => {
    return myaxios.get(`/mypage/questions/${questionId}`)
  }
}