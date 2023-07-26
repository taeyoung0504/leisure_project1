package com.project.leisure.taeyoung.user;

import java.io.IOException;
import java.net.URLEncoder;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// login 실패 에러메세지 반환 
@Component
public class UserLoginFailHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		String errorMessage;
		if (exception instanceof BadCredentialsException) {
			errorMessage = "ID 또는 PW를 다시 확인해주세요";
		} else {
			errorMessage = "로그인이 차단된 계정입니다. 관리자에게 문의하세요.";
		}

		errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
		setDefaultFailureUrl("/user/login?error=true&exception=" + errorMessage);

		super.onAuthenticationFailure(request, response, exception);
	}
}