package com.trip.member.service;

import com.trip.member.dto.LoginRequestDTO;
import com.trip.member.dto.LoginResponseDTO;
import com.trip.board.dto.Member;
import com.trip.member.dto.RegisterRequestDTO;

public interface MemberService {
	boolean checkDuplicateEmail(String email);
	int registerMember(RegisterRequestDTO request);
	LoginResponseDTO loginMember(LoginRequestDTO request);
	int deleteMember(String userId);
}