package com.trip.question.mapper;

import com.trip.question.dto.QuestionDetailResDto;
import com.trip.question.dto.QuestionInsertDto;
import com.trip.question.dto.QuestionsDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionMapper {
    int insertQuestion(QuestionInsertDto questionInsertDto);

    List<QuestionsDto> getQuestionsByUserId(int userId);

    QuestionDetailResDto getQuestionByIdAndUserId(@Param("questionId") int questionId, @Param("userId") int userId);
}