package com.project.leisure.vaild;

import org.springframework.stereotype.Component;

import com.project.leisure.dogyeom.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Phone extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;
	
	@Column(name = "count")
	private int count;
	
	@Column(name = "enable", columnDefinition = "int default 1") // 기본값으로 1을 설정
	private int enable;
	
	public PhoneResDto toPhoneResDto() {
		return PhoneResDto.builder()
				.phoneNumber(phoneNumber)
				.count(count)
				.enable(enable)
				.build();
	}
	
}
