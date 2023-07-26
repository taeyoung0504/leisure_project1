package com.project.leisure.son.notice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {

	Page<Notice> findAllByOrderByCreateDateDesc(Pageable pageable);

}
