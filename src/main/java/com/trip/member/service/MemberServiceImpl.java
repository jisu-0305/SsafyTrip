package com.trip.member.service;

import com.trip.member.dto.LoginRequestDTO;
import com.trip.member.dto.LoginResponseDTO;
import com.trip.member.dto.RegisterRequestDTO;
import com.trip.member.entity.Member;
import com.trip.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;

	@Transactional
	@Override
	public boolean registerMember(RegisterRequestDTO registerRequestDTO) {
		Member newMember = Member.fromDTO(registerRequestDTO);
		Member member = memberRepository.save(newMember);

		return member != null ;
	}


	@Override
	public LoginResponseDTO loginMember(LoginRequestDTO request) {
		return memberRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
				.map(LoginResponseDTO::from) 	   // 값이 있으면 DTO로 변환
				.orElse(null);               // 값이 없으면 null 반환
	}


	@Override
	public boolean checkDuplicateEmail(String email) {
		return memberRepository.findByEmail(email).isPresent();
	}
}
