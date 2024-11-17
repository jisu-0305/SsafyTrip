package com.trip.member.dto;

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

    public Member() {}
    
    public Member(String userId, String userPwd) {
		this.userId = userId;
		this.userPwd = userPwd;
	}

    public Member(String userId, String userName, String userPwd, String emailId, String emailDomain, Timestamp joinDate, String salt) {
        this.userId = userId;
        this.userName = userName;
        this.userPwd = userPwd;
        this.emailId = emailId;
        this.emailDomain = emailDomain;
        this.joinDate = joinDate;
        this.salt = salt;
    }
}