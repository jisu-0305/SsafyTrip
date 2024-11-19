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
    private long userId;            // 로그인된 사용자의 고유 ID
    private String name;            // 사용자 이름
    private String email;           // 로그인 ID로 사용되는 이메일
    private String role;            // 사용자 역할 (ROLE_USER, ROLE_ADMIN)
    private String profileImage;    // 프로필 이미지 URL (선택 사항)
}
