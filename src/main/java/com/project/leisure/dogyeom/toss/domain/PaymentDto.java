package com.project.leisure.dogyeom.toss.domain;

import java.util.UUID;

import com.project.leisure.dogyeom.toss.PayType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
	
	@NonNull
	private PayType payType;
	
	@NonNull
	private Long amount;
	
	private String orderName;
	
	private String customerName;
	
	private String customerTel;
	
	private String yourSuccessUrl;
	private String yourFailUrl;
	
	public Payment toEntity() {
		return Payment.builder()
				.payType(payType)
				.amount(amount)
				.orderName(orderName)
				.orderId(UUID.randomUUID().toString())
				.paySuccessYN(false)
				.build();
	}
	
}
