package com.trip.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class LoginResponseDTO {
    private long userId;
    private String name;
    private String email;
    private String role;            //ROLE_ADMIN, ROLE_USER
    private String profileImage;
}
