package com.project.leisure.dogyeom.kakao;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.RestTemplate;

import com.project.leisure.dogyeom.booking.BookingVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoPayService {

	private KakaoReadyResponseVO res;
	static final String cid = "TC0ONETIME";
	static final String admin_Key = "b7560c47f234c1974d527dd7029216dc";

	public KakaoReadyResponseVO kakaoPay(@ModelAttribute BookingVO params) {

		/*
		 * 결제번호는 고유한 결제번호로 생성해줘야 한다. 여기서는 임시로 그냥 KAO20230623
		 */
		// 카카오페이 요청 양식
		MultiValueMap<String, Object> payParams = new LinkedMultiValueMap<>();
		// 결제수단 추가하나?

		String stringTotalPrice = params.getTotalPrice();
		String totalPrice = stringTotalPrice.replaceAll(",", "");

		payParams.add("cid", cid);
		payParams.add("partner_order_id", "KAO20230623"); // 이런 것들은 고정값으로 박아놓는게 좋은가? or 따로 해당 정보만 담고 있는 데이터베이스를 만들어야하나?
		payParams.add("partner_user_id", "kakaopayTest");
		payParams.add("item_name", params.getRoomTitle());
		payParams.add("quantity", 1);
		payParams.add("total_amount", Integer.parseInt(totalPrice));
		payParams.add("vat_amount", 200);
		payParams.add("tax_free_amount", 200);
		// 계속 에러가 발생해서 https를 http로 바꿔주었음 - 노션에 정리


//		payParams.add("approval_url", "http://localhost:8080/success"); // 결제승인시 넘어갈 url
	//	payParams.add("fail_url", "http://localhost:8080/fail"); // 결제실패시 넘어갈 url
	//	payParams.add("cancel_url", "http://localhost:8080/cancel"); // 결제취소시 넘어갈 url
		payParams.add("approval_url", "http://192.168.10.67:8080/success"); // 결제승인시 넘어갈 url
		payParams.add("fail_url", "http://192.168.10.67:8080/fail"); // 결제실패시 넘어갈 url
		payParams.add("cancel_url", "http://192.168.10.67:8080/cancel"); // 결제취소시 넘어갈 url

		// 카카오페이 결제준비 api 요청
		// 파라미터, 헤더
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(payParams, this.getHeaders());

		// 외부에 보낼 url
		RestTemplate template = new RestTemplate();
		String url = "https://kapi.kakao.com/v1/payment/ready";
//		String url = "http://kapi.kakao.com/v1/payment/ready";

		// 요청결과
//		KakaoReadyResponseVO res = template.postForObject(url, requestEntity, KakaoReadyResponseVO.class);
		res = template.postForObject(url, requestEntity, KakaoReadyResponseVO.class);

		/*
		 * 요청결과에서 응답받은 tid 값을 데이터베이스에 저장하는 로직 추가 주문번호랑 tid랑 연결하여 결제이력테이블로 관리?
		 */
		return res;

	}

	/**
	 * 결제 완료 승인
	 */
	public KakaoApproveResponse approveResponse(String pgToken) { // 나중에 pgToken외에도 model을 받아야함(취소시 주문번호 받기)

		// 카카오 요청
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.add("cid", String.valueOf(cid));
		parameters.add("tid", res.getTid()); // model로 받으면 res.getTid()가 아니고 model.getTid()가 될 것. -> 근데 굳이 그렇게 안줘도 될듯
		parameters.add("partner_order_id", "KAO20230623"); // partner order id는 굳이 바꿔줄 필요 없나? 아니면 따로 제품마다 해줘야하나? ->
															// 해줘야하면 model받아야함.
		parameters.add("partner_user_id", "kakaopayTest");
		parameters.add("pg_token", pgToken);

		// 파라미터, 헤더
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

		// 외부에 보낼 url
		RestTemplate restTemplate = new RestTemplate();

		KakaoApproveResponse approveResponse = restTemplate.postForObject(
//                "https://kapi.kakao.com/v1/payment/approve",
				"https://kapi.kakao.com/v1/payment/approve", requestEntity, KakaoApproveResponse.class);
		// 여기서 결제 승인 요청 성공여부 확인후에 리포지토리에 tid insert 해줘야함. -> 컨트롤러에 하는게 나을 것 같은데
		return approveResponse;
	}

	/**
	 * 결제 환불
	 */
	// 취소하고자하는 상품의 id를 받아서 데이터를 조회하고 조회한 데이터를 model에 담아 취소 서비스에 넘긴다.
	// 취소 서비스에서 model에서 받은 tid와 금액을 추출하여 취소요청을 보낸다.
	// -> 그런데 해당 결제수단을 if문으로 검사하여 해당 결제수단의 서비스로 보내줘야 할듯
	public KakaoCancelResponse kakaoCancel(Map<String, Object> params) {

		/*
		 * System.out.println(")))))))))))))))))))))))))) amount : " +
		 * params.get("cancel_amount")); String amount =
		 * String.valueOf(params.get("cancel_amount"));
		 * System.out.println(")))))))))))))))))))))))))) amount : " + amount); String
		 * cancelAmount = amount.replaceAll(",", "");
		 * System.out.println(")))))))))))))))))))))))))) cancelAmount : " +
		 * cancelAmount);
		 */

		String cancelAmountStr = params.get("cancel_amount").toString();
		cancelAmountStr = cancelAmountStr.trim();
		int cancelAmount = Integer.parseInt(cancelAmountStr);

		// 카카오페이 요청
		MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
		parameters.add("cid", cid);
		parameters.add("tid", params.get("tid")); // 결제 취소 요청은 tid를 사용한다.
		parameters.add("cancel_amount", cancelAmount);
		parameters.add("cancel_tax_free_amount", params.get("cancel_tax_free_amount"));
		parameters.add("cancel_vat_amount", params.get("cancel_vat_amount"));

		// 파라미터, 헤더
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

		// 외부에 보낼 url
		RestTemplate restTemplate = new RestTemplate();

		KakaoCancelResponse cancelResponse = restTemplate.postForObject("https://kapi.kakao.com/v1/payment/cancel",
				requestEntity, KakaoCancelResponse.class);

		return cancelResponse;
	}

	private HttpHeaders getHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();

		String auth = "KakaoAK " + admin_Key;

		httpHeaders.set("Authorization", auth);
		httpHeaders.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		// headers.add("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
		// headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE +
		// ";charset=UTF-8");

		return httpHeaders;
	}

}