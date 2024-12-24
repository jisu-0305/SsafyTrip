package com.trip.member.controller;

import com.trip.common.ResponseDto;
import com.trip.member.dto.LoginRequestDTO;
import com.trip.member.dto.LoginResponseDTO;
import com.trip.member.dto.RegisterRequestDTO;
import com.trip.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("members")
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
										  @PathVariable("email") String email) {
		Boolean isDuplicate = memberService.checkDuplicateEmail(email);

		return ResponseEntity.ok(isDuplicate);
	}


	@Operation(summary = "로그인", description = "아이디와 비밀번호를 이용하여 로그인 처리")
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@Parameter(description = "로그인 시 필요한 회원정보(아이디, 비밀번호).", required = true)
						@RequestBody LoginRequestDTO loginRequestDTO, HttpSession session) {
		LoginResponseDTO loginResponseDTO = memberService.loginMember(loginRequestDTO);

		session.setAttribute("userId", loginResponseDTO.getUserId());
		session.setAttribute("email", loginResponseDTO.getEmail());
		session.setAttribute("userRole", loginResponseDTO.getRole());
		session.setAttribute("userAddress", loginResponseDTO.getAddress());

		return ResponseEntity.ok(loginResponseDTO);

	}


	@Operation(summary = "로그아웃", description = "세션 정보를 이용하여 로그아웃")
	@PostMapping("/logout")
	private ResponseEntity<ResponseDto> logout(HttpSession session) {

		session.invalidate();
		return ResponseEntity.ok(ResponseDto.success("로그아웃 완료!"));
	}


	@GetMapping("/check-session")
	private ResponseEntity<ResponseDto> checkSession(HttpSession session){
		Object userId = session.getAttribute("userId");

		if(userId != null) {
			return ResponseEntity.ok(ResponseDto.success("Session 체크 완료!"));
		}else{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.failure("Session에 해당 User 정보가 없음"));
		}
	}
}
