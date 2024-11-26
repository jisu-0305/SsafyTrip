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

    public PagedQuestionResponseDto selectAllQuestions(int userId, String userRole,int page, int size) {
        int offset = (page - 1) * size;
        int totalCount, totalPage;
        List<QuestionsDto> questionsList;

        if(userRole.equals("ROLE_USER")){
            totalCount = questionMapper.getCountQuestions(userId);
            totalPage = (int) Math.ceil((double) totalCount / size);
            questionsList = questionMapper.getQuestionsByUserId((int)userId,offset,size);
        }else{
            totalCount = questionMapper.getCountQuestionsForAdmin();
            totalPage = (int) Math.ceil((double) totalCount / size);
            System.out.println("QuestionService.selectAllQuestions");
            System.out.println(totalCount);
            questionsList = questionMapper.getQuestionsByAdmin(offset, size);
        }


        PagedQuestionResponseDto pagedQuestionResponseDto = new PagedQuestionResponseDto();
        pagedQuestionResponseDto.setQuestionsList(questionsList);
        pagedQuestionResponseDto.setTotalPages(totalPage);
        pagedQuestionResponseDto.setTotalCount(totalCount);

        return pagedQuestionResponseDto;
    }


    @Transactional
    public QuestionDetailResDto selectByUserAndQuestionId(int userId, String role, int questionId) {
        Boolean isAnswered = questionMapper.selectIsAnswered(questionId);

        QuestionDetailResDto questionDetailResDto;
        if(role.equals("ROLE_USER")){
            if(isAnswered){
                questionDetailResDto = questionMapper.selectQuestionAndAnswerById(userId, questionId);
            }else{
                questionDetailResDto = questionMapper.selectQuestionById(userId,questionId);
            }
        }else{
            if(isAnswered){
                System.out.println("QuestionService.selectByUserAndQuestionId");
                System.out.println("답변이 달린 경우(관리자)");
                questionDetailResDto = questionMapper.selectQuestionAndAnswerByIdForAdmin(questionId);
                System.out.println(questionDetailResDto);
            }else{

                System.out.println("QuestionService.selectByUserAndQuestionId");
                System.out.println("답변이 달리지 않은 경우(관리자)");
                questionDetailResDto = questionMapper.selectQuestionForAdmin(questionId);
                System.out.println(questionDetailResDto);
            }
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
