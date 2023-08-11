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

	
    private String title; // 제목


    private String category; // 카테고리


    private String type; // 유형

    @Column(columnDefinition = "LONGTEXT")
    private String message; // 메시지 내용

    private LocalDateTime createDate;
    
    @OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL)
	private List<InquiryAnswer> inquiryAnswer;
 

}
