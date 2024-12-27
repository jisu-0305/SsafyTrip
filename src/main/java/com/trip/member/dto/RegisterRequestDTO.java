package com.trip.member.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.trip.member.enums.Gender;
import com.trip.member.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RegisterRequestDTO {
    private String name;        // 이름
    private Gender gender;      // 성별
    private String email;       // 이메일
    private String password;// 비밀번호

    @JsonProperty("birthdate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate; // 생년월일

    private String address;     // 주소
    private Role role;        // 역할 (USER)
}
