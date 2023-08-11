package com.project.leisure.son.board;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class InquiryController {
	
	private final InquiryAnswerRepository inquiryAnswerRepository;
	

	public InquiryController(InquiryRepository inquiryRepository, InquiryService inquiryService, InquiryAnswerRepository inquiryAnswerRepository,
			InquiryAnswerService inquiryAnswerService) {
	    this.inquiryRepository = inquiryRepository;
	    this.inquiryService = inquiryService;
	    this.inquiryAnswerRepository = inquiryAnswerRepository;
	    this.inquiryAnswerService = inquiryAnswerService;
	}

    private final InquiryRepository inquiryRepository;
    private final InquiryService inquiryService;
    private final InquiryAnswerService inquiryAnswerService;


    @GetMapping("/more/inquiry")
    public String showInquiryForm(Model model, Principal principal) {
        if (principal == null) {
            return "kty/login_form";
        }

        model.addAttribute("inquiry", new Inquiry());
        return "syw/inquiry";
    }
    
    
    @GetMapping("/mypage/my_inquiry")
    public String my_inquiry(Principal principal, Model model) {
        String username = principal.getName();
        List<Inquiry> inquiries = inquiryService.getFindByUsernameWithAnswers(username);

        for (Inquiry inquiry : inquiries) {
            List<InquiryAnswer> answers = inquiryAnswerRepository.findByInquiryOrderByCreateDateDesc(inquiry);
            inquiry.setInquiryAnswer(answers);
        }
        model.addAttribute("inquiries", inquiries);

        return "syw/my_inquiry";
    }

    @PostMapping("/inquiry")
    public String submitInquiryForm(Inquiry inquiry, Principal principal) {
        String username = principal.getName(); 
        inquiry.setUsername(username); 

        inquiry.setCreateDate(LocalDateTime.now()); 

        inquiryRepository.save(inquiry); 

        return "redirect:/user/mypage/my_inquiry";
    }
    
    
    @GetMapping("/inquiry/delete/{id}")
    public String deleteInquiry(@PathVariable("id") Integer id){
        inquiryService.deleteInquiry(id);
        return "redirect:/user/mypage/my_inquiry";
    }
    

    @GetMapping("/inquiry/modify/{id}")
    public String showInquiryModifyForm(@PathVariable("id") Integer id, Model model, Principal principal) {
        if (principal == null) {
            return "kty/login_form";
        }

        Optional<Inquiry> optionalInquiry = inquiryRepository.findById(id);
        if (optionalInquiry.isEmpty()) {
            return "redirect:/user/mypage/my_inquiry";
        }

        model.addAttribute("inquiry", optionalInquiry.get());
        return "syw/inquiryModify";
    }
    
    @PostMapping("/inquiry/modify/{id}")
    public String modifyInquiry(@PathVariable("id") Integer id, Inquiry inquiry, Principal principal) {
        String username = principal.getName(); 

        Optional<Inquiry> optionalInquiry = inquiryRepository.findById(id);
        if (optionalInquiry.isEmpty()) {
            return "redirect:/user/mypage/my_inquiry";
        }

        Inquiry existingInquiry = optionalInquiry.get();

        if (!username.equals(existingInquiry.getUsername())) {
            return "redirect:/user/mypage/my_inquiry";
        }

        existingInquiry.setTitle(inquiry.getTitle());
        existingInquiry.setCategory(inquiry.getCategory());
        existingInquiry.setType(inquiry.getType());
        existingInquiry.setMessage(inquiry.getMessage());

        inquiryRepository.save(existingInquiry);

        return "redirect:/user/mypage/my_inquiry";
    }
    
}