package com.project.leisure.taeyoung.public_food;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long>, JpaSpecificationExecutor<Food> {
    boolean existsByShopId(String shopId);

    Page<Food> findAll(Pageable pageable);

    Page<Food> findAll(Specification<Food> spec, Pageable pageable);
}