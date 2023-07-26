package com.project.leisure.yuri.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.leisure.taeyoung.user.Users;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

	Accommodation findByUsername(String username);

	List<Accommodation> findByusername(String username);

	Page<Accommodation> findAll(Pageable pageable);

	Page<Accommodation> findAll(Specification<Accommodation> spec, Pageable pageable);

//	// 도겸이가 추가한 코드
//	// 모든 숙소(업소) 조회
////	Optional<List<Accommodation>> findAll();
	Optional<Accommodation> findById(Long id);

	// 효경 추가
	void save(Users users);

}
