package com.trip.member.service;

import com.trip.member.dto.LoginRequestDTO;
import com.trip.member.dto.LoginResponseDTO;
import com.trip.member.dto.RegisterRequestDTO;
import com.trip.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	
	private final MemberMapper memberMapper;


	@Override
	public int registerMember(RegisterRequestDTO request) {
		return memberMapper.save(request);
	}


	@Override
	public LoginResponseDTO loginMember(LoginRequestDTO request) {
		LoginResponseDTO dto =memberMapper.findByEmailAndPassword(request);

		return memberMapper.findByEmailAndPassword(request);
	}


	@Override
	public boolean checkDuplicateEmail(String userId) {
		if(memberMapper.findByEmail(userId) != null) {
			return true;
		} else return false;
	}



	@Override
	public int deleteMember(String userId) {
		return memberMapper.delete(userId);
	}
}
