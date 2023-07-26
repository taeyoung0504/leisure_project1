package com.project.leisure.taeyoung.public_food;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class FoodSpecification {
	public static Specification<Food> searchByAddressAndCategories(String kw2, List<String> kw3) {
	    return new Specification<Food>() {
	        @Override
	        public Predicate toPredicate(Root<Food> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
	            Predicate addressPredicate = criteriaBuilder.like(root.get("shopAddr"), "%" + kw2 + "%");

	            if (kw3 == null) {
	                return addressPredicate;
	            }

	            List<Predicate> categoryPredicates = new ArrayList<>();
	            for (String category : kw3) {
	                categoryPredicates.add(criteriaBuilder.equal(root.get("shopCategory"), category));
	            }

	            Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

	            return criteriaBuilder.and(addressPredicate, categoryPredicate);
	        }
	    };
	}
	
	
	
	private Specification<Food> search5(String kw2, List<String> kw3) {
	    return new Specification<Food>() {
	        @Override
	        public Predicate toPredicate(Root<Food> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
	            Predicate addressPredicate = criteriaBuilder.like(root.get("shopAddr"), "%" + kw2 + "%");

	            if (kw3 == null || kw3.isEmpty()) {
	                return addressPredicate;
	            }

	            List<Predicate> categoryPredicates = new ArrayList<>();
	            for (String category : kw3) {
	                categoryPredicates.add(criteriaBuilder.equal(root.get("shopCategory"), category));
	            }

	            Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));
	            return criteriaBuilder.and(addressPredicate, categoryPredicate);
	        }
	    };
	}
}