package com.project.leisure.yuri.product;

import java.util.ArrayList;
import java.util.List;

import com.project.leisure.taeyoung.review.Review;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Accommodation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 숙소 번호(PK)

	@Column(nullable = false)
	private String username; // 사용자명

	private String acc_name; // 숙소 이름

	private String acc_explain; // 숙소 설명

	@Column(columnDefinition = "LONGTEXT")
	private String acc_info; // 숙소 정보

	private String acc_img; // 이미지 URL

	private String acc_sectors; // 업종

	private String acc_address; // 주소

	private int acc_averPrice; // 평균 금액

	private int acc_rating; // 별점

	private int acc_max_people; // 최대 숙박 인원

	@OneToMany(mappedBy = "accommodation", cascade = CascadeType.ALL)
	private List<Review> reviews = new ArrayList<>();

	// 숙박업소 내의 방들
	@OneToMany(mappedBy = "accommodation")
	private List<Product> products = new ArrayList<>();

}