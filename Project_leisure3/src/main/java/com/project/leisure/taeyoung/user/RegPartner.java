package com.project.leisure.taeyoung.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
/* 테스트 */
@Getter
@Setter
@Entity
@Table(name = "reg_partner")
public class RegPartner {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String company_name; // 업체명

   private String company_address; // 업체주소

   private String partner_name; // 담당자명

   private String partner_tel; // 담당자번호

   private String partner_sectors; // 업종

   private String partner_region; // 지역(ex. 대구, 수성구,북구,칠곡...등)

   private String reg_username; // 신청자 아이디

   private String origFilename; // 원본 파일명

   private String filename; // 서버에 저장된 파일 명

   private String filePath; // 파일 저장 경로

   
   @OneToOne
   @JoinColumns({
       @JoinColumn(name = "username", referencedColumnName = "username")
   })
   private Users users;
   
 private int result_partner_reg = 0; // 상태  (0 : 심사중 , 1: 반려 , 2: 승인)


 
 
}