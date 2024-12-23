package com.trip.mypg.service;

import com.trip.member.entity.Member;
import com.trip.member.mapper.MemberMapper;
import com.trip.member.repository.MemberRepository;
import com.trip.member.service.MemberService;
import com.trip.mypg.dto.DeleteRequestDTO;
import com.trip.mypg.dto.MemberUpdateDTO;
import com.trip.mypg.mapper.MypageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Optional;

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
