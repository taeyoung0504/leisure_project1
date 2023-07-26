package com.project.leisure.dogyeom.booking;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Room {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id; 
	
	@Column(nullable = false, length = 100)
	private String roomTitle;

	@Override
	public String toString() {
		return "Room [id=" + id + ", roomTitle=" + roomTitle + "]";
	}
	
	
}