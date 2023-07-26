package com.project.leisure.son.board;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquiryAnswerRepository inquiryAnswerRepository;

    public InquiryService(InquiryRepository inquiryRepository,InquiryAnswerRepository inquiryAnswerRepository) {
        this.inquiryRepository = inquiryRepository;
        this.inquiryAnswerRepository = inquiryAnswerRepository;
    }
    


    public Inquiry create(String title, String category, String type, String message, String userName) {
        Inquiry inquiry = new Inquiry();
        inquiry.setTitle(title);
        inquiry.setCategory(category);
        inquiry.setType(type);
        inquiry.setMessage(message);
        inquiry.setCreateDate(LocalDateTime.now()); // 현재 시간으로 설정
        inquiry.setUsername(userName);

        return inquiryRepository.save(inquiry);
    }

	
    public Page<Inquiry> getAllInquiries(Pageable pageable) {
        return inquiryRepository.findAllByOrderByCreateDateDesc(pageable);
    }
    

	public List<Inquiry> getFindByUsername(String username) {
		return inquiryRepository.findByUsernameOrderByCreateDateDesc(username);
	}

	public Optional<Inquiry> findById(Integer id) {
		return inquiryRepository.findById(id);
	}

	public List<Inquiry> getFindByUsernameWithAnswers(String username) {
	    List<Inquiry> inquiries = inquiryRepository.findByUsernameOrderByCreateDateDesc(username);
	    for (Inquiry inquiry : inquiries) {
	        List<InquiryAnswer> answers = inquiryAnswerRepository.findByInquiryId(inquiry.getId());
	        inquiry.setInquiryAnswer(answers);
	    }
	    return inquiries;
	}
	

    public void deleteInquiry(Integer id){
        inquiryRepository.deleteById(id);
    }
    
    public Optional<Inquiry> getInquiryById(Integer id) {
        return inquiryRepository.findById(id);
    }
    
   	
}
