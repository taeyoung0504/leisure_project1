package com.project.leisure.taeyoung.review;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;


// 리뷰신고 엔티티 

@Entity
@Getter
@Setter
public class Declaration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String declaration; // 신고내용(요약)

    
    private String declarationDetail; //신고내용(상세)
    
    @Column(nullable = true)
    private String repoter; //신고자
    
    @ManyToOne
    @JoinColumn(name = "review_id", referencedColumnName = "id", nullable = false)
    private Review review;
}