package com.trip.mypg.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MypageMapper {


    String findPasswordByEmail(String email);

    void deleteMember(String email);
}
