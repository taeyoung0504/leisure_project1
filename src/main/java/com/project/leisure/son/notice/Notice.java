package com.project.leisure.son.notice;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name= "Notice")
public class Notice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; // 공지사항 번호
	
	private String title;
	
    private String username;
	
    @Column(length = 1000, columnDefinition = "TEXT")
    private String content; // 공지사항 내용

    private LocalDateTime createDate;
    
    
    // 이미지 파일의 경로를 저장할 필드
    private String imagePath;

    public Notice(String title, String username, String content, String imagePath) {
        this.title = title;
        this.username = username;
        this.content = content;
        this.imagePath = imagePath;
        this.createDate = LocalDateTime.now();
    }
    
//    public Notice(String title, String username, String content) {
//        this.title = title;
//        this.username = username;
//        this.content = content;
//        this.createDate = LocalDateTime.now();
//    }
//    
    
	
}
