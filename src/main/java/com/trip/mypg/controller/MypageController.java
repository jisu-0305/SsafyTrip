package com.trip.mypg.controller;

import com.trip.mypg.dto.DeleteRequestDTO;
import com.trip.mypg.dto.MemberUpdateDTO;
import com.trip.mypg.service.MypageServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("mypage")
public class MypageController {
    private final MypageServiceImpl mypageService;

    // 회원 정보 수정
    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody MemberUpdateDTO updateRequest, HttpSession session) throws Exception {
        long userId = (long) session.getAttribute("userId");
        mypageService.updateUser(updateRequest, userId);

        return ResponseEntity.ok("회원 수정 성공");
    }

    // 회원 비활성화
    @DeleteMapping("/delete")
    public ResponseEntity<String> deactivateUser(@RequestBody DeleteRequestDTO deleteRequestDTO, HttpSession session) {
        mypageService.deactivateUser(deleteRequestDTO);

        session.invalidate();
        return ResponseEntity.ok("회원 비활성화 성공");
    }

}
