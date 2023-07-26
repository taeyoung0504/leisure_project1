package com.project.leisure.son.board;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.security.Principal;


@Controller
@RequestMapping("/answer")
public class InquiryAnswerController {

	private final InquiryService inquiryService;
	private final InquiryAnswerService inquiryAnswerService;

	public InquiryAnswerController(InquiryService inquiryService, InquiryAnswerService inquiryAnswerService) {
		this.inquiryService = inquiryService;
		this.inquiryAnswerService = inquiryAnswerService;
	}

	@GetMapping("/inquiryAnswer/{id}")
	public String inquiryAnswer(@PathVariable("id") Integer id, Model model) {
	    Optional<Inquiry> inquiry = inquiryService.findById(id);
	    List<InquiryAnswer> answers = inquiryAnswerService.findByInquiry(inquiry.get());
	    model.addAttribute("inquiry", inquiry.get());
	    model.addAttribute("answers", answers);
	    return "syw/inquiry_answer";
	}

	@PostMapping("/inquiryAnswer/{id}")
	public String submitInquiryAnswer(@PathVariable("id") Integer id, @RequestParam("answer") String answer, Principal principal) {
		String username = principal.getName();
		Optional<Inquiry> inquiry = inquiryService.findById(id);
		if (inquiry.isPresent()) {
			InquiryAnswer inquiryAnswer = new InquiryAnswer();
			inquiryAnswer.setMessage(answer);
			inquiryAnswer.setCreateDate(LocalDateTime.now());
			inquiryAnswer.setUsername(username); // 사용자명 설정 (수정 필요)
			inquiryAnswer.setInquiry(inquiry.get());
			inquiryAnswerService.saveInquiryAnswer(inquiryAnswer);
		}
		return "redirect:/admin/inquiryList";
	}
}