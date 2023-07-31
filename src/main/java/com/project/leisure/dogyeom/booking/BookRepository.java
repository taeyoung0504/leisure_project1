package com.project.leisure.dogyeom.booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<BookingVO, Integer> {
//	BookingVO findByRoomID(int roomID);
//	Optional<BookingVO> findByRoomID(int bookNum);

	List<BookingVO> findAll(Sort sort);

	Optional<BookingVO> findByBookNum(int bookNum);

//	@Transactional
	List<BookingVO> findByBookStatusAndCheckOut(String bookStatus, LocalDate currentDate);

	List<BookingVO> findByBookStatusAndCheckin(String bookStatus, LocalDate checkIn);

	// 유리 추가
	Optional<BookingVO> findByTid(String tid);

	//유리 추가
	List<BookingVO> findByCheckinAndBookStatus(LocalDate tomorrow, String string);
}
