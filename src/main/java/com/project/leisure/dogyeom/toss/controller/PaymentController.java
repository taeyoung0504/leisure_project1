package com.project.leisure.dogyeom.toss.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.leisure.dogyeom.booking.BookService;
import com.project.leisure.dogyeom.booking.BookingVO;
import com.project.leisure.dogyeom.toss.PaymentService;
import com.project.leisure.dogyeom.toss.TossPaymentConfig;
import com.project.leisure.dogyeom.toss.domain.CancelPaymentDto;
import com.project.leisure.dogyeom.toss.domain.Payment;
import com.project.leisure.dogyeom.toss.domain.PaymentDto;
import com.project.leisure.dogyeom.toss.domain.PaymentResDto;
import com.project.leisure.dogyeom.toss.domain.PaymentSuccessDto;
import com.project.leisure.taeyoung.user.Users;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

//@RestController
@Controller
@Validated
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
	
	private final PaymentService paymentService;
    
	private final TossPaymentConfig tossPaymentConfig;
//    private final PaymentMapper mapper;
	
	private final BookService bookService;

	
    @PostMapping("/toss")
    public ResponseEntity<PaymentResDto> requestTossPayment(@AuthenticationPrincipal Users principal, @RequestBody @Valid PaymentDto paymentReqDto) {
    	
    	PaymentResDto paymentResDto = paymentService.requestTossPayment(paymentReqDto.toEntity(), principal.getEmail()).toPaymentResDto();
        
    	paymentResDto.setSuccessUrl(paymentReqDto.getYourSuccessUrl() == null ? tossPaymentConfig.getSuccessUrl() : paymentReqDto.getYourSuccessUrl());
        
    	paymentResDto.setFailUrl(paymentReqDto.getYourFailUrl() == null ? tossPaymentConfig.getFailUrl() : paymentReqDto.getYourFailUrl());
        
//    	return ResponseEntity.ok().body(new SingleResponse<>(paymentResDto));
    	return ResponseEntity.ok().body(paymentResDto);
    }
    
    @GetMapping("/toss/success")
    public String tossPaymentSuccess(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount,
            Model model
    ) {
		
		PaymentSuccessDto result = paymentService.tossPaymentSuccess(paymentKey, orderId, amount);
		
		String payDate = result.getApprovedAt();
		String tid = result.getOrderId(); // 취소 요청을 보낼 때 orderId 쓰는줄 알았는데 paymentKey 쓴다고함.
		String newTid = result.getPaymentKey();
		String status = "예약완료";
		
		bookService.updatePaymentDate2(tid, payDate, status, newTid);
		
		BookingVO book = bookService.getBook(newTid);
		
		model.addAttribute("book", book);
		
		return "kdg/success";
    }
    
    
    @GetMapping("/toss/fail")
    public String tossPaymentFail(
            @RequestParam String code,
            @RequestParam String message,
            @RequestParam String orderId,
            Model model
    ) {
    	
       Payment payment =  paymentService.tossPaymentFail(code, message, orderId);
       
       String tid = payment.getOrderId();
       String status = code;
       
      bookService.updateFail(tid, status);
      
      BookingVO book = bookService.getBook(tid);
      
      model.addAttribute("book", book);
       
        
        return "kdg/fail";
    }
    
	
}
