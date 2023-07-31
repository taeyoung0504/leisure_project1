package com.project.leisure.dogyeom.booking;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.project.leisure.dogyeom.kakao.DataNotFoundException;

@Service
public class BookService {
	
	private BookRepository bookRepository;
	private RoomRepository roomRepository;
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	 @Autowired
	    public BookService(BookRepository bookRepository, RoomRepository roomRepository) {
	        this.bookRepository = bookRepository;
	        this.roomRepository = roomRepository;
	    }

	public int create(BookingVO bookingvo) {
		 
	    BookingVO savedBooking = this.bookRepository.save(bookingvo);
		
	    if (Objects.isNull(savedBooking.getBookNum())) {
	        System.out.println("Booking 저장 실패.");
	        return 0;
	    } else {
	        System.out.println("Booking 저장 성공. BookNum: " + savedBooking.getBookNum());
	        return savedBooking.getBookNum();
	    }
	    
	}

	public BookingVO getBookVO(int bookNum) {
//		int roomID = bookNum;
		 Optional<BookingVO> bookingVO = this.bookRepository.findByBookNum(bookNum);
	        if (bookingVO.isPresent()) {
	            return bookingVO.get();
	        } else {
	            throw new DataNotFoundException("bookingVO not found");
	        }
	}
	
	public BookingVO getBook(String tid) {
//		int roomID = bookNum;
		 Optional<BookingVO> bookingVO = this.bookRepository.findByTid(tid);
	        if (bookingVO.isPresent()) {
	            return bookingVO.get();
	        } else {
	            throw new DataNotFoundException("bookingVO not found");
	        }
	}
	
	public void updateTid(Long bookNum, String tid) {
        String sql = "UPDATE bookingvo SET tid = ? WHERE book_num = ?";
        jdbcTemplate.update(sql, tid, bookNum);
    }
	
	public void updatePaymentDate(String tid, String payDate, String status) {
		String sql = "UPDATE bookingvo SET payment_date = ?, book_status = ? WHERE tid = ?";
		jdbcTemplate.update(sql, payDate, status, tid);
	}
	
	public void updatePaymentDate2(String tid, String payDate, String status, String newTid) {
		String sql = "UPDATE bookingvo SET payment_date = ?, book_status = ?, tid = ? WHERE tid = ?";
		jdbcTemplate.update(sql, payDate, status, newTid, tid);
	}
	
	public void updateCancel(String tid, String status, String canceled_at) {
		String sql = "UPDATE bookingvo SET book_status = ?, canceled_at = ? WHERE tid = ?";
		jdbcTemplate.update(sql, status, canceled_at, tid);
	}
	
	public void updateFail(String tid, String status) {
		String sql = "UPDATE bookingvo SET book_status = ? where tid = ?";
		jdbcTemplate.update(sql, status, tid);
	}
	
	public List<BookingVO> getBookList() {
        Sort sort = Sort.by(Sort.Direction.DESC, "bookNum");
        try {
			return bookRepository.findAll(sort);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
	
	public Room getRoom(int id) {
		
		Optional<Room> room = this.roomRepository.findById(id);
        if (room.isPresent()) {
            return room.get();
        } else {
            throw new DataNotFoundException("room not found");
        }
	}
	
	
	public List<BookingVO> getbooklist(){
		List<BookingVO> bookingList = this.bookRepository.findAll();
	    Collections.reverse(bookingList);
	    return bookingList;
	}
	
}
