package com.project.leisure.dogyeom.kakao;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.SessionAttributes;

import com.project.leisure.dogyeom.booking.BookService;
import com.project.leisure.dogyeom.booking.BookingVO;
import com.project.leisure.taeyoung.email.EmailService2;
import com.project.leisure.taeyoung.user.UserService;
import com.project.leisure.taeyoung.user.Users;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

//@RequestMapping("/")
@Slf4j
@Controller
//@SessionAttributes({"bookNum"}) 
public class KakaoPayController {

	private KakaoReadyResponseVO res;
	private KakaoCancelResponse cres;
	private KakaoPayService kakaoPayService;

	@Autowired
	private BookService bookService;

	public KakaoPayController(KakaoPayService kakaoPayService) {
		this.kakaoPayService = kakaoPayService;
	}

	// 유리 추가
	@Autowired
	private UserService userService;
	@Autowired
	private EmailService2 emailService2;

	// 결제 버튼 클릭 시 호출
	@PostMapping("/ready")
	public @ResponseBody KakaoReadyResponseVO kakaoPay(@ModelAttribute BookingVO bookingVO) throws IOException {
		// bookingVO를 이용하여 필요한 작업 수행
		res = kakaoPayService.kakaoPay(bookingVO);
		log.info(res.toString());
		// response.sendRedirect(res.getNext_redirect_pc_url());
		log.info(res.toString());
		return res;
	}

	// 결제 승인
	@GetMapping("/success") // session에 예약VO이름으로 저장된 객체 가져와서 그안에 주문번호 가져와서 해당 서비스에 파라미터로 넣어주기
	public String kakaoPaySuccess(@RequestParam("pg_token") String pgToken, Model model, Principal principal) { // @ModelAttribute("bookNum")
																												// String
																												// bookNum,
		log.info("kakaoPaySuccess get............................................");
		log.info("kakaoPaySuccess pg_token : " + pgToken);
		// 여기서 session객체 불러서 service에 파라미터로 넘겨줘야한다. -> 안해도됨

		KakaoApproveResponse kcr = new KakaoApproveResponse();

		kcr = kakaoPayService.approveResponse(pgToken);

//		model.addAttribute("info", kcr); // model도 받아서 주문번호 넣어줘야함.

		String payDate = kcr.getApproved_at();
		String tid = kcr.getTid();
		String status = "예약완료";

		bookService.updatePaymentDate(tid, payDate, status);
		
		BookingVO book = bookService.getBook(tid);
		
		model.addAttribute("book", book);	

		// 유리 추가 카카오 승인이 되면 이메일 보내도록 처리
		// 현재 로그인한 유저의 정보를 가져온다.
		String username = principal.getName();
		List<Users> findUserEmail = userService.check(username); // 유저가 있는지 없는지 확인

		if (findUserEmail.isEmpty()) {
			model.addAttribute("error", "User does not exist.");
			return "kdg/error";
		}

		Users user = findUserEmail.get(0); // 유저의 정보를 가져온다
		String userEmail = user.getEmail(); // 가져온 정보에서 이메일을 가져옴

		// tid 불러와서 조회
		Optional<BookingVO> bookingVO = bookService.findBookTid(tid);
		if (bookingVO.isPresent()) {
			String accName = bookingVO.get().getAccomTitle(); // 숙박업소 이름
			String productType = bookingVO.get().getRoomTitle(); // 객실명
			LocalDate checkIn = bookingVO.get().getCheckin(); // 체크인 시간
			LocalDate checkOut = bookingVO.get().getCheckOut(); // 체크아웃 시간

			// 결제 날짜 년-월-일 식으로 보여주기 위함
			String paymentDateTimeStr = bookingVO.get().getPaymentDate();
			DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
			LocalDateTime paymentDateTime = LocalDateTime.parse(paymentDateTimeStr, parser);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String pay_Date = paymentDateTime.format(formatter);

			String totalPrict = bookingVO.get().getTotalPrice();
			String realName = bookingVO.get().getBookerName();

			try {
				emailService2.sendConfirmationEmail(userEmail, username, realName, accName, productType, checkIn,
						checkOut, pay_Date, totalPrict);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			model.addAttribute("error", "Booking not found.");
			return "kdg/error"; // 메세지 전달 실패 에러 페이지 추후에 만들기
		} // 유리 추가 끝

		return "kdg/success";
	}

	@PostMapping("/refundd")
	public @ResponseBody KakaoCancelResponse kakaoPayCancel(@RequestBody Map<String, Object> params, Model model,
			Principal principal) {
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@amount : " + params.toString());
		cres = kakaoPayService.kakaoCancel(params);
		log.info(cres.toString());
		model.addAttribute("info", cres);

		String tid = cres.getTid();
		String status = cres.getStatus();
		String canceled_at = cres.getCanceled_at();
		bookService.updateCancel(tid, status, canceled_at);

		// 유리 추가 예약 취소시 전송 문자
		// 현재 로그인한 유저의 정보를 가져온다.
		String username = principal.getName();
		List<Users> findUserEmail = userService.check(username); // 유저가 있는지 없는지 확인

		if (findUserEmail.isEmpty()) {
			model.addAttribute("error", "User does not exist.");
		}

		Users user = findUserEmail.get(0); // 유저의 정보를 가져온다
		String userEmail = user.getEmail(); // 가져온 정보에서 이메일을 가져옴

		// tid 불러와서 조회
		Optional<BookingVO> bookingVO = bookService.findBookTid(tid);
		if (bookingVO.isPresent()) {
			String accName = bookingVO.get().getAccomTitle(); // 숙박업소 이름
			String productType = bookingVO.get().getRoomTitle(); // 객실명
			LocalDate checkIn = bookingVO.get().getCheckin(); // 체크인 시간
			LocalDate checkOut = bookingVO.get().getCheckOut(); // 체크아웃 시간

			// 결제 날짜 년-월-일 식으로 보여주기 위함 (취소시에 Canceled_at을 넣어서 사용)
			String paymentDateTimeStr = bookingVO.get().getCanceled_at();
			DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
			LocalDateTime paymentDateTime = LocalDateTime.parse(paymentDateTimeStr, parser);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String pay_Date = paymentDateTime.format(formatter);

			// 결제 날짜 년-월-일 식으로 보여주기 위함 (취소시에 Canceled_at을 넣어서 사용)
			String cancelDateTimeStr = bookingVO.get().getCanceled_at();
			DateTimeFormatter canceParser = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
			LocalDateTime cancelDateTime = LocalDateTime.parse(cancelDateTimeStr, canceParser);

			DateTimeFormatter cancelformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String can_Date = cancelDateTime.format(cancelformatter);

			String totalPrict = bookingVO.get().getTotalPrice();
			String realName = bookingVO.get().getBookerName();

			try {
				emailService2.sendCancelEmailMessage(userEmail, username, realName, accName, productType, checkIn,
						checkOut, pay_Date, totalPrict, can_Date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			model.addAttribute("error", "Booking not found.");
		} // 유리 추가 끝

		return cres;
	}

	// 결제 취소시 실행 url
	@GetMapping("/cancel")
	public String payCancel() {
//		return "redirect:/carts";
		return "cancel";
	}

	// 결제 실패시 실행 url
	@GetMapping("/fail")
	public String payFail() {
//		return "redirect:/carts";
		return "fail";
	}

	// 취소 에러 던지는 법 - 나중에 하기

}