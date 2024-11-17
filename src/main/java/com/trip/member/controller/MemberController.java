package com.trip.member.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trip.member.service.MemberService;
import com.trip.member.dto.Member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
@RequestMapping("member")
@CrossOrigin("*")
@Tag(name = "회원 인증 컨트롤러", description = "로그인, 로그아웃, 회원 정보등 회원관련 서비스를 처리하는 클래스.")
public class MemberController {
	
	private final MemberService memberService;
	
	@Operation(summary = "중복 아이디 검사", description = "입력값을 사용하여 가입된 아이디와 중복되는지 검사.")
	@GetMapping("/{userId}")
	public ResponseEntity<String> idcheck(@Parameter(in = ParameterIn.PATH, description = "인증할 회원의 아이디.", required = true)
						@PathVariable("userId") String userId) {
		if(memberService.idCheck(userId)) {
			return ResponseEntity.ok("존재하는 회원입니다.");
		}

		return ResponseEntity.ok("존재하지 않는 회원입니다.");
	}
	
	@Operation(summary = "회원가입", description = "아이디, 이름, 비밀번호, 이메일을 입력해 회원가입")
	@PostMapping("/join")
	public ResponseEntity<String> join(@Parameter(description = "회원가입 시 필요한 회원정보(아이디, 비밀번호, 이름, 이메일).", required = true) 
						@RequestBody Member member) {

		String salt = getSalt();
		String saltPwd = getEncrypt(member.getUserPwd(), salt);
		
		member.setSalt(salt);
		member.setUserPwd(saltPwd);
		memberService.joinMember(member);
		return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
	}
	
	// salt 생성
	private String getSalt() {
		// 1. 랜덤 객체 생성
		SecureRandom sr = new SecureRandom();
		byte[] salt = new byte[20];

		// 2. 난수 생성
		sr.nextBytes(salt);

		// 3. byte -> 10진수 문자열로 변환
		StringBuffer sb = new StringBuffer();
		for (byte b : salt) {
			sb.append(String.format("%02x", b));
		}

		return sb.toString();
	}

	private String getEncrypt(String pwd, String salt) {
		String result = "";

		try {
			// 1. SHA-256 객체
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			// 2. pwd+salt -> 암호화
			md.update((pwd + salt).getBytes());
			byte[] saltPwd = md.digest();

			// 3. byte -> 10진수 문자열로 변환
			StringBuffer sb = new StringBuffer();
			for (byte b : saltPwd) {
				sb.append(String.format("%02x", b));
			}
			result = sb.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	@Operation(summary = "로그인", description = "아이디와 비밀번호를 이용하여 로그인 처리")
	@PostMapping
	public ResponseEntity<String> login(@Parameter(description = "로그인 시 필요한 회원정보(아이디, 비밀번호).", required = true) 
						@RequestBody Member member, HttpSession session) {
		
		String userId = member.getUserId();
		String userPwd = member.getUserPwd();
		
		// 비밀번호 암호화하여 DB 조회
		String salt = memberService.getSalt(userId);
		String saltPwd = getEncrypt(userPwd, salt);
		member.setUserPwd(saltPwd);

		Member memberInfo = memberService.loginMember(member);
		if (memberInfo != null) {
			session.setAttribute("memberInfo", memberInfo.getUserId());
		} else {
			return ResponseEntity.ok("아이디 또는 비밀번호 확인 후 다시 로그인하세요.");
		}
		
		return ResponseEntity.ok("로그인이 완료되었습니다.");
	}
	
	@Operation(summary = "로그아웃", description = "세션 정보를 이용하여 로그아웃")
	@PostMapping("/logout")
	private ResponseEntity<String> logout(HttpSession session) {
		session.invalidate();

		return ResponseEntity.ok("로그아웃이 완료되었습니다.");
	}
	
	@Operation(summary = "정보 수정", description = "회원 정보(이름) 수정")
	@PutMapping
	private ResponseEntity<String> update(@Parameter(description = "정보수정 시 필요한 회원정보(아이디, 비밀번호, 이름).", required = true)
						@RequestBody Member member) {
		
		memberService.editMember(member);
		return ResponseEntity.ok("정보 수정이 완료되었습니다.");
	}
	
	@Operation(summary = "회원 탈퇴", description = "세션 정보를 이용하여 탈퇴 처리")
	@DeleteMapping
	private ResponseEntity<String> delete(HttpSession session) {
		String userId = (String)session.getAttribute("memberInfo");
		
		memberService.deleteMember(userId);
		session.invalidate();
		
		return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
		
	}


}
