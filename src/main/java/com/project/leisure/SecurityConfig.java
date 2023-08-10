package com.project.leisure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.project.leisure.taeyoung.email.CustomOAuth2UserService;

import com.project.leisure.taeyoung.user.UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Autowired
	private AuthenticationFailureHandler userLoginFailHandler;

	@Autowired
	private CustomOAuth2UserService customOAuth2UserService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		/* 페이지별 접근 권한 설정 */
		http.authorizeHttpRequests()
        .requestMatchers(new AntPathRequestMatcher("/admin/*")).hasRole("ADMIN") 
        .requestMatchers(new AntPathRequestMatcher("/user/mypage/my_productList")).hasRole("PARTNER") // 숙소등록 및 확인 => only Partner role
        .requestMatchers("/partner/*").hasRole("PARTNER") // /partner/* => only Partner role
        .requestMatchers(new AntPathRequestMatcher("/user/mypage/*")).hasAnyRole("USER", "PARTNER", "SNS", "ADMIN") // etc...
        .requestMatchers("/**").permitAll() 
        .and()
        .csrf().disable()
        /* 로그인 성공, 실패 결과 관한 처리 코드 */
        .formLogin()
            .loginPage("/user/login") // 로그인 페이지 경로
            .failureHandler(userLoginFailHandler) // 로그인 실패 시 메시지 출력을 위한 핸들러 
            .defaultSuccessUrl("/") // 로그인 성공 시 메인페이지로 이동
        .and()
        .logout() // 로그아웃 처리 
            .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
            .logoutSuccessUrl("/") // 로그아웃 성공 시 메인페이지로 이동
            .invalidateHttpSession(true) // 로그아웃 성공 시 세션종료
        .and()
        .oauth2Login() // 소셜로그인을 위한 처리 
            .defaultSuccessUrl("/")
            .userInfoEndpoint()
            .userService(customOAuth2UserService);

		http.rememberMe() // 사용자 계정 저장
				.rememberMeParameter("remember") // default 파라미터는 remember-me
				.tokenValiditySeconds(604800) // 7일(default 14일)
				.alwaysRemember(false) // remember-me 기능 실행여부 (true=항상실행, false=실행안함)
				.userDetailsService(userDetailsService); // 사용자 계정 조회

		return http.build();
	}

	@Bean //패스워드 평문 저장을 방지하기 위한, 암호화를 위한 처리 
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}