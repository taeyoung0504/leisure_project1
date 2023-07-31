package com.project.leisure.taeyoung.email;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.project.leisure.dogyeom.booking.BookRepository;
import com.project.leisure.dogyeom.booking.BookingVO;
import com.project.leisure.taeyoung.user.UserRepository;
import com.project.leisure.taeyoung.user.Users;

import groovy.util.logging.Slf4j;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

//예약 완료, 예약 알림, 예약 취소 문자 발송

@PropertySource("classpath:application.properties")
@Slf4j // 로깅 => 프로그램 실행 중 이벤트 상태 기록
@RequiredArgsConstructor
@Service
public class EmailService2 {

	private final UserRepository userRepository;

	// 메일 전송을 위해 예약 정보를 관리하는 레포지터리를 가져옴
	private final BookRepository bookRepository;

	// 이메일 전송 기능을 제공하기 위해 선언된 멤버 변수
	private final JavaMailSender javaMailSender;

	// JavaMailSender 인터페이스를 사용하기 위해서는 해당 빈(Bean)을 Spring 컨테이너에 등록해야 한다.

	// 이매일 발송을 위한 내 계정의 Bean
	@Value("${spring.mail.username}")
	private String id;

	// 예약 취소 문자 내용 설정
	public MimeMessage confirmationEmailMessage(String to, String username, String realName, String accName,
			String productType, LocalDate checkIn, LocalDate checkOut, String payDate, String totalPrict)
			throws MessagingException, UnsupportedEncodingException {

		MimeMessage message = javaMailSender.createMimeMessage();

		String user_Name = username;
		String real_name = realName;
		String acc_name = accName;
		String pay_date = payDate;
		String product_type = productType;
		LocalDate check_in = checkIn;
		LocalDate check_out = checkOut;
		String total_price = totalPrict;

		// 예약자 이름 중간에 * 문자 삽입
		String maskedName = real_name.charAt(0) + "*" + real_name.substring(2);

		message.addRecipients(MimeMessage.RecipientType.TO, to); // to 보내는 대상
		message.setSubject("[경상도숙박장사] 예약완료"); // 메일 제목

		// 메일 내용 메일의 subtype을 html로 지정하여 html문법 사용 가능
		String msg = "";

		msg += " <table text-align=\"center\" margin=\"0\" cellpadding=\"0\" cellspacing=\"0\"\r\n"
				+ "        style=\"width:100%;background-color:#f8f8f9;letter-spacing:-1px\">\r\n"
				+ "        <tbody>\r\n" + "            <tr>\r\n" + "                <td text-align=\"center\">\r\n"
				+ "                    <div style=\"max-width:595px; margin:0 auto\">\r\n"
				+ "                        <table cellpadding=\"0\" cellspacing=\"0\"\r\n"
				+ "                            style=\"width:100%;margin:0 auto;background-color:#fff;;-webkit-text-size-adjust:100%;text-align:left\">\r\n"
				+ "                            <tbody>\r\n" + "                                <tr>\r\n"
				+ "                                    <td colspan=\"3\" height=\"30\"></td>\r\n"
				+ "                                </tr>\r\n" + "                                <tr>\r\n"
				+ "                                    <td width=\"21\"></td>\r\n"
				+ "                                    <td>\r\n"
				+ "                                        <table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0\">\r\n"
				+ "                                            <tbody>\r\n"
				+ "                                                <tr>\r\n"
				+ "                                                    <td\r\n"
				+ "                                                        style=\"font-size:28px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#424240;line-height:34px;vertical-align:top\">\r\n"
				+ "                                                        [경상도숙박장사] 예약완료 안내\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                </tr>\r\n" + "\r\n"
				+ "                                                <tr>\r\n"
				+ "                                                    <td height=\"80\">" + user_Name
				+ " 님의 예약이 완료되었습니다. <br> 아래의 정보를 확인해 주세요.\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                </tr>\r\n"
				+ "                                                <tr>\r\n"
				+ "                                                    <td height=\"22\"></td>\r\n"
				+ "                                                </tr>\r\n"
				+ "                                            </tbody>\r\n"
				+ "                                        </table>\r\n"
				+ "                                    </td>\r\n"
				+ "                                    <td width=\"21\"></td>\r\n"
				+ "                                </tr>\r\n" + "                                <tr>\r\n"
				+ "                                    <td colspan=\"3\" height=\"1\" style=\"background:#e5e5e5\"></td>\r\n"
				+ "                                </tr>\r\n" + "                                <tr>\r\n"
				+ "                                    <td colspan=\"3\" height=\"26\"></td>\r\n"
				+ "                                </tr>\r\n" + "                                <tr>\r\n"
				+ "                                    <td rowspan=\"5\" width=\"21\"></td>\r\n"
				+ "                                    <td>\r\n"
				+ "                                        <table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0\">\r\n"
				+ "                                            <tbody>\r\n"
				+ "                                                <tr>\r\n"
				+ "                                                    <td width=\"70\"\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;vertical-align:top\">\r\n"
				+ "                                                        고객명\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                    <td\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#333;vertical-align:top\">\r\n"
				+ "                                                        " + maskedName + "\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                </tr>\r\n" + "\r\n"
				+ "                                                <tr>\r\n"
				+ "                                                    <td width=\"70\"\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;vertical-align:top\">\r\n"
				+ "                                                        결제일자\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                    <td\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:Helvetica;color:#333;vertical-align:top\">\r\n"
				+ "                                                        " + pay_date + "\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                </tr>\r\n"
				+ "                                                <tr>\r\n"
				+ "                                                    <td width=\"70\"\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;vertical-align:top\">\r\n"
				+ "                                                        판매자\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                    <td\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:Helvetica;color:#333;vertical-align:top\">\r\n"
				+ "                                                        경상북도숙박장사\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                </tr>\r\n"
				+ "                                                <tr>\r\n"
				+ "                                                    <td width=\"70\"\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;vertical-align:top\">\r\n"
				+ "                                                        상품정보\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                    <td\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:Helvetica;color:#333;vertical-align:top\">\r\n"
				+ "                                                        " + acc_name + " MVG\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                </tr>\r\n"
				+ "                                            </tbody>\r\n"
				+ "                                        </table>\r\n"
				+ "                                    </td>\r\n"
				+ "                                    <td rowspan=\"4\" width=\"21\"></td>\r\n"
				+ "                                </tr>\r\n" + "                                <tr>\r\n"
				+ "                                    <td>\r\n"
				+ "                                        <table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0\">\r\n"
				+ "                                            <tbody>\r\n"
				+ "                                                <tr>\r\n"
				+ "                                                    <td height=\"3\"></td>\r\n"
				+ "                                                </tr>\r\n"
				+ "                                            </tbody>\r\n" + "\r\n"
				+ "                                </tr>\r\n" + "\r\n" + "                        </table>\r\n"
				+ "                </td>\r\n" + "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td>\r\n"
				+ "                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0\">\r\n"
				+ "                        <tbody>\r\n" + "                            <tr>\r\n"
				+ "                                <td height=\"56\"></td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td height=\"24\"\r\n"
				+ "                                    style=\"font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;font-weight:bold;color:#000;vertical-align:top\">\r\n"
				+ "                                    주문상품\r\n" + "                                </td>\r\n"
				+ "                            </tr>\r\n" + "\r\n" + "\r\n" + "\r\n" + "\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td height=\"2\" style=\"background:#424240\"></td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td height=\"21\"></td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td>\r\n"
				+ "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0\">\r\n"
				+ "                                        <tbody>\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td colspan=\"2\" height=\"27\">" + product_type
				+ "</td>\r\n" + "                                            </tr>\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td colspan=\"2\" height=\"27\"></td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td width=\"70\"\r\n"
				+ "                                                    style=\"padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;vertical-align:top\">\r\n"
				+ "                                                    체크인\r\n"
				+ "                                                </td>\r\n"
				+ "                                                <td\r\n"
				+ "                                                    style=\"padding-bottom:9px;font-size:14px;font-family:Helvetica;color:#333;vertical-align:top\">\r\n"
				+ "                                                   " + check_in + "\r\n"
				+ "                                                </td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td width=\"70\"\r\n"
				+ "                                                    style=\"padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;vertical-align:top\">\r\n"
				+ "                                                    체크아웃\r\n"
				+ "                                                </td>\r\n"
				+ "                                                <td\r\n"
				+ "                                                    style=\"padding-bottom:9px;font-size:14px;font-family:Helvetica;color:#333;vertical-align:top\">\r\n"
				+ "                                                    " + check_out + "\r\n"
				+ "                                                </td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td colspan=\"2\" height=\"10\"></td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td colspan=\"2\" height=\"27\">\r\n" + "\r\n"
				+ "                                                    <table cellpadding=\"0\" cellspacing=\"0\"\r\n"
				+ "                                                        style=\"width:100%;margin:0;padding:0\">\r\n"
				+ "                                                        <tbody>\r\n"
				+ "                                                            <tr>\r\n"
				+ "                                                                <td height=\"20\"></td>\r\n"
				+ "                                                            </tr>\r\n"
				+ "                                                            <tr>\r\n"
				+ "                                                                <td>\r\n"
				+ "                                                                    <table cellpadding=\"0\" cellspacing=\"0\"\r\n"
				+ "                                                                        style=\"width:100%;margin:0;padding:0;border:1px solid #ebebeb\">\r\n"
				+ "                                                                        <tbody>\r\n"
				+ "                                                                            <tr>\r\n"
				+ "                                                                                <td colspan=\"3\" height=\"20\">\r\n"
				+ "                                                                                </td>\r\n"
				+ "                                                                            </tr>\r\n"
				+ "                                                                            <tr>\r\n"
				+ "                                                                                <td rowspan=\"6\" width=\"15\"></td>\r\n"
				+ "                                                                                <td colspan=\"2\" height=\"24\"\r\n"
				+ "                                                                                    style=\"font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;font-weight:bold;color:#000;vertical-align:top\">\r\n"
				+ "                                                                                    주문 내역 안내\r\n"
				+ "                                                                                </td>\r\n"
				+ "                                                                                <td rowspan=\"6\" width=\"15\"></td>\r\n"
				+ "                                                                            </tr>\r\n"
				+ "                                                                            <tr>\r\n"
				+ "                                                                                <td width=\"10\"\r\n"
				+ "                                                                                    style=\"padding-top:10px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;line-height:23px;vertical-align:top\">\r\n"
				+ "                                                                                    -\r\n"
				+ "                                                                                </td>\r\n"
				+ "                                                                                <td\r\n"
				+ "                                                                                    style=\"padding-top:10px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;line-height:23px;vertical-align:top\">\r\n"
				+ "                                                                                    상세한 주문 내역 및 주문\r\n"
				+ "                                                                                    진행\r\n"
				+ "                                                                                    상태는 경상도숙박장사에서 확인\r\n"
				+ "                                                                                    가능합니다.\r\n"
				+ "                                                                                </td>\r\n"
				+ "                                                                            </tr>\r\n"
				+ "                                                                            <tr>\r\n"
				+ "                                                                                <td colspan=\"3\" height=\"20\">\r\n"
				+ "                                                                                </td>\r\n"
				+ "                                                                            </tr>\r\n"
				+ "                                                                        </tbody>\r\n"
				+ "                                                                    </table>\r\n"
				+ "                                                                </td>\r\n"
				+ "                                                            </tr>\r\n"
				+ "                                                        </tbody>\r\n"
				+ "                                                    </table>\r\n"
				+ "                                                </td>\r\n"
				+ "                                            </tr>\r\n" + "\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td colspan=\"2\" height=\"17\"></td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                        </tbody>\r\n"
				+ "                                    </table>\r\n" + "                                </td>\r\n"
				+ "                            </tr>\r\n" + "                        </tbody>\r\n"
				+ "                    </table>\r\n" + "                </td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td>\r\n"
				+ "                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0\">\r\n"
				+ "                        <tbody>\r\n" + "                            <tr>\r\n"
				+ "                                <td colspan=\"2\" height=\"1\" style=\"background:#e5e5e5\"></td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td colspan=\"2\" height=\"18\"></td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td width=\"45%\" height=\"30\"\r\n"
				+ "                                    style=\"font-size:16px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;font-weight:bold;color:#333;line-height:22px\">\r\n"
				+ "                                    최종결제금액\r\n" + "                                </td>\r\n"
				+ "                                <td height=\"30\"\r\n"
				+ "                                    style=\"font-size:16px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;font-weight:bold;color:#333;text-align:right;line-height:22px\">\r\n"
				+ "                                    <span\r\n"
				+ "                                        style=\"font-size:30px;font-family:HelveticaNeue;color:hsl(2, 98%, 65%);line-height:8px\">"
				+ total_price + "</span>원\r\n" + "                                </td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td colspan=\"2\" height=\"20\"></td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td colspan=\"2\" height=\"1\" style=\"background:#424240\"></td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td colspan=\"2\" height=\"29\"></td>\r\n"
				+ "                            </tr>\r\n" + "                        </tbody>\r\n"
				+ "                    </table>\r\n" + "                </td>\r\n" + "            </tr>\r\n" + "\r\n"
				+ "            <tr>\r\n" + "                <td colspan=\"3\" height=\"40\"></td>\r\n"
				+ "            </tr>\r\n" + "            <tr>\r\n" + "                <td colspan=\"3\"\r\n"
				+ "                    style=\"padding-top:26px;padding-left:21px;padding-right:21px;padding-bottom:13px;background:#e5e5e5;font-size:12px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;line-height:17px\">\r\n"
				+ "                    본 메일은 발신전용입니다.<br>\r\n"
				+ "                    상품의 배송/반품/교환/취소/환불 문의는 판매자에게 문의하시면 더 빠르고 정확한 처리가 가능합니다.<br>\r\n" + "\r\n"
				+ "                </td>\r\n" + "            </tr>\r\n" + "\r\n" + "            <tr>\r\n"
				+ "                <td colspan=\"3\"\r\n"
				+ "                    style=\"padding-left:21px;padding-right:21px;padding-bottom:57px;background:#e5e5e5;font-size:12px;font-family:Helvetica;color:#696969;line-height:17px\">\r\n"
				+ "                    <strong>경상도숙박장사</strong>\r\n" + "                </td>\r\n"
				+ "            </tr>\r\n" + "        </tbody>\r\n" + "    </table>\r\n" + "    </div>\r\n" + "\r\n"
				+ "    </td>\r\n" + "    </tr>\r\n" + "    </tbody>\r\n" + "    </table>";

		message.setText(msg, "utf-8", "html"); // 내용, charset타입, subtype
		message.setFrom(new InternetAddress(id, "경상도숙박장사")); // 보내는 사람의 메일 주소, 보내는 사람 이름

		return message;
	}

