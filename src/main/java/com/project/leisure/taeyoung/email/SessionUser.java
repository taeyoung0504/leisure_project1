package com.project.leisure.taeyoung.email;


import java.io.Serializable;

import com.project.leisure.taeyoung.user.Users;

import lombok.Getter;


//OAuth2 사용자 세션 정보
@Getter
public class SessionUser implements Serializable {
	private String social_nickname;
	private String email;
	private String sns;

	public SessionUser(Users user) {
		this.social_nickname = user.getNickname();
		this.email = user.getEmail();
		this.sns = user.getSns();

	}

}