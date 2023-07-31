package com.project.leisure.taeyoung.user;

import com.project.leisure.taeyoung.review.Review;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BookListCancle {
	
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id; 

	 	private String reasonCancle; // 취소 사유

	 	private String  req_partnername; //신청자(파트너명)
}