	// 예약 완료 문자 내용 설정
	public MimeMessage cancelEmailMessage(String to, String username, String realName, String accName,
			String productType, LocalDate checkIn, LocalDate checkOut, String payDate, String totalPrict,
			String canDate) throws MessagingException, UnsupportedEncodingException {

		MimeMessage message = javaMailSender.createMimeMessage();

		String user_Name = username;
		String real_name = realName;
		String acc_name = accName;
		String pay_date = payDate;
		String product_type = productType;
		LocalDate check_in = checkIn;
		LocalDate check_out = checkOut;
		String total_price = totalPrict;
		String can_Date = canDate;

		message.addRecipients(MimeMessage.RecipientType.TO, to); // to 보내는 대상
		message.setSubject("[경상도숙박장사] 예약취소"); // 메일 제목

		// 예약자 이름 중간에 * 문자 삽입
		String maskedName = real_name.charAt(0) + "*" + real_name.substring(2);

		// 메일 내용 메일의 subtype을 html로 지정하여 html문법 사용 가능
		String msg = "";

		msg += " <table text-align=\"center\" margin=\"0\" cellpadding=\"0\" cellspacing=\"0\"\r\n"
				+ "        style=\"width:100%;background-color:#f8f8f9;letter-spacing:-1px\">\r\n"
				+ "        <tbody>\r\n" + "            <tr>\r\n" + "                <td text-align=\"center\">\r\n"
				+ "                    <div style=\"max-width:595px; margin:0 auto\">\r\n"
				+ "                        <table cellpadding=\"0\" cellspacing=\"0\"\r\n"
				+ "                            style=\"width:100%;margin:0 auto;background-color:#fff;;-webkit-text-size-adjust:100%;text-align:left\">\r\n"
				+ "                            <tbody>\r\n" + "                                <tr>\r\n"
				+ "                                    <td colspan=\"3\" height=\"30\"></td>\r\n"
				+ "                                </tr>\r\n" + "                                <tr>\r\n"
				+ "                                    <td width=\"21\"></td>\r\n"
				+ "                                    <td>\r\n"
				+ "                                        <table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0\">\r\n"
				+ "                                            <tbody>\r\n"
				+ "                                                <tr>\r\n"
				+ "                                                    <td\r\n"
				+ "                                                        style=\"font-size:28px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#424240;line-height:34px;vertical-align:top\">\r\n"
				+ "                                                        [경상도숙박장사] 예약취소 안내\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                </tr>\r\n" + "\r\n"
				+ "                                                <tr>\r\n"
				+ "                                                    <td height=\"80\">" + user_Name
				+ " 님의 예약이 취소되었습니다. <br> 아래의 정보를 확인해 주세요.\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                </tr>\r\n"
				+ "                                                <tr>\r\n"
				+ "                                                    <td height=\"22\"></td>\r\n"
				+ "                                                </tr>\r\n"
				+ "                                            </tbody>\r\n"
				+ "                                        </table>\r\n"
				+ "                                    </td>\r\n"
				+ "                                    <td width=\"21\"></td>\r\n"
				+ "                                </tr>\r\n" + "                                <tr>\r\n"
				+ "                                    <td colspan=\"3\" height=\"1\" style=\"background:#e5e5e5\"></td>\r\n"
				+ "                                </tr>\r\n" + "                                <tr>\r\n"
				+ "                                    <td colspan=\"3\" height=\"26\"></td>\r\n"
				+ "                                </tr>\r\n" + "                                <tr>\r\n"
				+ "                                    <td rowspan=\"5\" width=\"21\"></td>\r\n"
				+ "                                    <td>\r\n"
				+ "                                        <table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0\">\r\n"
				+ "                                            <tbody>\r\n"
				+ "                                                <tr>\r\n"
				+ "                                                    <td width=\"70\"\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;vertical-align:top\">\r\n"
				+ "                                                        고객명\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                    <td\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#333;vertical-align:top\">\r\n"
				+ "                                                        " + maskedName + "\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                </tr>\r\n" + "\r\n"
				+ "                                                <tr>\r\n"
				+ "                                                    <td width=\"70\"\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;vertical-align:top\">\r\n"
				+ "                                                        결제일자\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                    <td\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:Helvetica;color:#333;vertical-align:top\">\r\n"
				+ "                                                        " + pay_date + "\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                </tr>\r\n"

				+ "<tr>\r\n" + "                                                    <td width=\"70\"\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;vertical-align:top\">\r\n"
				+ "                                                        취소일자\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                    <td\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:Helvetica;color:#333;vertical-align:top\">\r\n"
				+ "                                                        " + can_Date + "\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                </tr>\r\n"
				+ "                                                <tr>\r\n"
				+ "                                                    <td width=\"70\"\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;vertical-align:top\">\r\n"
				+ "                                                        판매자\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                    <td\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:Helvetica;color:#333;vertical-align:top\">\r\n"
				+ "                                                        경상북도숙박장사\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                </tr>\r\n"
				+ "                                                <tr>\r\n"
				+ "                                                    <td width=\"70\"\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;vertical-align:top\">\r\n"
				+ "                                                        상품정보\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                    <td\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:Helvetica;color:#333;vertical-align:top\">\r\n"
				+ "                                                        " + acc_name + " MVG\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                </tr>\r\n"
				+ "                                            </tbody>\r\n"
				+ "                                        </table>\r\n"
				+ "                                    </td>\r\n"
				+ "                                    <td rowspan=\"4\" width=\"21\"></td>\r\n"
				+ "                                </tr>\r\n" + "                                <tr>\r\n"
				+ "                                    <td>\r\n"
				+ "                                        <table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0\">\r\n"
				+ "                                            <tbody>\r\n"
				+ "                                                <tr>\r\n"
				+ "                                                    <td height=\"3\"></td>\r\n"
				+ "                                                </tr>\r\n"
				+ "                                            </tbody>\r\n" + "\r\n"
				+ "                                </tr>\r\n" + "\r\n" + "                        </table>\r\n"
				+ "                </td>\r\n" + "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td>\r\n"
				+ "                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0\">\r\n"
				+ "                        <tbody>\r\n" + "                            <tr>\r\n"
				+ "                                <td height=\"56\"></td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td height=\"24\"\r\n"
				+ "                                    style=\"font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;font-weight:bold;color:#000;vertical-align:top\">\r\n"
				+ "                                    취소상품\r\n" + "                                </td>\r\n"
				+ "                            </tr>\r\n" + "\r\n" + "\r\n" + "\r\n" + "\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td height=\"2\" style=\"background:#424240\"></td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td height=\"21\"></td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td>\r\n"
				+ "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0\">\r\n"
				+ "                                        <tbody>\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td colspan=\"2\" height=\"27\">" + product_type
				+ "</td>\r\n" + "                                            </tr>\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td colspan=\"2\" height=\"27\"></td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td width=\"70\"\r\n"
				+ "                                                    style=\"padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;vertical-align:top\">\r\n"
				+ "                                                    체크인\r\n"
				+ "                                                </td>\r\n"
				+ "                                                <td\r\n"
				+ "                                                    style=\"padding-bottom:9px;font-size:14px;font-family:Helvetica;color:#333;vertical-align:top\">\r\n"
				+ "                                                   " + check_in + "\r\n"
				+ "                                                </td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td width=\"70\"\r\n"
				+ "                                                    style=\"padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;vertical-align:top\">\r\n"
				+ "                                                    체크아웃\r\n"
				+ "                                                </td>\r\n"
				+ "                                                <td\r\n"
				+ "                                                    style=\"padding-bottom:9px;font-size:14px;font-family:Helvetica;color:#333;vertical-align:top\">\r\n"
				+ "                                                    " + check_out + "\r\n"
				+ "                                                </td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td colspan=\"2\" height=\"10\"></td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td colspan=\"2\" height=\"27\">\r\n" + "\r\n"
				+ "                                                    <table cellpadding=\"0\" cellspacing=\"0\"\r\n"
				+ "                                                        style=\"width:100%;margin:0;padding:0\">\r\n"
				+ "                                                        <tbody>\r\n"
				+ "                                                            <tr>\r\n"
				+ "                                                                <td height=\"20\"></td>\r\n"
				+ "                                                            </tr>\r\n"
				+ "                                                            <tr>\r\n"
				+ "                                                                <td>\r\n"
				+ "                                                                    <table cellpadding=\"0\" cellspacing=\"0\"\r\n"
				+ "                                                                        style=\"width:100%;margin:0;padding:0;border:1px solid #ebebeb\">\r\n"
				+ "                                                                        <tbody>\r\n"
				+ "                                                                            <tr>\r\n"
				+ "                                                                                <td colspan=\"3\" height=\"20\">\r\n"
				+ "                                                                                </td>\r\n"
				+ "                                                                            </tr>\r\n"
				+ "                                                                            <tr>\r\n"
				+ "                                                                                <td rowspan=\"6\" width=\"15\"></td>\r\n"
				+ "                                                                                <td colspan=\"2\" height=\"24\"\r\n"
				+ "                                                                                    style=\"font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;font-weight:bold;color:#000;vertical-align:top\">\r\n"
				+ "                                                                                    예약취소 내역 안내\r\n"
				+ "                                                                                </td>\r\n"
				+ "                                                                                <td rowspan=\"6\" width=\"15\"></td>\r\n"
				+ "                                                                            </tr>\r\n"
				+ "                                                                            <tr>\r\n"
				+ "                                                                                <td width=\"10\"\r\n"
				+ "                                                                                    style=\"padding-top:10px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;line-height:23px;vertical-align:top\">\r\n"
				+ "                                                                                    -\r\n"
				+ "                                                                                </td>\r\n"
				+ "                                                                                <td\r\n"
				+ "                                                                                    style=\"padding-top:10px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;line-height:23px;vertical-align:top\">\r\n"
				+ "                                                                                   상세한 예약취소\r\n"
				+ "                                                                                    진행\r\n"
				+ "                                                                                    상태는 경상도숙박장사에서 확인\r\n"
				+ "                                                                                    가능합니다.\r\n"
				+ "                                                                                </td>\r\n"
				+ "                                                                            </tr>\r\n"
				+ "                                                                            <tr>\r\n"
				+ "                                                                                <td colspan=\"3\" height=\"20\">\r\n"
				+ "                                                                                </td>\r\n"
				+ "                                                                            </tr>\r\n"
				+ "                                                                        </tbody>\r\n"
				+ "                                                                    </table>\r\n"
				+ "                                                                </td>\r\n"
				+ "                                                            </tr>\r\n"
				+ "                                                        </tbody>\r\n"
				+ "                                                    </table>\r\n"
				+ "                                                </td>\r\n"
				+ "                                            </tr>\r\n" + "\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td colspan=\"2\" height=\"17\"></td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                        </tbody>\r\n"
				+ "                                    </table>\r\n" + "                                </td>\r\n"
				+ "                            </tr>\r\n" + "                        </tbody>\r\n"
				+ "                    </table>\r\n" + "                </td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td>\r\n"
				+ "                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0\">\r\n"
				+ "                        <tbody>\r\n" + "                            <tr>\r\n"
				+ "                                <td colspan=\"2\" height=\"1\" style=\"background:#e5e5e5\"></td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td colspan=\"2\" height=\"18\"></td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td width=\"45%\" height=\"30\"\r\n"
				+ "                                    style=\"font-size:16px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;font-weight:bold;color:#333;line-height:22px\">\r\n"
				+ "                                    최종반환금액\r\n" + "                                </td>\r\n"
				+ "                                <td height=\"30\"\r\n"
				+ "                                    style=\"font-size:16px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;font-weight:bold;color:#333;text-align:right;line-height:22px\">\r\n"
				+ "                                    <span\r\n"
				+ "                                        style=\"font-size:30px;font-family:HelveticaNeue;color:hsl(2, 98%, 65%);line-height:8px\">"
				+ total_price + "</span>원\r\n" + "                                </td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td colspan=\"2\" height=\"20\"></td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td colspan=\"2\" height=\"1\" style=\"background:#424240\"></td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td colspan=\"2\" height=\"29\"></td>\r\n"
				+ "                            </tr>\r\n" + "                        </tbody>\r\n"
				+ "                    </table>\r\n" + "                </td>\r\n" + "            </tr>\r\n" + "\r\n"
				+ "            <tr>\r\n" + "                <td colspan=\"3\" height=\"40\"></td>\r\n"
				+ "            </tr>\r\n" + "            <tr>\r\n" + "                <td colspan=\"3\"\r\n"
				+ "                    style=\"padding-top:26px;padding-left:21px;padding-right:21px;padding-bottom:13px;background:#e5e5e5;font-size:12px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;line-height:17px\">\r\n"
				+ "                    본 메일은 발신전용입니다.<br>\r\n"
				+ "                    상품의 배송/반품/교환/취소/환불 문의는 판매자에게 문의하시면 더 빠르고 정확한 처리가 가능합니다.<br>\r\n" + "\r\n"
				+ "                </td>\r\n" + "            </tr>\r\n" + "\r\n" + "            <tr>\r\n"
				+ "                <td colspan=\"3\"\r\n"
				+ "                    style=\"padding-left:21px;padding-right:21px;padding-bottom:57px;background:#e5e5e5;font-size:12px;font-family:Helvetica;color:#696969;line-height:17px\">\r\n"
				+ "                    <strong>경상도숙박장사</strong>\r\n" + "                </td>\r\n"
				+ "            </tr>\r\n" + "        </tbody>\r\n" + "    </table>\r\n" + "    </div>\r\n" + "\r\n"
				+ "    </td>\r\n" + "    </tr>\r\n" + "    </tbody>\r\n" + "    </table>";

		message.setText(msg, "utf-8", "html"); // 내용, charset타입, subtype
		message.setFrom(new InternetAddress(id, "경상도숙박장사")); // 보내는 사람의 메일 주소, 보내는 사람 이름

		return message;
	}

