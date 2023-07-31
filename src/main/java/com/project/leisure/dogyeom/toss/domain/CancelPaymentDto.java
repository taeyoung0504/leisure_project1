package com.project.leisure.dogyeom.toss.domain;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class CancelPaymentDto {
	
	private String cancelReason;
	
	private String canceledAt;
	
	private long cancelAmount;
	
	private String paymentKey;
	
	private String orderId;
	
	private String method;
	
	private String status;
	
	private String requestedAt;
	
	private String approvedAt;
	
}
