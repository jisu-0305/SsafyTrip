import { defineStore } from "pinia";
import { ref } from "vue";
import { questionApi } from "@/api/questionApi";

export const useQuestionStore = defineStore("question", () => {
  // 상태
  const questions = ref([]);
  const currentQuestion = ref(null);
  const currentPage = ref(1);
  const totalPages = ref(0);
  const totalElements = ref(0);
  const searchKeyword = ref("");
  const isAnsweredQuestionStore = ref(false);

  // 액션
  const fetchQuestions = async (page = 1, size = 10, keyword = "") => {
    try {
      const response = await questionApi.getQuestions(page, size, keyword);
      questions.value = response.data.questionsList;
      totalPages.value = response.data.totalPages;
      totalElements.value = response.data.totalCount;
    } catch (error) {
      console.error("질문 목록 조회 실패:", error);
      throw error;
    }
  };

  const fetchQuestionById = async (questionId) => {
    try {
      const response = await questionApi.getQuestionById(questionId);
      currentQuestion.value = response.data;
      return response.data;
    } catch (error) {
      console.error("질문 상세 조회 실패:", error);
      throw error;
    }
  };

  const createQuestion = async (questionData) => {
    try {
      const response = await questionApi.createQuestion(questionData);
      return response.data;
    } catch (error) {
      console.error("질문 생성 실패:", error);
      throw error;
    }
  };

  const createQuestionAnswer = async (questionId, answerData) => {
    try {
      const response = await questionApi.createQuestionAnswer(
        questionId,
        answerData
      );
      return response.data;
    } catch (error) {
      console.error("답변 등록 실패:", error);
      throw error;
    }
  };

  return {
    // 상태
    questions,
    currentQuestion,
    currentPage,
    totalPages,
    totalElements,
    searchKeyword,
    isAnsweredQuestionStore,

    // 액션
    fetchQuestions,
    fetchQuestionById,
    createQuestion,
    createQuestionAnswer,
  };
});
