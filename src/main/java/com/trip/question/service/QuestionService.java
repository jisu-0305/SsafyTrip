package com.trip.question.service;

import com.trip.question.dto.PagedQuestionResponseDto;
import com.trip.question.dto.QuestionAnswerReqDto;
import com.trip.question.dto.QuestionDetailResDto;
import com.trip.question.dto.QuestionInsertReqDto;

public interface QuestionService {
    Boolean insertQuestion(QuestionInsertReqDto req, int userId);
    PagedQuestionResponseDto selectAllQuestions(int userId, String userRole, int page, int size);
    QuestionDetailResDto selectByUserAndQuestionId(int userId, String role, int questionId);
    Boolean insertAnswer(QuestionAnswerReqDto req);
}