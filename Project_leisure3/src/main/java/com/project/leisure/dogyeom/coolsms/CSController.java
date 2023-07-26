package com.project.leisure.dogyeom.coolsms;

import java.util.Random;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

@RequiredArgsConstructor
@RestController
public class CSController {
	private final DefaultMessageService messageService;
	private final HttpSession session;
	
	public CSController() {
		this.messageService = NurigoApp.INSTANCE.initialize("NCSYYCM7VEPHHNBD", "2AVV1N6SG40ENQFYQUSGUFFVMTXGVYNL", "https://api.coolsms.co.kr");
		this.session = null;
	}
	
	// 두 번째 생성자 수정
    public CSController(HttpSession session) {
        this.messageService = NurigoApp.INSTANCE.initialize("NCSYYCM7VEPHHNBD", "2AVV1N6SG40ENQFYQUSGUFFVMTXGVYNL", "https://api.coolsms.co.kr");
        this.session = session;
    }
	
	@GetMapping("/check/sendSMS")
	//@ResponseBody
	SingleMessageSentResponse sendSMS(@RequestParam("phoneNumber") String phoneNumber, HttpSession session) {
	    // phoneNumber2 매개변수명으로 수정하고 @RequestParam 어노테이션을 추가하여 클라이언트 측에서 전달한 값을 받아옴
	    
	    Random rand = new Random();
	    String numStr = "";
	    for (int i = 0; i < 4; i++) {
	        String ran = Integer.toString(rand.nextInt(10));
	        numStr += ran;
	    }
	    
	    session.setAttribute("rand", numStr);
	    
	    Message message = new Message();
	    message.setFrom("01093365038");
	    message.setTo(phoneNumber); // phoneNumber 매개변수 사용
	    message.setText("[경상도숙박장사]입력하셔야할 인증번호는[" + numStr + "]입니다.");
	    
	    SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
	    System.out.println(response);
	    
	    return response;
	}
	
	@GetMapping("/check/codeCheck")
	public Boolean codeCheck(@RequestParam("code") String code, HttpSession session) {
		
		String rand = (String)session.getAttribute("rand");
		
		if (rand.equals(code)) {
	        session.removeAttribute("rand");
	        return false;
	    } 
		
		return true;
	}

}
