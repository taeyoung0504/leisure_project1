package com.project.leisure.dogyeom.kakao;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

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
	
	// 결제 버튼 클릭 시 호출
	@PostMapping("/ready")
	public @ResponseBody KakaoReadyResponseVO kakaoPay(@ModelAttribute BookingVO bookingVO) throws IOException{
		// bookingVO를 이용하여 필요한 작업 수행
		res = kakaoPayService.kakaoPay(bookingVO);
		log.info(res.toString());
		// response.sendRedirect(res.getNext_redirect_pc_url());
		log.info(res.toString());
		return res;
	}
	
	// 결제 승인
	@GetMapping("/success") // session에 예약VO이름으로 저장된 객체 가져와서 그안에 주문번호 가져와서 해당 서비스에 파라미터로 넣어주기
	public String kakaoPaySuccess(@RequestParam("pg_token") String pgToken, Model model, Principal principal) { // @ModelAttribute("bookNum") String bookNum, 
		log.info("kakaoPaySuccess get............................................");
		log.info("kakaoPaySuccess pg_token : " + pgToken);
		// 여기서 session객체 불러서 service에 파라미터로 넘겨줘야한다. -> 안해도됨

		KakaoApproveResponse kcr = new KakaoApproveResponse();
		
		kcr = kakaoPayService.approveResponse(pgToken);
		
		model.addAttribute("info", kcr); // model도 받아서 주문번호 넣어줘야함.
        
		String payDate = kcr.getApproved_at();
		String tid = kcr.getTid();
		String status = "예약완료";
		
		bookService.updatePaymentDate(tid, payDate, status);
		
		return "kdg/success";

	}

	@PostMapping("/refundd")
	public @ResponseBody KakaoCancelResponse kakaoPayCancel(@RequestBody Map<String, Object> params, Model model) {
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@amount : " + params.toString());
		cres = kakaoPayService.kakaoCancel(params);
		log.info(cres.toString());
		model.addAttribute("info", cres);
		
		String tid = cres.getTid();
		String status = cres.getStatus();
		String canceled_at = cres.getCanceled_at();
		bookService.updateCancel(tid, status, canceled_at);
		
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
