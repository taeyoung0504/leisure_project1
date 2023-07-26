package com.project.leisure.taeyoung.user;

//ㄴㄴㄴ테스트
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RegRepository extends JpaRepository<RegPartner, Long> {

	Optional<RegPartner> findByUsers_Username(String username);

	void save(Users users);

	@Query("SELECT r.filePath FROM RegPartner r WHERE r.filename = :filename")
	String findFilePathByFilename(String filename);

	RegPartner findByFilename(String filename);

	// 추가 메서드
	Optional<RegPartner> findById(Long id);

	Page<RegPartner> findAll(Pageable pageable);

	Page<RegPartner> findAll(Specification<RegPartner> spec, Pageable pageable);

	Optional<RegPartner> findByUsers(Users users);

}