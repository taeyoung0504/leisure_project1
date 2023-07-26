package com.project.leisure.son.board;



import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;



@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name= "Inquiry")
public class Inquiry {
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; // 회원 번호(PK)
	

    private String username;

	// column 속성
	// name: 필드와 매칭할 테이블의 컬럼 이름 지정 - 기본 값을 객체의 필드 이름
	// nullable(DDL) : null 값의 허용 여부를 설정(false = not null)
	// unique(DDL) : 한 컬럼에 간단히 유니크 제약조건을 걸 때 사용
		// 두 컬럼 이상 유니크 제약 조건을 사용하는 경우 @Table(uniqueConstraints=)속성 사용 권장
	// collumneDefinition(DDL) : 데이터베이스 컬럼 정보를 직접 지정 가능
	
    private String title; // 제목


    private String category; // 카테고리


    private String type; // 유형

    @Column(columnDefinition = "LONGTEXT")
    private String message; // 메시지 내용

    private LocalDateTime createDate;
    
    @OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL)
	private List<InquiryAnswer> inquiryAnswer;
 

}
