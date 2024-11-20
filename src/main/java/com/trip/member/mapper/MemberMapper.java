package com.trip.member.mapper;

import com.trip.member.dto.LoginRequestDTO;
import com.trip.member.dto.LoginResponseDTO;
import org.apache.ibatis.annotations.Mapper;

import com.trip.member.dto.RegisterRequestDTO;

@Mapper
public interface MemberMapper {
	int save(RegisterRequestDTO registerMember);

	LoginResponseDTO findByEmailAndPassword(LoginRequestDTO loginRequestDTO);

	String findByEmail(String email);

	int delete(String userId);

}
