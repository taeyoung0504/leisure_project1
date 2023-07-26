package com.project.leisure.dogyeom.base;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.project.leisure.dogyeom.booking.BookRepository;
import com.project.leisure.dogyeom.booking.BookingVO;

@Service
public class BookStatusUpdateService {
	
	private final BookRepository bookRepository;

    public BookStatusUpdateService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    
    @Scheduled(fixedDelay = 10000)
    public void updateBookStatus() {
        LocalDate currentDate = LocalDate.now();
        System.out.println("?????????????????????? currentDate" + currentDate);
        List<BookingVO> booksToBeUpdated = bookRepository.findByBookStatusAndCheckOut("이용중", currentDate);

        for (BookingVO book : booksToBeUpdated) {
            book.setBookStatus("이용완료");
            bookRepository.save(book);
        }
    }
    
    // 새로운 스케줄러
    @Scheduled(fixedDelay = 10000)
    public void updateBookStatusByCheckIn() {
        LocalDate currentDate = LocalDate.now();
        List<BookingVO> booksToBeUpdated = bookRepository.findByBookStatusAndCheckin("예약완료", currentDate);

        for (BookingVO book : booksToBeUpdated) {
//            book.setBookStatus("이용완료");
            book.setBookStatus("이용중");
            bookRepository.save(book);
        }
    }
	
}
