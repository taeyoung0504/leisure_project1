package com.project.leisure.dogyeom.kakao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KakaoReadyResponseVO {
	private String tid; //결제 고유 번호
	private String next_redirect_mobile_url; // 요청한 클라이언트가 모바일 웹
	private String next_redirect_pc_url; // 요청한 클라이언트가 PC 웹
	private String created_at; // pc웹일 경우 받는 결제 페이
	private String partner_order_id; // 가맹점 주문번호
}
