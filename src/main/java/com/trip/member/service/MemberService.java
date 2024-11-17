package com.trip.member.service;

import com.trip.member.dto.Member;

public interface MemberService {
	boolean idCheck(String userId);
	String getSalt(String userId);
	int joinMember(Member member);
	Member loginMember(Member member);
	int deleteMember(String userId);
	int editMember(Member member);
}