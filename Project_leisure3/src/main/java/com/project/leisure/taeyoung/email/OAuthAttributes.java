package com.project.leisure.taeyoung.email;

// OAuth 로그인 사용자 반환정보 처리
import java.util.Map;

import com.project.leisure.taeyoung.user.UserRole;
import com.project.leisure.taeyoung.user.Users;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {
	private Map<String, Object> attributes; // OAuth2 반환하는 유저 정보 Map
	private String nameAttributeKey;
	private String nickname;
	private String email;
	private String sns;

	@Builder
	public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String nickname, String email,
			String sns) {
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.nickname = nickname;
		this.email = email;
		this.sns = sns;

	}

	public static OAuthAttributes of(String registrationId, String userNameAttributeName,
			Map<String, Object> attributes) {
		// 여기서 네이버와 카카오 등 구분 (ofNaver, ofKakao)
		switch (registrationId) {
		case "google":
			return ofGoogle(userNameAttributeName, attributes);
		case "naver":
			return ofNaver(userNameAttributeName, attributes);
		case "kakao":
			return ofKakao(userNameAttributeName, attributes);
		}

		return null;
	}

	private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuthAttributes.builder().nickname((String) attributes.get("name"))
				.email((String) attributes.get("email")).attributes(attributes).sns("Google")
				.nameAttributeKey(userNameAttributeName).build();
	}

	private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {

		Map<String, Object> response = (Map<String, Object>) attributes.get("response");

		return OAuthAttributes.builder().nickname((String) response.get("name")).email((String) response.get("email"))
				.sns("Naver").attributes(attributes).nameAttributeKey(userNameAttributeName).build();
	}

	private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
		// kakao는 kakao_account에 유저정보가 있다. (email)
		Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
		// kakao_account안에 또 profile이라는 JSON객체가 있다. (nickname, profile_image)
		Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

		return OAuthAttributes.builder().nickname((String) kakaoProfile.get("nickname"))
				.email((String) kakaoAccount.get("email")).sns("Kakao").attributes(attributes)
				.nameAttributeKey(userNameAttributeName).build();
	}

	public Users toEntity() {
		return Users.builder().nickname(nickname).email(email).sns(sns).role(UserRole.USER).build();
	}

}