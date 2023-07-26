package com.project.leisure.yuri.product;

import com.project.leisure.taeyoung.user.RegPartner;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ProductMain {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long product_main_id; // 상품 mian PK
	
	private String main_product_img_url; //main 이미지 URL
	
	private String product_main_detail; //숙소 main 정보
	
	//RegPartner pk 연결
		@OneToOne
		@JoinColumn(name = "id")
		private RegPartner regPartner;

}
