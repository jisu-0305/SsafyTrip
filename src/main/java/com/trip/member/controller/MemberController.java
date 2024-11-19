package com.trip.member.controller;

import com.trip.member.dto.LoginResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trip.member.service.MemberService;
import com.trip.member.dto.LoginRequestDTO;
import com.trip.board.dto.Member;
import com.trip.member.dto.RegisterRequestDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
@RequestMapping("members")
//@CrossOrigin("*")
@Tag(name = "회원 인증 컨트롤러", description = "로그인, 로그아웃, 회원 정보등 회원관련 서비스를 처리하는 클래스.")
public class MemberController {
	
	private final MemberService memberService;


	@Operation(summary = "회원가입", description = "아이디, 이름, 비밀번호, 이메일을 입력해 회원가입")
	@PostMapping("/register")
	public ResponseEntity<String> register(@Parameter(description = "회원가입 시 필요한 회원정보(아이디, 비밀번호, 이름, 이메일).", required = true) 
						@RequestBody RegisterRequestDTO registerRequestDTO) {

		memberService.registerMember(registerRequestDTO);

		return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
	}


	@Operation(summary = "중복 아이디 검사", description = "입력값을 사용하여 가입된 아이디와 중복되는지 검사.")
	@GetMapping("/{email}")
	public ResponseEntity<Boolean> checkDuplicateEmail(@Parameter(in = ParameterIn.PATH, description = "인증할 회원의 아이디.", required = true)
										  @PathVariable("email") String userId) {

		Boolean isDuplicate = memberService.checkDuplicateEmail(userId);

		return ResponseEntity.ok(isDuplicate);
	}


	@Operation(summary = "로그인", description = "아이디와 비밀번호를 이용하여 로그인 처리")
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@Parameter(description = "로그인 시 필요한 회원정보(아이디, 비밀번호).", required = true)
						@RequestBody LoginRequestDTO loginRequestDTO, HttpSession session) {

		// 로그인한 정보가 없으면 예외처리하기.
		LoginResponseDTO loginResponseDTO = memberService.loginMember(loginRequestDTO);

		session.setAttribute("userId", loginResponseDTO.getUserId());
		session.setAttribute("userRole", loginResponseDTO.getRole());

		return ResponseEntity.ok(loginResponseDTO);
	}

	@Operation(summary = "로그아웃", description = "세션 정보를 이용하여 로그아웃")
	@PostMapping("/logout")
	private ResponseEntity<String> logout(HttpSession session) {

		session.invalidate();

		return ResponseEntity.ok("로그아웃이 완료되었습니다.");
	}
	

	@Operation(summary = "회원 탈퇴", description = "세션 정보를 이용하여 탈퇴 처리")
	@DeleteMapping
	private ResponseEntity<String> deleteAccount(HttpSession session) {
		System.out.println("MemberController.deleteAccount");

		String userId = String.valueOf(session.getAttribute("userId"));
		System.out.println(userId);

		memberService.deleteMember(userId);
		session.invalidate();
		
		return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
	}

}
