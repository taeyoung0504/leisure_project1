package com.project.leisure.son.board;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryAnswerRepository extends JpaRepository<InquiryAnswer, Integer> {

	InquiryAnswer save(InquiryAnswer inquiryAnswer);

    List<InquiryAnswer> findByInquiryOrderByCreateDateDesc(Inquiry inquiry);

    List<InquiryAnswer> findByInquiryId(Integer id);
	
	
}
