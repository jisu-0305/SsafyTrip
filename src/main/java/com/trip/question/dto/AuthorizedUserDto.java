package com.trip.question.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AuthorizedUserDto {
        int userId;
        String email;
        String userRole;
        public AuthorizedUserDto(int userId, String email, String userRole) {
            this.userId = userId;
            this.email = email;
            this.userRole = userRole;
        }
}
