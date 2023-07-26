package com.project.leisure.taeyoung.public_food;

import org.springframework.data.jpa.domain.Specification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

@Service
public class FoodService {
	private final FoodRepository foodRepository;

	@Autowired
	public FoodService(FoodRepository foodRepository) {
		this.foodRepository = foodRepository;
	}

	public boolean existsByShopId(String shopId) {
		return foodRepository.existsByShopId(shopId);
	}

	public void saveFood(Food food) {
		String shopId = food.getShopId();

		if (existsByShopId(shopId)) {
			System.out.println("중복된 데이터입니다: " + shopId);
			return;
		}

		foodRepository.save(food);
		System.out.println("음식 저장됨: " + shopId);
	}

	public List<Food> getAllFoods() {
		return foodRepository.findAll();
	}

	/* 키워드 검색 */
	public Page<Food> getList2(int page, String kw) {
		Pageable pageable = PageRequest.of(page, 20);
		Specification<Food> spec = search(kw);
		return foodRepository.findAll(spec, pageable);
	}

	/* 구역 + 음식 종류 검색 */
	public Page<Food> getList3(int page, String kw2, List<String> kw3) {
		Pageable pageable = PageRequest.of(page, 30);
		Specification<Food> spec = search2(kw2, kw3);
		return foodRepository.findAll(spec, pageable);
	}

	/* 키워드 검색 */
	private Specification<Food> search(String kw) {
		return new Specification<>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Food> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true); // 중복 제거

				String[] keywords = kw.split("\\s+"); // 입력된 키워드를 공백을 기준으로 분리

				List<Predicate> predicates = new ArrayList<>();
				for (String keyword : keywords) {
					Predicate keywordPredicate = cb.and(cb.or(cb.like(q.get("shopAddr"), "%" + keyword + "%"), // 주소로 찾기
							cb.like(q.get("shopName"), "%" + keyword + "%"), // 가게명으로 찾기
							cb.like(q.get("shopCategory"), "%" + keyword + "%"), // 업종으로 찾기
							cb.like(q.get("shopMenu"), "%" + keyword + "%"), // 메뉴로 찾기
							cb.like(q.get("shopInfo"), "%" + keyword + "%") // 소개로 찾기
					));
					predicates.add(keywordPredicate);
				}

				Predicate finalPredicate = cb.and(predicates.toArray(new Predicate[0]));
				return finalPredicate;
			}
		};
	}

	/* 구역 + 음식 종류 검색 */
	private Specification<Food> search2(String kw2, List<String> kw3) {
		return new Specification<>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Food> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				Predicate addressPredicate = criteriaBuilder.like(root.get("shopAddr"), "%" + kw2 + "%");

				List<Predicate> categoryPredicates = new ArrayList<>();

				for (String category : kw3) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("shopCategory"), category));
				}

				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				return criteriaBuilder.and(addressPredicate, categoryPredicate);
			}
		};
	}

	/* 구역만 선택 */
	public Page<Food> getList4(int page, String kw2) {
		Pageable pageable = PageRequest.of(page, 20);
		Specification<Food> spec = search(kw2);
		return foodRepository.findAll(spec, pageable);
	}

	private Specification<Food> search3(String kw2) {
		return new Specification<Food>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Food> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				query.distinct(true); // Remove duplicates

				Predicate keywordPredicate = criteriaBuilder.like(root.get("shopAddr"), "%" + kw2 + "%");

				return keywordPredicate;
			}
		};
	}

	/* 구역 + 주차공간 */
	public Page<Food> getList5(int page, String kw2, String kw4) {
		Pageable pageable = PageRequest.of(page, 20);
		Specification<Food> spec = search4(kw2, kw4);
		return foodRepository.findAll(spec, pageable);
	}

	private Specification<Food> search4(String kw2, String kw4) {
		return (root, query, cb) -> {
			query.distinct(true);

			Predicate keywordPredicate = cb.like(root.get("shopAddr"), "%" + kw2 + "%");
			Predicate parkingPredicate = cb.equal(root.get("shopParking"), kw4);

			return cb.and(keywordPredicate, parkingPredicate);
		};
	}

	/* 구역 + 음식 + 주차공간 */

	public Page<Food> getList6(int page, String kw2, List<String> kw3, String kw4) {
		Pageable pageable = PageRequest.of(page, 30);
		Specification<Food> spec = search5(kw2, kw3, kw4);
		return foodRepository.findAll(spec, pageable);
	}

	private Specification<Food> search5(String kw2, List<String> kw3, String kw4) {
		return new Specification<Food>() {
			@Override
			public Predicate toPredicate(Root<Food> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				Predicate addressPredicate = criteriaBuilder.like(root.get("shopAddr"), "%" + kw2 + "%");

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw3) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("shopCategory"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				Predicate parkingPredicate = criteriaBuilder.equal(root.get("shopParking"), kw4);

				return criteriaBuilder.and(addressPredicate, criteriaBuilder.or(categoryPredicate), parkingPredicate);
			}
		};
	}

}
