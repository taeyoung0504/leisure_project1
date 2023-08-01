package com.project.leisure.son.board;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Integer> {

    Inquiry save(Inquiry inquiry);
    
    Page<Inquiry> findAllByOrderByCreateDateDesc(Pageable pageable);

    List<Inquiry> findByUsernameOrderByCreateDateDesc(String username);
    
	Optional<Inquiry> findById(Integer id);

	Page<Inquiry> findInquiriesByInquiryAnswerIsNotEmptyOrderByCreateDateDesc(Pageable pageable);

	Page<Inquiry> findInquiriesByInquiryAnswerIsEmptyOrderByCreateDateDesc(Pageable pageable);
	
	

}