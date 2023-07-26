package com.project.leisure.dogyeom.booking;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;


@Controller
public class BookingViewController {
	

	private final BookService bookService;
	
	public BookingViewController(BookService bookService) {
		super();
		this.bookService = bookService;
	}
	
	@GetMapping("/kdg/reserve")
	@PreAuthorize("isAuthenticated()")
	public String reservePage(@ModelAttribute("queryParams") BookingVO bookingvo, Model model) {
	    System.out.println("BookingVO: " + bookingvo);
	    // 필요한 로직 수행
	    model.addAttribute("bookingvo", bookingvo);
	    //System.out.println("11111+++++++++s++++++bookingvo : " + model.toString());
	    return "kdg/reserve";
	}
}
