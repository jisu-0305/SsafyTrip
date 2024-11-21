package com.trip.question.controller;

import com.trip.question.dto.*;
import com.trip.question.service.QuestionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("mypage")
public class QuestionController {
    @Autowired
    private final QuestionService questionService;

    // 전체 리스트 조회
    @GetMapping("/questions")
    public ResponseEntity<List<QuestionsDto>> getAllQuestions(HttpSession session) {
        AuthorizedUserDto user = isAuthenticated(session);

        System.out.println("QuestionController.getAllQuestions");
        System.out.println(user);

        List<QuestionsDto> res = questionService.selectAllQuestions(user.getUserId());
        return ResponseEntity.ok(res);
    }


    // 리스트 추가
    @PostMapping("/questions")
    public ResponseEntity<Boolean> insertQuestions(@RequestBody QuestionInsertReqDto questionInsertReqDto, HttpSession session){
        AuthorizedUserDto user = isAuthenticated(session);

        System.out.println("QuestionController.insertQuestions");
        System.out.println(user);
        System.out.println(questionInsertReqDto);

        Boolean isSuccess = questionService.insertQuestion(questionInsertReqDto,user.getUserId());

        return ResponseEntity.ok(isSuccess);
    }


    // 1:1 문의 상세보기
    @GetMapping("/questions/{questionId}")
    public ResponseEntity<QuestionDetailResDto> getQuestionById(@PathVariable int questionId, HttpSession session){
        System.out.println("QuestionController.getQuestionById");

        AuthorizedUserDto user = isAuthenticated(session);

        System.out.println("QuestionController.getQuestionById");
        System.out.println(user);
        System.out.println("id: " + questionId);

        // userId와 questionId 둘 다 동일해야 보내주기
        QuestionDetailResDto res = questionService.selectByUserAndQuestionId(user.getUserId(), questionId);

        return ResponseEntity.ok(res);
    }


    // 1:1문의 답변하기(아직 못함)
    @PostMapping("/questions/{questionId}/answer")
    public ResponseEntity<Boolean> insertQuestionAnswer(@PathVariable Long questionId,
                                                        @RequestBody QuestionAnswerReqDto questionAnswerReqDto,
                                                        HttpSession session)
    {
        AuthorizedUserDto user = isAuthenticated(session);

        // 관리자 아이디도 서버에서 받기
        System.out.println("questionId: " + questionId);
        System.out.println("QuestionController.insertQuestionAnswer");
        System.out.println(user);
        System.out.println(questionAnswerReqDto);

        Boolean isSuccess = true;
        return ResponseEntity.ok(isSuccess);
    }


    public AuthorizedUserDto isAuthenticated(HttpSession session) {
        long userId = ((Long)session.getAttribute("userId"));

        String email = (String) session.getAttribute("email");
        String userRole = (String) session.getAttribute("userRole");

        if(userId != 0 && email != null && userRole != null){
            return new AuthorizedUserDto((int)userId, email, userRole);
        }
        return null; // 세션에 'user'가 있으면 인증된 것으로 간주
    }
}
