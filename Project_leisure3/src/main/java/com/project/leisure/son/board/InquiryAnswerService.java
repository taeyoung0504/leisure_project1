package com.project.leisure.son.board;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class InquiryAnswerService {
	private final InquiryAnswerRepository inquiryAnswerRepository;

	public InquiryAnswerService(InquiryAnswerRepository inquiryAnswerRepository) {
		this.inquiryAnswerRepository = inquiryAnswerRepository;
	}

	public InquiryAnswer saveInquiryAnswer(InquiryAnswer inquiryAnswer) {
		return inquiryAnswerRepository.save(inquiryAnswer);
	}

	public List<InquiryAnswer> findByInquiry(Inquiry inquiry) {
		return inquiryAnswerRepository.findByInquiryOrderByCreateDateDesc(inquiry);
	}
	
}