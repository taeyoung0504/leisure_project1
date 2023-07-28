package com.project.leisure.dogyeom.booking.reserveList;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.leisure.dogyeom.booking.BookingVO;

@Repository
public interface JpaAlreadyReserveRepository extends JpaRepository<BookingVO, Integer>{
	
	// " AND b.bookStatus IS NOT NULL AND b.bookStatus != 'CANCEL_PAYMENT'") 이 조건 추가해서 푸시
//	AND b.bookStatus != '이용완료' 또 추가 - 230726
	@Query(value = "SELECT new com.project.leisure.dogyeom.booking.BookingVO(b.tempRoomId)" +
            " FROM BookingVO b" + 
            " WHERE " +
            "   ((b.checkin BETWEEN :currentDate AND :nextDate) " +
            "   OR (b.checkOut BETWEEN :currentDate AND :nextDate) " +
            "   OR (:currentDate BETWEEN b.checkin AND b.checkOut) " +
            "   OR (:nextDate BETWEEN b.checkin AND b.checkOut)) " +
            "    AND b.tempAccomId = :tempAccomId " +
            "    AND (:currentDate <> b.checkOut AND :nextDate <> b.checkin)" +
			" AND b.bookStatus IS NOT NULL AND b.bookStatus != 'CANCEL_PAYMENT' AND b.bookStatus != '이용완료'")
	List<BookingVO> findTempRoomId(
            @Param("tempAccomId") Long tempAccomId,
            @Param("currentDate") LocalDate currentDate,
            @Param("nextDate") LocalDate nextDate
    );
	
}