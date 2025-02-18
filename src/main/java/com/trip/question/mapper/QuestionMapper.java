package com.trip.question.mapper;

import com.trip.question.dto.QuestionAnswerReqDto;
import com.trip.question.dto.QuestionDetailResDto;
import com.trip.question.dto.QuestionInsertDto;
import com.trip.question.dto.QuestionsDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionMapper {
    int insertQuestion(QuestionInsertDto questionInsertDto);

    List<QuestionsDto> getQuestionsByUserId(@Param("userId") int userId, @Param("offset") int offset, @Param("size") int size);

    List<QuestionsDto> getQuestionsByAdmin(@Param("offset") int offset, @Param("size") int size);

    Boolean selectIsAnswered(@Param("questionId") int questionId);

    QuestionDetailResDto selectQuestionById(@Param("userId") int userId, @Param("questionId") int questionId);

    QuestionDetailResDto selectQuestionAndAnswerById(@Param("userId") int userId, @Param("questionId") int questionId);

    int insertAnswer(QuestionAnswerReqDto questionAnswerReqDto);

    int updateQuestionIsAnswered(@Param("questionId") int questionId);

    int getCountQuestions(@Param("userId") int userId);

    int getCountQuestionsForAdmin();

    QuestionDetailResDto selectQuestionForAdmin(@Param("questionId") int questionId);

    QuestionDetailResDto selectQuestionAndAnswerByIdForAdmin(@Param("questionId") int questionId);

}
