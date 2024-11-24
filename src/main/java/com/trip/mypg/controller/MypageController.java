package com.trip.mypg.controller;

import com.trip.member.dto.RegisterRequestDTO;
import com.trip.mypg.dto.DeleteRequestDTO;
import com.trip.mypg.service.MypageServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("mypage")
public class MypageController {
    private final MypageServiceImpl mypageService;

    // 회원 탈퇴 API
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestBody DeleteRequestDTO deleteRequestDTO, HttpSession session) {
        boolean isDeleted = mypageService.deleteUser(deleteRequestDTO);

        if (isDeleted) {
            session.invalidate();
            return ResponseEntity.ok("회원 탈퇴 성공");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("비밀번호가 일치하지 않거나 이메일을 찾을 수 없습니다.");
        }

    }


}
