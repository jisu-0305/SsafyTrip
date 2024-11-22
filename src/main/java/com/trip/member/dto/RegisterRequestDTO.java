package com.trip.member.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String gender;      // 성별
    private String email;       // 이메일
    private String password;    // 비밀번호
    @JsonProperty("birthdate")
    private LocalDate birthDate; // 생년월일
    private String address;     // 주소
    private String role;        // 역할 (USER)
}
