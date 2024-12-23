package com.trip.mypg.dto;

import com.trip.member.enums.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberUpdateDTO {
    private Gender gender;
    private String address;

}
