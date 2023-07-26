package com.project.leisure.son.board;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Inquiry_answer")
public class InquiryAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 답변 번호(PK)

    @Column(columnDefinition = "LONGTEXT")
    private String message; // 답변 내용

    private LocalDateTime createDate; // 답변 생성 일자

    private String username; // 작성자 이름

    @ManyToOne
    @JoinColumn(name = "inquiry_id")
    private Inquiry inquiry; // 질문에 대한 외래키
    
    public InquiryAnswer(String message, LocalDateTime createDate, String username, Inquiry inquiry) {
        this.message = message;
        this.createDate = createDate;
        this.username = username;
        this.inquiry = inquiry;
    }

	

}
