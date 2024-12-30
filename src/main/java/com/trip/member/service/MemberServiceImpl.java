package com.trip.member.service;

import com.trip.member.dto.LoginRequestDTO;
import com.trip.member.dto.LoginResponseDTO;
import com.trip.member.dto.RegisterRequestDTO;
import com.trip.member.entity.Member;
import com.trip.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	@Override
	public void registerMember(RegisterRequestDTO registerRequestDTO) {
		try {
			// 비밀번호 암호화
			String encodedPassword = passwordEncoder.encode(registerRequestDTO.getPassword());
			registerRequestDTO.setPassword(encodedPassword);

			Member newMember = Member.fromDTO(registerRequestDTO);
			Member member = memberRepository.save(newMember);
		}catch (Exception e){
			throw new IllegalArgumentException("회원 가입에 실패했습니다.");
		}
	}


	@Override
	public LoginResponseDTO loginMember(LoginRequestDTO request) {
		return memberRepository.findByEmail(request.getEmail())
				.filter(member -> {
					return passwordEncoder.matches(request.getPassword(), member.getPassword());
				})
				.map(LoginResponseDTO::from)
				.orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다."));
	}


	@Override
	public boolean checkDuplicateEmail(String email) {
		return memberRepository.findByEmail(email).isPresent();
	}
}
