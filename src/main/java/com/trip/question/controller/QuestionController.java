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

        List<QuestionsDto> res = questionService.selectAllQuestions(user.getUserId());

        return ResponseEntity.ok(res);

    }


    // 리스트 추가
    @PostMapping("/questions")
    public ResponseEntity<Boolean> insertQuestions(@RequestBody QuestionInsertReqDto questionInsertReqDto, HttpSession session){
        AuthorizedUserDto user = isAuthenticated(session);

        Boolean isSuccess = questionService.insertQuestion(questionInsertReqDto,user.getUserId());

        return ResponseEntity.ok(isSuccess);

    }


    // 1:1 문의 상세보기
    @GetMapping("/questions/{questionId}")
    public ResponseEntity<QuestionDetailResDto> getQuestionById(@PathVariable int questionId, HttpSession session){
        AuthorizedUserDto user = isAuthenticated(session);

        // userId와 questionId 둘 다 동일해야 보내주기
        QuestionDetailResDto res = questionService.selectByUserAndQuestionId(user.getUserId(), questionId);

        return ResponseEntity.ok(res);

    }



    @PostMapping("/questions/{questionId}/answer")
    public ResponseEntity<Boolean> insertQuestionAnswer(@PathVariable int questionId,
                                                        @RequestBody QuestionAnswerContentDto questionAnswerContentDto,
                                                        HttpSession session)
    {
        AuthorizedUserDto user = isAuthenticated(session);

        questionService.insertAnswer(new QuestionAnswerReqDto(user.getUserId(), questionId, questionAnswerContentDto.getContent()));

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
