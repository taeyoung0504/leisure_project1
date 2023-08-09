package com.project.leisure.son.notice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NoticeController {

	private NoticeService noticeService;
	
	public NoticeController(NoticeService noticeService) {
		this.noticeService = noticeService;
	}
	
	////////
	@GetMapping("/more/noticeList")
	public String noticeList(Model model, @PageableDefault(size = 10, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
	    Page<Notice> page = noticeService.getAllNotices(pageable);
	    model.addAttribute("page", page);
	    return "syw/noticeList";
	}
	
}
