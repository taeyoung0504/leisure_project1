package com.project.leisure.taeyoung.public_food;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Entity
@Table(name = "food")
@Getter
@Setter
public class Food {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(name = "shop_id")

	    private String shopId; // 공공데이터 내 포함된 공공데이터 id 

	    @Column(name = "shop_addr")
	    private String shopAddr; // 가게 주소

	    @Column(name = "shop_category")
	    private String shopCategory; //가게 업종 (ex: 한식 , 중식 , 일식 ...)

	    @Column(name = "shop_name")
	    private String shopName; // 가게명

	    @Column(name = "shop_tel")
	    private String shopTel; // 가게 전화번호

	    @Column(name = "shop_opening") 
	    private String shopOpening; // 영업시간

	    @Column(name = "shop_parking")
	    private String shopParking; // 주차  가능 여부

	    @Column(name = "shop_menu")
	    private String shopMenu; // 메뉴

	    @Column(name = "shop_info")
	    private String shopInfo; // 가게 소개

	    public Food() {
	       
	    }

	    public Food(String shopId, String shopAddr, String shopCategory, String shopName, String shopTel, String shopOpening, String shopParking, String shopMenu, String shopInfo) {
	        this.shopId = shopId;
	        this.shopAddr = shopAddr;
	        this.shopCategory = shopCategory;
	        this.shopName = shopName;
	        this.shopTel = shopTel;
	        this.shopOpening = shopOpening;
	        this.shopParking = shopParking;
	        this.shopMenu = shopMenu;
	        this.shopInfo = shopInfo;
	    }
}