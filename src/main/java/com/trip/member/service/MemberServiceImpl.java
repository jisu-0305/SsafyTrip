package com.trip.member.service;

import org.springframework.stereotype.Service;

import com.trip.member.mapper.MemberMapper;
import com.trip.member.dto.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	
	private final MemberMapper memberMapper;

	@Override
	public boolean idCheck(String userId) {
		if(memberMapper.findById(userId) != null) {
			return true;
		} else return false;
	}

	@Override
	public String getSalt(String userId) {
		return memberMapper.findBySalt(userId);
	}

	@Override
	public int joinMember(Member member) {
		return memberMapper.save(member);
	}

	@Override
	public Member loginMember(Member member) {
		return memberMapper.findByMember(member);
	}

	@Override
	public int deleteMember(String userId) {
		return memberMapper.delete(userId);
	}

	@Override
	public int editMember(Member member) {
		return memberMapper.update(member);
	}

}
