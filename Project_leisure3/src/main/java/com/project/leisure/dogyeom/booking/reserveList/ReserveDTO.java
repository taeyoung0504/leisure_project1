package com.project.leisure.dogyeom.booking.reserveList;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 예약테이블에서 걸러진 값들 받을 DTO
@Entity
@Table(name="unavailable")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReserveDTO {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int id; 
	
	@Column(nullable = true)
	private Long roomID;
	
	@Column(nullable = false)
	private int overlap;
	
	@Column(nullable = false, length = 30)
	private String isDuplicate;
	
	
}
