package com.project.leisure.yuri.product;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long product_id; // 객실 고유 번호

	private int product_count; // 객실 수

	private int product_amount; // 요금

	private String product_type; // 방 타입

	private String product_detail; // 방 상세 설명

	private LocalTime checkin; // 체크인

	private LocalTime checkout; // 체크아웃

	private int product_pernum; // 인원수

	// 상품 이미지
	@OneToMany(mappedBy = "product")
	private List<ProductImg> productImgs = new ArrayList<>();


	// 숙박업소 연결
	@ManyToOne
	@JoinColumn(name = "accommodation_id", referencedColumnName = "id")
	private Accommodation accommodation;
	
	// 김도겸이 추가한 totalPrice ㅋ
	private String totalPrice;

}
