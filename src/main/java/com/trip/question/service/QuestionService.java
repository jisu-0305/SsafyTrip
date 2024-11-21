package com.trip.question.service;

import com.trip.question.dto.QuestionDetailResDto;
import com.trip.question.dto.QuestionInsertDto;
import com.trip.question.dto.QuestionInsertReqDto;
import com.trip.question.dto.QuestionsDto;
import com.trip.question.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionMapper questionMapper;

    public Boolean insertQuestion(QuestionInsertReqDto req, int userId) {
        QuestionInsertDto questionInsertDto = new QuestionInsertDto(userId, req.getTitle(), req.getContent());

        int result = questionMapper.insertQuestion(questionInsertDto);

        System.out.println("QuestionService.insertQuestion");
        System.out.println(result);

        return true;
    }

    public List<QuestionsDto> selectAllQuestions(int userId) {
        List<QuestionsDto> questionsList = questionMapper.getQuestionsByUserId((int)userId);

        System.out.println("QuestionService.selectAllQuestions");
        System.out.println("개수: " + questionsList.size());

        return questionsList;
    }


    public QuestionDetailResDto selectByUserAndQuestionId(int userId, int questionId) {
        QuestionDetailResDto questionDetailResDto = questionMapper.getQuestionByIdAndUserId(userId, questionId);

        System.out.println("QuestionService.selectByUserAndQuestionId");
        System.out.println(questionDetailResDto);

        return questionDetailResDto;

    }
}
