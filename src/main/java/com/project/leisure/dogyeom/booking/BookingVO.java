package com.project.leisure.dogyeom.booking;

import java.io.Serializable;
import java.time.LocalDate;

import com.project.leisure.dogyeom.base.BaseEntity;
import com.project.leisure.yuri.product.Accommodation;
import com.project.leisure.yuri.product.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BookingVO extends BaseEntity implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bookNum; // 예약 번호
	
	//@Column(name = "book_status", nullable = false)
	private String bookStatus; // 예약 상태
	
	private String bookerID; // 예약자 아이디(회원, 비회원 구분)
	private String bookerName; // 예약자 이름
	private String bookerTel; // 예약자 전화번호
	private String accomTitle; // 숙소명
	private String roomTitle; // 객실명(상품명)
	
	private LocalDate checkin; // 체크인 날짜
	
	@Column(name = "check_out", nullable = false)
	private LocalDate checkOut; // 체크아웃 날짜
	
	private int bookHeadCount; // 인원수
	private String paymentDate; // 결제일
	private String payType; // 결제 방식
	private String totalPrice; // 총 결제 금액
	
	private Long tempRoomId; // 객실아이디(외래키 적용 예정)
//	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "roomID", referencedColumnName = "product_id")
	private Product product;
	
	private Long tempAccomId; // 숙소아이디(외래키 적용 예정)
	
	@OneToOne
	@JoinColumn(name = "accomID", referencedColumnName = "id")
	private Accommodation accommodation;
	
//	@JsonIgnore
//	@OneToOne(mappedBy = "booking")
//	@JoinColumn(name = "roomID", referencedColumnName = "product_id")
//	private Product product;
//	
//	private Long tempAccomId; // 숙소아이디(외래키 적용 예정)
//	
//	@OneToOne(mappedBy = "booking")
//	@JoinColumn(name = "accomID", referencedColumnName = "id")
//	private Accommodation accommodation;
	
//	@OneToOne
//    @JoinColumn(name = "roomID", referencedColumnName = "product_id")
//    private Product product;
//	
//	 @OneToOne
//	 @JoinColumn(name = "accomID", referencedColumnName = "id")
//	 private Accommodation accommodation;
	
	private String tid; // 결제번호(카카오에서 받음)

	
	
	
	
	// 추가
	private String productImg;

	public BookingVO(Long tempRoomId) {
        super();
        this.tempRoomId = tempRoomId;
    }


	/*
	 * public BookingVO(LocalDateTime paymentDate, String tid) { super();
	 * this.paymentDate = paymentDate; this.tid = tid; }
	 */

	 public BookingVO(String roomTitle, String totalPrice, Product product) {
	        super();
	        this.roomTitle = roomTitle;
	        this.totalPrice = totalPrice;
	        this.product = product;
	    }

	 public BookingVO(String bookerID, String accomTitle, String roomTitle, LocalDate checkIn, LocalDate checkOut,
	            int bookHeadCount, String totalPrice, Long tempRoomId, Long tempAccomId) {
	        super();
	        this.bookerID = bookerID;
	        this.accomTitle = accomTitle;
	        this.roomTitle = roomTitle;
	        this.checkin = checkIn;
	        this.checkOut = checkOut;
	        this.bookHeadCount = bookHeadCount;
	        this.totalPrice = totalPrice;
	        this.tempRoomId = tempRoomId;
	        this.tempAccomId = tempAccomId;
	    }

	 public BookingVO(String bookerID, String bookerName, String bookerTel, Product product, Accommodation accommodation) {
	        super();
	        this.bookerID = bookerID;
	        this.bookerName = bookerName;
	        this.bookerTel = bookerTel;
	        this.product = product;
	        this.accommodation = accommodation;
	    }
	
	

	// 빌드패턴 생성하기

	 @Override
	    public String toString() {
	        return "BookingVO [bookNum=" + bookNum + ", bookStatus=" + bookStatus + ", bookerID=" + bookerID
	                + ", bookerName=" + bookerName + ", bookerTel=" + bookerTel + ", accomTitle=" + accomTitle
	                + ", roomTitle=" + roomTitle + ", checkIn=" + checkin + ", checkOut=" + checkOut + ", bookHeadCount="
	                + bookHeadCount + ", paymentDate=" + paymentDate + ", payType=" + payType + ", totalPrice=" + totalPrice
	                + ", accomID=" + tempAccomId + ", tid=" + tid + ", productImg=" + productImg + "]";
	    }

}