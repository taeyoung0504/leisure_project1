package com.project.leisure.taeyoung.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeclarationRepository extends JpaRepository<Declaration, Long> {

	Page<Declaration> findAll(Pageable pageable);

	Page<Declaration> findAll(Specification<Declaration> spec, Pageable pageable);

	int countByReviewId(Long reviewId);

}