package com.project.leisure.yuri.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

	//accommodation 앤티티의 id를 조회
	 List<Product> findByAccommodationId(Long accommodationId);
}
