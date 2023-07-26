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

		http.authorizeHttpRequests().requestMatchers(new AntPathRequestMatcher("/admin/*")).hasRole("ADMIN") //amin route => only Admin role
		.requestMatchers(new AntPathRequestMatcher("/user/mypage/my_productList")).hasRole("PARTNER") // 숙소등록 및 확인 => only Partner role
				.requestMatchers(new AntPathRequestMatcher("/user/mypage/*")) // etc... 
				.hasAnyRole("USER", "PARTNER", "SNS", "ADMIN").anyRequest().permitAll().and().csrf().disable()
				.formLogin().loginPage("/user/login").failureHandler(userLoginFailHandler).defaultSuccessUrl("/").and()
				.logout().logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")).logoutSuccessUrl("/")
				.invalidateHttpSession(true).and().oauth2Login().defaultSuccessUrl("/").userInfoEndpoint()
				.userService(customOAuth2UserService);

		http.rememberMe() // 사용자 계정 저장
				.rememberMeParameter("remember") // default 파라미터는 remember-me
				.tokenValiditySeconds(604800) // 7일(default 14일)
				.alwaysRemember(false) // remember-me 기능 항상 실행
				.userDetailsService(userDetailsService); // 사용자 계정 조회

		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}