	// 체크인 하루 전날 알림 문자를 전송
	public MimeMessage bookingAlert(String to, BookingVO booking)
			throws MessagingException, UnsupportedEncodingException {

		MimeMessage message = javaMailSender.createMimeMessage();
		message.addRecipients(MimeMessage.RecipientType.TO, to); // to 보내는 대상
		message.setSubject("[경상도숙박장사] 예약알림"); // 메일 제목

		// 예약자 이름 중간에 * 문자 삽입
		String maskedName = booking.getBookerName().charAt(0) + "*" + booking.getBookerName().substring(2);

		// 결제일자 0000-00-00 형식으로 전환
		String paymentDateTimeStr = booking.getPaymentDate(); // 현재 날짜 및 시간
		DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		LocalDateTime paymentDateTime = LocalDateTime.parse(paymentDateTimeStr, parser);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedDateTime = paymentDateTime.format(formatter);

		// 메일 내용 메일의 subtype을 html로 지정하여 html문법 사용
		String msg = "";

		msg += " <table text-align=\"center\" margin=\"0\" cellpadding=\"0\" cellspacing=\"0\"\r\n"
				+ "        style=\"width:100%;background-color:#f8f8f9;letter-spacing:-1px\">\r\n"
				+ "        <tbody>\r\n" + "            <tr>\r\n" + "                <td text-align=\"center\">\r\n"
				+ "                    <div style=\"max-width:595px; margin:0 auto\">\r\n"
				+ "                        <table cellpadding=\"0\" cellspacing=\"0\"\r\n"
				+ "                            style=\"width:100%;margin:0 auto;background-color:#fff;;-webkit-text-size-adjust:100%;text-align:left\">\r\n"
				+ "                            <tbody>\r\n" + "                                <tr>\r\n"
				+ "                                    <td colspan=\"3\" height=\"30\"></td>\r\n"
				+ "                                </tr>\r\n" + "                                <tr>\r\n"
				+ "                                    <td width=\"21\"></td>\r\n"
				+ "                                    <td>\r\n"
				+ "                                        <table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0\">\r\n"
				+ "                                            <tbody>\r\n"
				+ "                                                <tr>\r\n"
				+ "                                                    <td\r\n"
				+ "                                                        style=\"font-size:28px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#424240;line-height:34px;vertical-align:top\">\r\n"
				+ "                                                        [경상도숙박장사] 예약알림 안내\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                </tr>\r\n" + "\r\n"
				+ "                                                <tr>\r\n"
				+ "                                                    <td height=\"80\">" + booking.getBookerID()
				+ " 님의 예약알림 메세지 입니다. <br> 아래의 정보를 확인해 주세요.\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                </tr>\r\n"
				+ "                                                <tr>\r\n"
				+ "                                                    <td height=\"22\"></td>\r\n"
				+ "                                                </tr>\r\n"
				+ "                                            </tbody>\r\n"
				+ "                                        </table>\r\n"
				+ "                                    </td>\r\n"
				+ "                                    <td width=\"21\"></td>\r\n"
				+ "                                </tr>\r\n" + "                                <tr>\r\n"
				+ "                                    <td colspan=\"3\" height=\"1\" style=\"background:#e5e5e5\"></td>\r\n"
				+ "                                </tr>\r\n" + "                                <tr>\r\n"
				+ "                                    <td colspan=\"3\" height=\"26\"></td>\r\n"
				+ "                                </tr>\r\n" + "                                <tr>\r\n"
				+ "                                    <td rowspan=\"5\" width=\"21\"></td>\r\n"
				+ "                                    <td>\r\n"
				+ "                                        <table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0\">\r\n"
				+ "                                            <tbody>\r\n"
				+ "                                                <tr>\r\n"
				+ "                                                    <td width=\"70\"\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;vertical-align:top\">\r\n"
				+ "                                                        고객명\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                    <td\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#333;vertical-align:top\">\r\n"
				+ "                                                        " + maskedName + "\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                </tr>\r\n" + "\r\n"
				+ "                                                <tr>\r\n"
				+ "                                                    <td width=\"70\"\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;vertical-align:top\">\r\n"
				+ "                                                        결제일자\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                    <td\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:Helvetica;color:#333;vertical-align:top\">\r\n"
				+ "                                                        " + formattedDateTime + "\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                </tr>\r\n"
				+ "                                                <tr>\r\n"
				+ "                                                    <td width=\"70\"\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;vertical-align:top\">\r\n"
				+ "                                                        판매자\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                    <td\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:Helvetica;color:#333;vertical-align:top\">\r\n"
				+ "                                                        경상북도숙박장사\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                </tr>\r\n"
				+ "                                                <tr>\r\n"
				+ "                                                    <td width=\"70\"\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;vertical-align:top\">\r\n"
				+ "                                                        상품정보\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                    <td\r\n"
				+ "                                                        style=\"padding-bottom:9px;font-size:14px;font-family:Helvetica;color:#333;vertical-align:top\">\r\n"
				+ "                                                        " + booking.getAccomTitle() + " MVG\r\n"
				+ "                                                    </td>\r\n"
				+ "                                                </tr>\r\n"
				+ "                                            </tbody>\r\n"
				+ "                                        </table>\r\n"
				+ "                                    </td>\r\n"
				+ "                                    <td rowspan=\"4\" width=\"21\"></td>\r\n"
				+ "                                </tr>\r\n" + "                                <tr>\r\n"
				+ "                                    <td>\r\n"
				+ "                                        <table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0\">\r\n"
				+ "                                            <tbody>\r\n"
				+ "                                                <tr>\r\n"
				+ "                                                    <td height=\"3\"></td>\r\n"
				+ "                                                </tr>\r\n"
				+ "                                            </tbody>\r\n" + "\r\n"
				+ "                                </tr>\r\n" + "\r\n" + "                        </table>\r\n"
				+ "                </td>\r\n" + "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td>\r\n"
				+ "                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0\">\r\n"
				+ "                        <tbody>\r\n" + "                            <tr>\r\n"
				+ "                                <td height=\"56\"></td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td height=\"24\"\r\n"
				+ "                                    style=\"font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;font-weight:bold;color:#000;vertical-align:top\">\r\n"
				+ "                                    주문상품\r\n" + "                                </td>\r\n"
				+ "                            </tr>\r\n" + "\r\n" + "\r\n" + "\r\n" + "\r\n"
				+ "                            <tr>\r\n"
				+ "                                <td height=\"2\" style=\"background:#424240\"></td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td height=\"21\"></td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td>\r\n"
				+ "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0\">\r\n"
				+ "                                        <tbody>\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td colspan=\"2\" height=\"27\">"
				+ booking.getRoomTitle() + "</td>\r\n" + "                                            </tr>\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td colspan=\"2\" height=\"27\"></td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td width=\"70\"\r\n"
				+ "                                                    style=\"padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;vertical-align:top\">\r\n"
				+ "                                                    체크인\r\n"
				+ "                                                </td>\r\n"
				+ "                                                <td\r\n"
				+ "                                                    style=\"padding-bottom:9px;font-size:14px;font-family:Helvetica;color:#333;vertical-align:top \">\r\n"
				+ "                                                    " + booking.getCheckin() + "\r\n"
				+ "                                                </td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td width=\"70\"\r\n"
				+ "                                                    style=\"padding-bottom:9px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;vertical-align:top\">\r\n"
				+ "                                                    체크아웃\r\n"
				+ "                                                </td>\r\n"
				+ "                                                <td\r\n"
				+ "                                                    style=\"padding-bottom:9px;font-size:14px;font-family:Helvetica;color:#333;vertical-align:top\">\r\n"
				+ "                                                    " + booking.getCheckOut() + "\r\n"
				+ "                                                </td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td colspan=\"2\" height=\"10\"></td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td colspan=\"2\" height=\"27\">\r\n" + "\r\n"
				+ "                                                    <table cellpadding=\"0\" cellspacing=\"0\"\r\n"
				+ "                                                        style=\"width:100%;margin:0;padding:0\">\r\n"
				+ "                                                        <tbody>\r\n"
				+ "                                                            <tr>\r\n"
				+ "                                                                <td height=\"20\"></td>\r\n"
				+ "                                                            </tr>\r\n"
				+ "                                                            <tr>\r\n"
				+ "                                                                <td>\r\n"
				+ "                                                                    <table cellpadding=\"0\" cellspacing=\"0\"\r\n"
				+ "                                                                        style=\"width:100%;margin:0;padding:0;border:1px solid #ebebeb\">\r\n"
				+ "                                                                        <tbody>\r\n"
				+ "                                                                            <tr>\r\n"
				+ "                                                                                <td colspan=\"3\" height=\"20\">\r\n"
				+ "                                                                                </td>\r\n"
				+ "                                                                            </tr>\r\n"
				+ "                                                                            <tr>\r\n"
				+ "                                                                                <td rowspan=\"6\" width=\"15\"></td>\r\n"
				+ "                                                                                <td colspan=\"2\" height=\"24\"\r\n"
				+ "                                                                                    style=\"font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;font-weight:bold;color:#000;vertical-align:top\">\r\n"
				+ "                                                                                    주문 내역 안내\r\n"
				+ "                                                                                </td>\r\n"
				+ "                                                                                <td rowspan=\"6\" width=\"15\"></td>\r\n"
				+ "                                                                            </tr>\r\n"
				+ "                                                                            <tr>\r\n"
				+ "                                                                                <td width=\"10\"\r\n"
				+ "                                                                                    style=\"padding-top:10px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;line-height:23px;vertical-align:top\">\r\n"
				+ "                                                                                    -\r\n"
				+ "                                                                                </td>\r\n"
				+ "                                                                                <td\r\n"
				+ "                                                                                    style=\"padding-top:10px;font-size:14px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;line-height:23px;vertical-align:top\">\r\n"
				+ "                                                                                    상세한 주문 내역 및 주문 진행 상태는 경상도숙박장사에서 확인\r\n"
				+ "                                                                                    가능합니다.\r\n"
				+ "                                                                                </td>\r\n"
				+ "                                                                            </tr>\r\n"
				+ "                                                                            <tr>\r\n"
				+ "                                                                                <td colspan=\"3\" height=\"20\">\r\n"
				+ "                                                                                </td>\r\n"
				+ "                                                                            </tr>\r\n"
				+ "                                                                        </tbody>\r\n"
				+ "                                                                    </table>\r\n"
				+ "                                                                </td>\r\n"
				+ "                                                            </tr>\r\n"
				+ "                                                        </tbody>\r\n"
				+ "                                                    </table>\r\n"
				+ "                                                </td>\r\n"
				+ "                                            </tr>\r\n" + "\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td colspan=\"2\" height=\"17\"></td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                        </tbody>\r\n"
				+ "                                    </table>\r\n" + "                                </td>\r\n"
				+ "                            </tr>\r\n" + "                        </tbody>\r\n"
				+ "                    </table>\r\n" + "                </td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td>\r\n"
				+ "                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0\">\r\n"
				+ "                        <tbody>\r\n" + "                            <tr>\r\n"
				+ "                                <td colspan=\"2\" height=\"1\" style=\"background:#e5e5e5\"></td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td colspan=\"2\" height=\"18\"></td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td width=\"45%\" height=\"30\"\r\n"
				+ "                                    style=\"font-size:16px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;font-weight:bold;color:#333;line-height:22px\">\r\n"
				+ "                                    최종반환금액\r\n" + "                                </td>\r\n"
				+ "                                <td height=\"30\"\r\n"
				+ "                                    style=\"font-size:16px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;font-weight:bold;color:#333;text-align:right;line-height:22px\">\r\n"
				+ "                                    <span\r\n"
				+ "                                        style=\"font-size:30px;font-family:HelveticaNeue;color:hsl(2, 98%, 65%);line-height:8px\">"
				+ booking.getTotalPrice() + "</span>원\r\n" + "                                </td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td colspan=\"2\" height=\"20\"></td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td colspan=\"2\" height=\"1\" style=\"background:#424240\"></td>\r\n"
				+ "                            </tr>\r\n" + "                            <tr>\r\n"
				+ "                                <td colspan=\"2\" height=\"29\"></td>\r\n"
				+ "                            </tr>\r\n" + "                        </tbody>\r\n"
				+ "                    </table>\r\n" + "                </td>\r\n" + "            </tr>\r\n" + "\r\n"
				+ "            <tr>\r\n" + "                <td colspan=\"3\" height=\"40\"></td>\r\n"
				+ "            </tr>\r\n" + "            <tr>\r\n" + "                <td colspan=\"3\"\r\n"
				+ "                    style=\"padding-top:26px;padding-left:21px;padding-right:21px;padding-bottom:13px;background:#e5e5e5;font-size:12px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;color:#696969;line-height:17px\">\r\n"
				+ "                    본 메일은 발신전용입니다.<br>\r\n"
				+ "                    상품의 배송/반품/교환/취소/환불 문의는 판매자에게 문의하시면 더 빠르고 정확한 처리가 가능합니다.<br>\r\n" + "\r\n"
				+ "                </td>\r\n" + "            </tr>\r\n" + "\r\n" + "            <tr>\r\n"
				+ "                <td colspan=\"3\"\r\n"
				+ "                    style=\"padding-left:21px;padding-right:21px;padding-bottom:57px;background:#e5e5e5;font-size:12px;font-family:Helvetica;color:#696969;line-height:17px\">\r\n"
				+ "                    <strong>경상도숙박장사</strong>\r\n" + "                </td>\r\n"
				+ "            </tr>\r\n" + "        </tbody>\r\n" + "    </table>\r\n" + "    </div>\r\n" + "\r\n"
				+ "    </td>\r\n" + "    </tr>\r\n" + "    </tbody>\r\n" + "    </table>";

		message.setText(msg, "utf-8", "html"); // 내용, charset타입, subtype
		message.setFrom(new InternetAddress(id, "경상도숙박장사")); // 보내는 사람의 메일 주소, 보내는 사람 이름

		return message;

	}

