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
	
	List<Accommodation> findAll();

	Page<Accommodation> findAll(Pageable pageable);

	Page<Accommodation> findAll(Specification<Accommodation> spec, Pageable pageable);

	Optional<Accommodation> findById(Long id);

	void save(Users users);
	
}
