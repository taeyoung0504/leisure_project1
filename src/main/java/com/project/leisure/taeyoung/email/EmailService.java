package com.project.leisure.taeyoung.email;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import groovy.util.logging.Slf4j;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

//이메일 인증 메일 템플릿 설정

@PropertySource("classpath:application.properties")
@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

	private final JavaMailSender javaMailSender;
	// @Autowired
	// private RedisUtill redisUtill;

	// 인증번호 생성
	private String ePw;
	// 임시비밀번호 생성
	private String tempPw;

	@Value("${spring.mail.username}")
	private String id;

	public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {

		MimeMessage message = javaMailSender.createMimeMessage();

		message.addRecipients(MimeMessage.RecipientType.TO, to); // to 보내는 대상
		message.setSubject(" 회원가입 인증 코드: "); // 메일 제목

		// 메일 내용 메일의 subtype을 html로 지정하여 html문법 사용 가능
		String msg = "";
		msg += "<div style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 540px; height: 600px; border-top: 4px solid {$point_color}; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">";
		msg += "<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">";
		msg += "<span style=\"font-size: 15px; margin: 0 0 10px 3px;\"></span><br />";
		msg += "<span style=\"color: {$point_color}; border-bottom: 2px solid green;\">회원가입 인증번호 안내입니다. </span>";
		msg += "</h1>";
		msg += "<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">";
		msg += "안녕하세요 경상도숙박장사 입니다.<br />";
		msg += "요청하신 회원가입을 위한 인증번호가 생성되었습니다.<br />";
		msg += "아래 <b style=\"color: {$point_color};\">'인증번호'</b>를 , 인증번호 입력 창에 입력하세요.<br />";
		msg += "감사합니다.";
		msg += "</p><br />";
		msg += "<p style=\"font-size: 16px; margin: 40px 5px 20px; line-height: 28px;\">";
		msg += "인증번호 : <br />";
		msg += "<span style=\"font-size: 24px;\">";
		msg += ePw;
		msg += "</span>";
		msg += "</p>";
		msg += "<br />";
		msg += "<div style=\"border-top: 1px solid #DDD; padding: 5px;\">";
		msg += "<p style=\"font-size: 13px; line-height: 21px; color: #555;\">";
		msg += "</p>";
		msg += "</div>";
		msg += "</div>";

		message.setText(msg, "utf-8", "html"); // 내용, charset타입, subtype
		message.setFrom(new InternetAddress(id, "경상도숙박장사")); // 보내는 사람의 메일 주소, 보내는 사람 이름

		return message;
	}

	// 임시 비밀번호 생성 메일
	public MimeMessage createTempPwMessage(String to) throws MessagingException, UnsupportedEncodingException {

		MimeMessage message = javaMailSender.createMimeMessage();

		message.addRecipients(MimeMessage.RecipientType.TO, to); // to 보내는 대상
		message.setSubject(" 임시비밀번호 발급 안내: "); // 메일 제목

		// 메일 내용 메일의 subtype을 html로 지정하여 html문법 사용 가능
		String msg = "";
		msg += "<div style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 540px; height: 600px; border-top: 4px solid {$point_color}; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">";
		msg += "<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">";
		msg += "<span style=\"font-size: 15px; margin: 0 0 10px 3px;\"></span><br />";
		msg += "<span style=\"color: {$point_color}; border-bottom: 2px solid green;\">임시 비밀번호 안내입니다. </span>";
		msg += "</h1>";
		msg += "<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">";
		msg += "안녕하세요 경상도숙박장사 입니다.<br />";
		msg += "요청하신 임시비밀번호가 생성되었습니다.<br />";
		msg += "아래 <b style=\"color: {$point_color};\">'임시비밀번호를 '</b>를 , 비밀번호 입력창에 입력하세요.<br />";
		msg += "감사합니다.";
		msg += "</p><br />";
		msg += "<p style=\"font-size: 16px; margin: 40px 5px 20px; line-height: 28px;\">";
		msg += "임시 비밀번호 : <br />";
		msg += "<span style=\"font-size: 24px;\">";
		msg += tempPw;
		msg += "</span>";
		msg += "</p>";
		msg += "<br />";
		msg += "<div style=\"border-top: 1px solid #DDD; padding: 5px;\">";
		msg += "<p style=\"font-size: 13px; line-height: 21px; color: #555;\">";
		msg += "</p>";
		msg += "</div>";
		msg += "</div>";

		message.setText(msg, "utf-8", "html"); // 내용, charset타입, subtype
		message.setFrom(new InternetAddress(id, "경상도숙박장사")); // 보내는 사람의 메일 주소, 보내는 사람 이름

		return message;
	}
	
	//이메일 수정을 위한 인증번호 발송 메일
	public MimeMessage createmodifyEamilmessage(String to) throws MessagingException, UnsupportedEncodingException {

		MimeMessage message = javaMailSender.createMimeMessage();

		message.addRecipients(MimeMessage.RecipientType.TO, to); // to 보내는 대상
		message.setSubject(" 이메일 수정을 위한 인증번호 발급 안내: "); // 메일 제목

		// 메일 내용 메일의 subtype을 html로 지정하여 html문법 사용 가능
		String msg = "";
		msg += "<div style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 540px; height: 600px; border-top: 4px solid {$point_color}; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">";
		msg += "<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">";
		msg += "<span style=\"font-size: 15px; margin: 0 0 10px 3px;\"></span><br />";
		msg += "<span style=\"color: {$point_color}; border-bottom: 2px solid green;\">이메일 수정을 위한 인증 번호 안내입니다. </span>";
		msg += "</h1>";
		msg += "<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">";
		msg += "안녕하세요 경상도숙박장사 입니다.<br />";
		msg += "요청하신 이메일 수정을 위한 인증번호가 생성되었습니다.<br />";
		msg += "아래 <b style=\"color: {$point_color};\">'인증번호'</b>를 , 인증번호 입력 창에 입력하세요.<br />";
		msg += "감사합니다.";
		msg += "</p><br />";
		msg += "<p style=\"font-size: 16px; margin: 40px 5px 20px; line-height: 28px;\">";
		msg += "임시 비밀번호 : <br />";
		msg += "<span style=\"font-size: 24px;\">";
		msg += tempPw;
		msg += "</span>";
		msg += "</p>";
		msg += "<br />";
		msg += "<div style=\"border-top: 1px solid #DDD; padding: 5px;\">";
		msg += "<p style=\"font-size: 13px; line-height: 21px; color: #555;\">";
		msg += "</p>";
		msg += "</div>";
		msg += "</div>";

		message.setText(msg, "utf-8", "html"); // 내용, charset타입, subtype
		message.setFrom(new InternetAddress(id, "경상도숙박장사")); // 보내는 사람의 메일 주소, 보내는 사람 이름

		return message;
	}
	
	// 인증코드 만들기
	public static String createKey() {
		StringBuffer key = new StringBuffer();
		Random rnd = new Random();

		for (int i = 0; i < 6; i++) { // 인증코드 6자리
			key.append((rnd.nextInt(10)));
		}
		return key.toString();
	}

	/*
	 * 메일 발송 sendSimpleMessage의 매개변수로 들어온 to는 인증번호를 받을 메일주소 MimeMessage 객체 안에 내가 전송할
	 * 메일의 내용을 담아준다. bean으로 등록해둔 javaMailSender 객체를 사용하여 이메일 send
	 */
	public String sendSimpleMessage(String to) throws Exception {
		ePw = createKey();
		MimeMessage message = createMessage(to);
		try {
			// redisUtill.setDataExpire(ePw, to, 60*3L); //인증코드 3분 시간제한
			javaMailSender.send(message); // 메일 발송
		} catch (MailException es) {
			es.printStackTrace();
			throw new IllegalArgumentException();
		}
		return ePw; // 메일로 보냈던 인증 코드를 서버로 리턴
	}

	// 임시 비밀번호 발송
	public String sendTempMessage(String to) throws Exception {
		tempPw = createKey();
		MimeMessage message = createTempPwMessage(to);
		try {
			// redisUtill.setDataExpire(ePw, to, 60*3L); //인증코드 3분 시간제한
			javaMailSender.send(message); // 메일 발송
		} catch (MailException es) {
			es.printStackTrace();
			throw new IllegalArgumentException();
		}
		return tempPw; // 메일로 보냈던 인증 코드를 서버로 리턴
	}
	
	public String sendmodifyemailCodeMessage(String to) throws Exception {
		tempPw = createKey();
		MimeMessage message = createmodifyEamilmessage(to);
		try {
			// redisUtill.setDataExpire(ePw, to, 60*3L); //인증코드 3분 시간제한
			javaMailSender.send(message); // 메일 발송
		} catch (MailException es) {
			es.printStackTrace();
			throw new IllegalArgumentException();
		}
		return tempPw; // 메일로 보냈던 인증 코드를 서버로 리턴
	}
	/*
	 * public String verifyEmail(String key) throws
	 * ChangeSetPersister.NotFoundException { String memberEmail =
	 * redisUtill.getData(key); if (memberEmail == null) { throw new
	 * ChangeSetPersister.NotFoundException(); } //redisUtill.deleteData(key);
	 * return ePw; }
	 */
}