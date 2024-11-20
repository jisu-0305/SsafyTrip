package com.trip.mypg.service;

import com.trip.member.mapper.MemberMapper;
import com.trip.member.service.MemberService;
import com.trip.mypg.dto.DeleteRequestDTO;
import com.trip.mypg.mapper.MypageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class MypageServiceImpl {

    private final MypageMapper mypageMapper;

    public Boolean deleteUser(DeleteRequestDTO request) {
        String memberEmail = request.getEmail();

        String storedPassWord= mypageMapper.findPasswordByEmail(memberEmail);

        if(storedPassWord!=null && storedPassWord.equals(request.getPassWord())){
            mypageMapper.deleteMember(memberEmail);
            return true;
        }else{
            return false;
        }
    }

}