	// 예약 완료 메세지 전송
	public void sendConfirmationEmail(String to, String username, String realName, String accName, String productType,
			LocalDate checkIn, LocalDate checkOut, String payDate, String totalPrict) throws Exception {
		// 위에 지정한 형식으로 메세지를 보낸다
		MimeMessage message = confirmationEmailMessage(to, username, realName, accName, productType, checkIn, checkOut,
				payDate, totalPrict);

		try {
			javaMailSender.send(message); // 메일 발송
		} catch (MailException es) {
			es.printStackTrace();
			throw new IllegalArgumentException();
		}
	}

	// 예약취소 메세지 전송
	public void sendCancelEmailMessage(String to, String username, String realName, String accName, String productType,
			LocalDate checkIn, LocalDate checkOut, String payDate, String totalPrict, String canDate) throws Exception {
		// 위에 지정한 형식으로 메세지를 보낸다
		MimeMessage message = cancelEmailMessage(to, username, realName, accName, productType, checkIn, checkOut,
				payDate, totalPrict, canDate);

		try {
			javaMailSender.send(message); // 메일 발송
		} catch (MailException es) {
			es.printStackTrace();
			throw new IllegalArgumentException();
		}
	}

	// 체크인 날짜, 연재 상태가 예약완료인 상태인 사람을 대상으로 메일을 발송
	// 초(0-59) 분(0-59) 시(0-23) 일(1-31) 월(1-12) 요일(0-7, 0과 7은 일요일)
	@Scheduled(cron = "0 0 10 * * ?") // 매일 아침 10시에 메서드를 실행하도록 스케줄링
	public void sendNotification() {
		LocalDate tomorrow = LocalDate.now().plusDays(1); // 내일 날짜를 가져옴
	
		// 내일 체크인이고 예약 상태가 '예약 완료'인 예약들을 대상으로 알림 메세지 전송
		List<BookingVO> bookings = bookRepository.findByCheckinAndBookStatus(tomorrow, "예약완료");

		// 해당하는 bookings 에서 bookerid 에서 userName을 가져와서 User의 이메일을 조회 후 전송
		bookings.forEach(booking -> {
			List<Users> users = userRepository.findByUsername(booking.getBookerID());
			users.forEach(user -> {
				try {
					MimeMessage message = bookingAlert(user.getEmail(), booking);
					sendEmail(message);
				} catch (MessagingException | UnsupportedEncodingException | MailException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		});
	}

	// 예약 알림 메세지 전송
	public void sendEmail(MimeMessage message) throws Exception {
		try {
			javaMailSender.send(message); // 메일 발송
		} catch (MailException es) {
			es.printStackTrace();
			throw new IllegalArgumentException();
		}
	}

}