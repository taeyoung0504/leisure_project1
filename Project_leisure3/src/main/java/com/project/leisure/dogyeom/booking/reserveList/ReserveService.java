package com.project.leisure.dogyeom.booking.reserveList;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.leisure.dogyeom.booking.BookingVO;

@Service
public class ReserveService {
	
	private final JpaAlreadyReserveRepository jpaAlreadyReserveRepository;
	
	public ReserveService(JpaAlreadyReserveRepository jpaAlreadyReserveRepository) {
		super();
		this.jpaAlreadyReserveRepository = jpaAlreadyReserveRepository;
//		this.entityManager = entityManager;
	}
	
	public List<BookingVO> getReservedRoomList(Long accomid, LocalDate currentDate,
			LocalDate nextDate) {
		List<BookingVO> reservedRooms = jpaAlreadyReserveRepository.findRoomId(accomid, currentDate, nextDate);
		
		return reservedRooms;
	}
	
	
}
