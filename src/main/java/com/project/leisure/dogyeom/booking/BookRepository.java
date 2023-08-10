package com.project.leisure.dogyeom.booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.project.leisure.yuri.product.Accommodation;
import com.project.leisure.yuri.product.Product;

public interface BookRepository extends JpaRepository<BookingVO, Integer> {

	List<BookingVO> findAll(Sort sort);

	Optional<BookingVO> findByBookNum(int bookNum);

	List<BookingVO> findByBookStatusNotNull();
	
	Page<BookingVO> findAll(Pageable pageable);

//	@Transactional
	List<BookingVO> findByBookStatusAndCheckOut(String bookStatus, LocalDate currentDate);

	List<BookingVO> findByBookStatusAndCheckin(String bookStatus, LocalDate checkIn);

	// 찾아서 해당 정보를 가져온 후 메세지 전송
	Optional<BookingVO> findByTid(String tid);

	// 다음날인지 확인하여 예약확인 메세지 전송
	List<BookingVO> findByCheckinAndBookStatus(LocalDate tomorrow, String string);

	// product 외래키 참조해서 null 값으로 처리 (삭제를 위해 설정)
	Optional<BookingVO> findByProduct(Product product);

	// Acc 외래키 참조해서 null 값으로 처리(삭제를 위해 설정)
	List<BookingVO> findByAccommodation(Accommodation accommodation);

	// product 외래키 참조해서 null 값으로 처리 (삭제를 위해 설정)
	List<BookingVO> findAllByProduct(Product product);
}