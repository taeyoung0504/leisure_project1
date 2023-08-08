package com.project.leisure.dogyeom.booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.leisure.yuri.product.Accommodation;
import com.project.leisure.yuri.product.Product;

public interface BookRepository extends JpaRepository<BookingVO, Integer> {
//	BookingVO findByRoomID(int roomID);
//	Optional<BookingVO> findByRoomID(int bookNum);

	List<BookingVO> findAll(Sort sort);

	Optional<BookingVO> findByBookNum(int bookNum);
	
	// bookStatus가 null인 것 제외하고 가져옴
//	List<BookingVO> findByBookStatusIsNotNull();
//	@Query("SELECT b FROM BookingVO b WHERE b.bookStatus IS NOT NULL")
//    List<BookingVO> findBooksByNonNullStatus();
	List<BookingVO> findByBookStatusNotNull();
	
	void deleteByBookStatusIsNull();

//	@Transactional
	List<BookingVO> findByBookStatusAndCheckOut(String bookStatus, LocalDate currentDate);

	List<BookingVO> findByBookStatusAndCheckin(String bookStatus, LocalDate checkIn);

	// 유리 추가 tid를 찾아서 해당 정보를 가져온 후 메세지 전송
	Optional<BookingVO> findByTid(String tid);

	// 유리 추가 => 다음날인지 확인하여 예약확인 메세지 전송
	List<BookingVO> findByCheckinAndBookStatus(LocalDate tomorrow, String string);

	// 유리 추가 => product 외래키 참조해서 null 값으로 처리 (삭제를 위해 설정)
	Optional<BookingVO> findByProduct(Product product);

	// 유리 추가 => Acc 외래키 참조해서 null 값으로 처리(삭제를 위해 설정)
	List<BookingVO> findByAccommodation(Accommodation accommodation);

	// 유리 추가 =>  product 외래키 참조해서 null 값으로 처리 (삭제를 위해 설정)
	List<BookingVO> findAllByProduct(Product product);
}