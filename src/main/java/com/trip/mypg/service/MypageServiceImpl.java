package com.trip.mypg.service;

import com.trip.member.entity.Member;
import com.trip.member.repository.MemberRepository;
import com.trip.mypg.dto.DeleteRequestDTO;
import com.trip.mypg.dto.MemberUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MypageServiceImpl {

    private final MemberRepository memberRepository;

    public void deactivateUser(DeleteRequestDTO request) {
        Member member = memberRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(() -> new RuntimeException("계정을 찾을 수 없습니다."));
        member.deactivate();

        memberRepository.save(member);

    }

    public void updateUser(MemberUpdateDTO memberUpdateDTO, long userId) throws Exception{
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + userId));

        member.updateAddress(memberUpdateDTO.getAddress());
        member.updateGender(memberUpdateDTO.getGender());

        memberRepository.save(member);

    }

}
