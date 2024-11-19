package com.trip.member.service;

import com.trip.member.dto.LoginRequestDTO;
import com.trip.member.dto.LoginResponseDTO;
import org.springframework.stereotype.Service;

import com.trip.member.mapper.MemberMapper;
import com.trip.board.dto.Member;
import com.trip.member.dto.RegisterRequestDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	
	private final MemberMapper memberMapper;


	@Override
	public int registerMember(RegisterRequestDTO request) {
		System.out.println("registerMember from service");
		System.out.println(request);
		return memberMapper.save(request);
	}


	@Override
	public LoginResponseDTO loginMember(LoginRequestDTO request) {
		System.out.println("MemberServiceImpl.loginMember");
		LoginResponseDTO dto =memberMapper.findByEmailAndPassword(request);
		System.out.println(dto);
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
