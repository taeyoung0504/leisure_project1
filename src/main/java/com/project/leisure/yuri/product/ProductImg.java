package com.project.leisure.yuri.product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ProductImg {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long img_id; // 이미지 ID

	private String img_url; // 이미지 경로 저장

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

}
