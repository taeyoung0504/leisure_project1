package com.project.leisure.taeyoung.review;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.project.leisure.yuri.product.Accommodation;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 리뷰번호

	@Column(nullable = false)
	private int rating; // 평점

	@Column(length = 2000, nullable = false)
	private String content; // 리뷰 내용

	@Column(nullable = false)
	private String author; // 작성자

	// 작성일aa
	private LocalDateTime create_reivew_Time = LocalDateTime.now();

	@OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
	private List<Declaration> declarations = new ArrayList<>();

	@ManyToOne(optional = false)
	@JoinColumn(name = "accommodation_id", referencedColumnName = "id", nullable = false)
	private Accommodation accommodation;

	// 신고 누적 시 블라인드처리를 위한 엔티티

	private boolean contentBlinded;

	public String getContentForDisplay() {
		if (isContentBlinded()) {
			return "운영방침에 의해 블라인드 처리된 리뷰입니다.";
		} else {
			return content;
		}
	}

	public void setContent(String content) {
		if (!this.isContentBlinded()) {
			this.content = content;
		}
		// 리뷰의 내용이 블라인드 처리되어 있으면 변경되지 않도록 조건을 추가합니다.
	}

	// 추가된 메서드
	public boolean isContentBlinded() {
		return contentBlinded;
	}

	public void setContentBlinded(boolean contentBlinded) {
		this.contentBlinded = contentBlinded;
	}
}