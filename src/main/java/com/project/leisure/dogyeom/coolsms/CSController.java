package com.project.leisure.dogyeom.coolsms;

import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.leisure.vaild.Phone;
import com.project.leisure.vaild.PhoneReqDto;
import com.project.leisure.vaild.PhoneResDto;
import com.project.leisure.vaild.PhoneService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

@RequiredArgsConstructor
@RestController
public class CSController {
	private final DefaultMessageService messageService = NurigoApp.INSTANCE.initialize("NCSYYCM7VEPHHNBD", "2AVV1N6SG40ENQFYQUSGUFFVMTXGVYNL", "https://api.coolsms.co.kr");
	
	private final HttpSession session;
	
	private final Phone phone;
	
	private final PhoneService phoneService;

	
	@GetMapping("/check/sendSMS")
	//@ResponseBody
//	SingleMessageSentResponse sendSMS(@RequestParam("phoneNumber") String phoneNumber, HttpSession session) {
		public ResponseEntity<?> sendSMS(@RequestParam("phoneNumber") String phoneNumber, HttpSession session) {
	    // phoneNumber2 매개변수명으로 수정하고 @RequestParam 어노테이션을 추가하여 클라이언트 측에서 전달한 값을 받아옴
	    
	    Random rand = new Random();
	    String numStr = "";
	    for (int i = 0; i < 4; i++) {
	        String ran = Integer.toString(rand.nextInt(10));
	        numStr += ran;
	    }
	    
	    PhoneReqDto phoneReqDto = new PhoneReqDto(); ;
	    
	    phoneReqDto.setPhoneNumber(phoneNumber);
	    
	    PhoneResDto phoneResDto = phoneService.findPhone(phoneReqDto.toEntity(), phoneNumber).toPhoneResDto();
	    
	    PhoneResDto phoneResDto2;
	    
	    int count = phoneResDto.getCount();
	    int enable = phoneResDto.getEnable();
	    
	    if(count <= 3 && enable == 1) {
	    	count += 1;
	    	phoneResDto.setCount(count);
	    	
	    	int result = phoneService.updateCount(phoneResDto, phoneNumber);
	    	
		    	if(result == 1) {
		    		
			    		session.setAttribute("rand", numStr);
			    		
			    		Message message = new Message();
			    		message.setFrom("01093365038");
			    		message.setTo(phoneNumber); // phoneNumber 매개변수 사용
			    		message.setText("[경상도숙박장사]입력하셔야할 인증번호는[" + numStr + "]입니다.");
			    		
			    		SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
			    		System.out.println(response);
			    		
			    		return ResponseEntity.ok().body(response);
		    		}
	    	} else if(count > 3) {
		//	    	phoneResDto.setEnable(0);
			    	enable = 0;
			    	phoneResDto2 = phoneService.updateEnableZero(enable, phoneNumber).toPhoneResDto();
			    	
			    	
			    	return ResponseEntity.ok().body(phoneResDto2);
			    }
			    
			    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			    
			}
	


	@GetMapping("/check/codeCheck")
	public Boolean codeCheck(@RequestParam("code") String code, 
			@RequestParam("phoneNumber") String phoneNumber,
			HttpSession session) {
		
		String rand = (String)session.getAttribute("rand");
		
		if (rand.equals(code)) {
			phoneService.updateEnableOne(phoneNumber);
	        session.removeAttribute("rand");
	        return false;
	    } 
		
		return true;
	}



}
