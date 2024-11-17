package com.trip.member.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.trip.member.dto.Member;

@Mapper
public interface MemberMapper {

	String findById(String userId);

	String findBySalt(String userId);

	int save(Member member);

	Member findByMember(Member member);

	int delete(String userId);

	int update(Member member);
	
}
