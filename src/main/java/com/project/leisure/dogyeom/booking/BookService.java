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
import com.project.leisure.yuri.product.Product;

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

	public void updateTid(Long bookNum, String tid) {
		String sql = "UPDATE bookingvo SET tid = ? WHERE book_num = ?";
		jdbcTemplate.update(sql, tid, bookNum);
	}

	public void updatePaymentDate(String tid, String payDate, String status) {
		String sql = "UPDATE bookingvo SET payment_date = ?, book_status = ? WHERE tid = ?";
		jdbcTemplate.update(sql, payDate, status, tid);
	}
	

	public void updateCancel(String tid, String status, String canceled_at) {
		String sql = "UPDATE bookingvo SET book_status = ?, canceled_at = ? WHERE tid = ?";
		jdbcTemplate.update(sql, status, canceled_at, tid);

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
	

	//유리 추가 Tid 로 해당 bookVo를 조회하여 넘겨준다
	  public Optional<BookingVO> findBookTid(String tid) {
	        return bookRepository.findByTid(tid);
	    }


	
	public List<BookingVO> getbooklist(){
		List<BookingVO> bookingList = this.bookRepository.findAll();
	    Collections.reverse(bookingList);
	    return bookingList;
	}
	
	
	//유리 추가 참조된 productID를 조회하여 보낸다
	public void updateBookingVoProductToNull(Product product) {
        // BookingVO 조회
        Optional<BookingVO> bookingVoOptional = bookRepository.findByProduct(product);

        // BookingVO가 존재할 경우 product 값을 null로 설정
        bookingVoOptional.ifPresent(bookingVO -> {
            bookingVO.setProduct(null);
            bookRepository.save(bookingVO); // 변경된 BookingVO를 저장
        });
    }

	
}
