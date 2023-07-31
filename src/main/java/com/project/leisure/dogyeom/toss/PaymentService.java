package com.project.leisure.dogyeom.toss;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import org.hibernate.bytecode.spi.ReflectionOptimizer.AccessOptimizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.project.leisure.dogyeom.toss.domain.CancelPaymentDto;
import com.project.leisure.dogyeom.toss.domain.Payment;
import com.project.leisure.dogyeom.toss.domain.PaymentSuccessDto;
import com.project.leisure.hyokyeong.user.UserListService;
import com.project.leisure.taeyoung.user.UserService;
import com.project.leisure.taeyoung.user.Users;

import jakarta.transaction.Transactional;
import net.minidev.json.JSONObject;


@Service
@Transactional
public class PaymentService {
	
	private UserService userService;
	
	private UserListService userListService;
	
	private JpaPaymentRepository paymentRepository;
	
	private TossPaymentConfig tossPaymentConfig;
	
	 @Autowired
	    public PaymentService(JpaPaymentRepository paymentRepository,
	    		UserListService userListService,
	    		TossPaymentConfig tossPaymentConfig) {
	        this.paymentRepository = paymentRepository;
	        this.userListService = userListService;
	        this.tossPaymentConfig = tossPaymentConfig;
	    }
	
	public Payment requestTossPayment(Payment payment, String userName) {
//        List<Users> users = userService.check(userName);
//        
//        Users user;
//        
//        for(Users user1: users) {
//        	if(user1.getUsername().equals(userName)) {
//        		user = user1;
//        		break;
//        	}
//        }
		
		Users user = userListService.getUserByUsername(userName).get();
		
        if (payment.getAmount() < 1000) {
        	throw new CustomLogicException(ExceptionCode.INVALID_PAYMENT_AMOUNT);
        }
//        payment.setCustomer(user);
        payment.setCustomer(user);
        return paymentRepository.save(payment);
    }
	
	@Transactional
	public PaymentSuccessDto tossPaymentSuccess(String paymentKey, String orderId, Long amount) {
	    Payment payment = verifyPayment(orderId, amount);
	    PaymentSuccessDto result = requestPaymentAccept(paymentKey, orderId, amount);
	    payment.setPaymentKey(paymentKey);//추후 결제 취소 / 결제 조회
	    payment.setPaySuccessYN(true);
	    return result;
	}

	public Payment verifyPayment(String orderId, Long amount) {
	    Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> {
	        throw new CustomLogicException(ExceptionCode.PAYMENT_NOT_FOUND);
	    });
	    if (!payment.getAmount().equals(amount)) {
	        throw new CustomLogicException(ExceptionCode.PAYMENT_AMOUNT_EXP);
	    }
	    return payment;
	}
	
	@Transactional
	public PaymentSuccessDto requestPaymentAccept(String paymentKey, String orderId, Long amount) {
	    RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = getHeaders();
	    JSONObject params = new JSONObject();//키/값 쌍을 문자열이 아닌 오브젝트로 보낼 수 있음
	    params.put("orderId", orderId);
	    params.put("amount", amount);

	    PaymentSuccessDto result = null;
	    try { //post요청 (url , HTTP객체 ,응답 Dto)
	        result = restTemplate.postForObject(TossPaymentConfig.URL + paymentKey,
	                new HttpEntity<>(params, headers),
	                PaymentSuccessDto.class);
	    } catch (Exception e) {
	        throw new CustomLogicException(ExceptionCode.ALREADY_APPROVED);
	    }

	    return result;

	}

	 private HttpHeaders getHeaders() {
	        HttpHeaders headers = new HttpHeaders();
	        String encodedAuthKey = new String(
	                Base64.getEncoder().encode((tossPaymentConfig.getTestSecretApiKey() + ":").getBytes(StandardCharsets.UTF_8)));
	        headers.setBasicAuth(encodedAuthKey);
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	        return headers;
	    }

	@Transactional
	public Payment tossPaymentFail(String code, String message, String orderId) {
		Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> {
	        throw new CustomLogicException(ExceptionCode.PAYMENT_NOT_FOUND);
	    });
	    payment.setPaySuccessYN(false);
	    payment.setFailReason(message);
		
	    return payment;
	}

	public CancelPaymentDto tossPaymentCancel(String paymentKey, String cancelReason) {
		RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = getHeaders();
	    JSONObject params = new JSONObject();
	    params.put("cancelReason", cancelReason);

	    CancelPaymentDto cres = null;
	    
	    cres =  restTemplate.postForObject(TossPaymentConfig.URL + paymentKey + "/cancel",
	            new HttpEntity<>(params, headers),
	            CancelPaymentDto.class);
	    
	    return cres;
	}
	
	
	}
	
