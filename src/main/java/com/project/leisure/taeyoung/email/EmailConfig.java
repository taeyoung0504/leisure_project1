package com.project.leisure.taeyoung.email;


import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;



// 이메일 발송 설정파일
@Configuration
@PropertySource("classpath:application.properties")
public class EmailConfig {
	@Value("${spring.mail.username}")
	private String id;
	@Value("${spring.mail.password}")
	private String password;
	@Value("${spring.mail.host}")
	private String host;
	@Value("${spring.mail.port}")
	private int port;

	@Bean
	public JavaMailSender javaMailService() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

		javaMailSender.setHost(host); // SMTP 서버 주소
		javaMailSender.setUsername(id); // 설정(발신) 메일 아이디
		javaMailSender.setPassword(password); // 설정(발신) 메일 패스워드
		javaMailSender.setPort(port); // SMTP 포트
		javaMailSender.setJavaMailProperties(getMailProperties()); // 메일 속성 설정
		javaMailSender.setDefaultEncoding("UTF-8");
		return javaMailSender;
	}

	private Properties getMailProperties() {
		Properties properties = new Properties();
		properties.setProperty("mail.transport.protocol", "smtp"); // 프로토콜 설정
		properties.setProperty("mail.smtp.auth", "true"); // SMTP 인증
		properties.setProperty("mail.smtp.starttls.enable", "true"); // STARTTLS 사용
		properties.setProperty("mail.debug", "true"); // 디버그 사용
		properties.setProperty("mail.smtp.ssl.trust", host); // 신뢰할 수 있는 SSL 인증서 설정
		properties.setProperty("mail.smtp.ssl.enable", "true"); // SSL 사용
		return properties;
	}
}
