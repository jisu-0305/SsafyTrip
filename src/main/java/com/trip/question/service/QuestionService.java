package com.trip.question.service;

import com.trip.question.dto.*;
import com.trip.question.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionMapper questionMapper;

    public Boolean insertQuestion(QuestionInsertReqDto req, int userId) {
        QuestionInsertDto questionInsertDto = new QuestionInsertDto(userId, req.getTitle(), req.getContent());

        int result = questionMapper.insertQuestion(questionInsertDto);

        return true;
    }

    public List<QuestionsDto> selectAllQuestions(int userId) {
        List<QuestionsDto> questionsList = questionMapper.getQuestionsByUserId((int)userId);

        return questionsList;
    }


    @Transactional
    public QuestionDetailResDto selectByUserAndQuestionId(int userId, int questionId) {
        Boolean isAnswered = questionMapper.selectIsAnswered(userId, questionId);

        QuestionDetailResDto questionDetailResDto;
        if(isAnswered){
            questionDetailResDto = questionMapper.selectQuestionAndAnswerById(userId, questionId);
        }else{
            questionDetailResDto = questionMapper.selectQuestionById(userId,questionId);
        }


        return questionDetailResDto;

    }

    @Transactional
    public Boolean insertAnswer(QuestionAnswerReqDto req){
        int insertResult = questionMapper.insertAnswer(req);

        int updateResult = 0;
        if(insertResult == 1){
            updateResult = questionMapper.updateQuestionIsAnswered(req.getQuestionId());

        }

        if(updateResult == 1) return true;
        else return false;

    }

}
