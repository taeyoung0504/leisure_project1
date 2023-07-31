package com.project.leisure.taeyoung.statistics;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Visit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // PK
	
	private Long visit_count; // 방문 카운트 1씩
	
	private LocalDateTime visit_date = LocalDateTime.now();  // 방문 일시 
	
	
}
