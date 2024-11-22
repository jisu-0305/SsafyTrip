package com.trip.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.sql.Timestamp;

@Getter@Setter
@ToString
public class Member {
    private String userId;
    private String userName;
    private String userPwd;
    private String emailId;
    private String emailDomain;
    private Timestamp joinDate;
    private String salt;


}