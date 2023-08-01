package com.project.leisure.dogyeom.toss;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.leisure.dogyeom.toss.domain.Payment;

public interface JpaPaymentRepository extends JpaRepository<Payment, Long> {
	Optional<Payment> findByOrderId(String orderId);
	
    Optional<Payment> findByPaymentKeyAndCustomer_Email(String paymentKey, String email);
    
//    Slice<Payment> findAllByCustomer_Email(String email, Pageable pageable);
}
