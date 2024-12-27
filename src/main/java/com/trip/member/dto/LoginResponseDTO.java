package com.trip.member.dto;

import com.trip.member.entity.Member;
import com.trip.member.enums.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LoginResponseDTO {
    private long userId;
    private String name;
    private String email;
    private Role role;
    private String address;
    private String profileImage;

    // Entity -> Dto
    public static LoginResponseDTO from(Member member) {
        return LoginResponseDTO.builder()
                .userId(member.getUserId())
                .name(member.getName())
                .email(member.getEmail())
                .role(member.getRole())
                .address(member.getAddress())
                .profileImage(member.getProfileImage())
                .build();
    }
}
