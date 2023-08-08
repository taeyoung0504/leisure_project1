package com.project.leisure.yuri.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.leisure.DataNotFoundException;
import com.project.leisure.dogyeom.booking.BookService;
import com.project.leisure.dogyeom.booking.BookingVO;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccommodationService {

	private final AccommodationRepository accommodationRepository;

	private final BookService bookService;

	// 현재 로그인한 username을 가져와서 있는지 없는지 찾음
	public Accommodation accfindUserName(String username) {

		Accommodation accommodation = accommodationRepository.findByUsername(username);

		return accommodation;

	}

	// 해당 숙소 삭제
	public void deleteAcc(long acc_id) {
		Optional<Accommodation> optionalAccommodation = accommodationRepository.findById(acc_id);
		if (optionalAccommodation.isPresent()) {
			// 숙소를 가져옴 + 추가
			Accommodation accommodation = optionalAccommodation.get();

			// 서버에 있는 숙소 이미지 삭제
			String baseDirectory = "src/main/resources/static/";
			String imgPath = optionalAccommodation.get().getAcc_img();
			String fileImgPath = baseDirectory + imgPath;

			try {
				Path imagePath = Paths.get(fileImgPath);
				Files.delete(imagePath);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// 숙소 삭제 전에 bookVO 에 외래키 연결된거 삭제 + 추가
			bookService.updateBookingVoAccToNull(accommodation);

			// 숙소 삭제
			accommodationRepository.deleteById(acc_id);
		} else {
			throw new RuntimeException("Accommodation not found");
		}
	}

	// 평균 금액 저장(해당하는 숙소에 평균 금액 저장)
	public void saveAverPrice(Accommodation accommodation, int averagePrice) {
		// 숙소 객체에 평균 가격 설정
		accommodation.setAcc_averPrice(averagePrice);

		// 숙소 저장
		accommodationRepository.save(accommodation);

	}

	// id로 방 정보 가져오기
	public Accommodation findByAccId(long acc_id) {
		Optional<Accommodation> optionalAccommodation = accommodationRepository.findById(acc_id);
		if (optionalAccommodation.isPresent()) {
			return optionalAccommodation.get();
		} else {
			throw new RuntimeException("Accommodation not found");
		}
	}

	// 숙소 이미지 및 숙소 업데이트
	// 해당 숙소 이름, 이미지, 설명, 업종, 주소 , 사용자명 등록
	public Integer addAccommodation(String username, String acc_name, String acc_explain, MultipartFile acc_img,
			String acc_sectors, String acc_address, int acc_max_people, Long acc_id, String acc_info) {

		Optional<Accommodation> existingAccommodation = accommodationRepository.findById(acc_id);

		Accommodation acc = existingAccommodation.get();

		acc.setAcc_name(acc_name);
		acc.setAcc_explain(acc_explain);
		acc.setAcc_sectors(acc_sectors);
		acc.setAcc_address(acc_address);
		acc.setAcc_max_people(acc_max_people);
		acc.setAcc_info(acc_info);

		// 이미지 필드가 비어있지 않은 경우에만 이미지 처리를 수행합니다.
		if (acc_img != null && !acc_img.isEmpty()) {
			try {
				UUID uuid = UUID.randomUUID();

				// 파일을 저장할 경로와 파일명 설정
				String fileName = uuid.toString() + "_" + acc_img.getOriginalFilename(); // 원본 파일 이름을 반환
				String filePath = "src/main/resources/static/img/acc_img/" + fileName;

				// 파일 저장
				Path targetPath = Path.of(filePath);
				Files.copy(acc_img.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

				// 이미지를 저장한 파일 경로 설정
				acc.setAcc_img("/img/acc_img/" + fileName);
			} catch (IOException e) {
				// 예외 처리
				e.printStackTrace();
				return null; // 실패
			}
		}

		this.accommodationRepository.save(acc);

		return 1; // 성공
	}

	// 새로운 객실 추가 등록
	public Integer addNewAccommodation(String username, String acc_name, String acc_explain, MultipartFile acc_img,
			String acc_sectors, String acc_address, int acc_max_people, String acc_info) {

		Accommodation acc = new Accommodation();

		acc.setUsername(username);
		acc.setAcc_name(acc_name);
		acc.setAcc_explain(acc_explain);
		acc.setAcc_sectors(acc_sectors);
		acc.setAcc_address(acc_address);
		acc.setAcc_max_people(acc_max_people);
		acc.setAcc_info(acc_info);

		// 이미지 필드가 비어있지 않은 경우에만 이미지 처리를 수행합니다.
		if (acc_img != null && !acc_img.isEmpty()) {
			try {
				UUID uuid = UUID.randomUUID();

				// 파일을 저장할 경로와 파일명 설정
				String fileName = uuid.toString() + "_" + acc_img.getOriginalFilename(); // 원본 파일 이름을 반환
				String filePath = "src/main/resources/static/img/acc_img/" + fileName;

				// 파일 저장
				Path targetPath = Path.of(filePath);
				Files.copy(acc_img.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

				// 이미지를 저장한 파일 경로 설정
				acc.setAcc_img("/img/acc_img/" + fileName);
			} catch (IOException e) {
				e.printStackTrace();
				return -1; // 실패
			}
		}

		this.accommodationRepository.save(acc);

		return 1; // 성공
	}

	public List<Accommodation> findAccommodationsByUsername(String username) {
		return accommodationRepository.findByusername(username);
	}

	/* 서비스 */
	public Accommodation getAccommodation(Long id) {
		Optional<Accommodation> acc = this.accommodationRepository.findById(id);
		return acc.get();
	}

	/* 방찾기 페이지 페이징 */
	public Page<Accommodation> getList(int page) {
		Pageable pageable = PageRequest.of(page, 10); // 10개씩 보이게
		return this.accommodationRepository.findAll(pageable);
	}

	/* 키워드 검색 */

	public Page<Accommodation> getList(int page, String kw) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search(kw);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search(String kw) {
		return new Specification<>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Accommodation> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true); // 중복 제거

				String[] keywords = kw.split("\\s+"); // 입력된 키워드를 공백을 기준으로 분리

				List<Predicate> predicates = new ArrayList<>();
				for (String keyword : keywords) {
					Predicate keywordPredicate = cb.and(cb.or(cb.like(q.get("acc_address"), "%" + keyword + "%"), // 주소로
							// 찾기
							cb.like(q.get("acc_name"), "%" + keyword + "%"), // 가게명으로 찾기
							cb.like(q.get("acc_sectors"), "%" + keyword + "%"), // 업종으로 찾기
							cb.like(q.get("acc_explain"), "%" + keyword + "%") // 메뉴로 찾기
					));
					predicates.add(keywordPredicate);
				}

				Predicate finalPredicate = cb.and(predicates.toArray(new Predicate[0]));
				return finalPredicate;
			}
		};
	}

	/* 구역 선택 필터 */

	public Page<Accommodation> getList2(int page, String kw2) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search2(kw2);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search2(String kw2) {
		return new Specification<Accommodation>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true); // Remove duplicates

				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				return keywordPredicate;
			}
		};
	}

	/* 숙소 타입 필터 */
	public Page<Accommodation> getList3(int page, List<String> kw5) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search3(kw5);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search3(List<String> kw5) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				return criteriaBuilder.and(categoryPredicate);
			}
		};
	}

	/* 투숙 인원 필터 */
	public Page<Accommodation> getList4(int page, int kw7) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search4(kw7);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search4(int kw7) {
		return new Specification<Accommodation>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true); // Remove duplicates

				Predicate keywordPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), kw7);

				return keywordPredicate;
			}
		};
	}

	/* 평점 필터 */

	public Page<Accommodation> getList5(int page, List<String> kw6) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search5(kw6);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search5(List<String> kw6) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw6) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_rating"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				return criteriaBuilder.and(categoryPredicate);
			}
		};
	}

	/* 구역 + 숙소타입 + 투숙인원 + 평점 필터 */

	public Page<Accommodation> getList6(int page, String kw2, List<String> kw5, List<String> kw6, int kw7) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search6(kw2, kw5, kw6, kw7);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search6(String kw2, List<String> kw5, List<String> kw6, int kw7) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				/* 주소 */
				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				/* 숙소 타입 */
				List<Predicate> categoryPredicates1 = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates1.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates1.toArray(new Predicate[0]));

				/* 리뷰 */
				List<Predicate> categoryPredicates2 = new ArrayList<>();
				for (String category2 : kw6) {
					categoryPredicates2.add(criteriaBuilder.equal(root.get("acc_rating"), category2));
				}
				Predicate categoryPredicate2 = criteriaBuilder.or(categoryPredicates2.toArray(new Predicate[0]));

				/* 투숙 인원 */
				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"),kw7);
						

				return criteriaBuilder.and(keywordPredicate, categoryPredicate, categoryPredicate2, keywordPredicate2);
			}
		};
	}

	/* 구역 + 숙소타입 */
	public Page<Accommodation> getList7(int page, String kw2, List<String> kw5) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search7(kw2, kw5);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search7(String kw2, List<String> kw5) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				/* 주소 */
				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				/* 숙소 타입 */
				List<Predicate> categoryPredicates1 = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates1.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates1.toArray(new Predicate[0]));

				return criteriaBuilder.and(keywordPredicate, categoryPredicate);
			}
		};
	}

	/* 구역 + 숙소타입+평점 */
	public Page<Accommodation> getList8(int page, String kw2, List<String> kw5, List<String> kw6) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search8(kw2, kw5, kw6);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search8(String kw2, List<String> kw5, List<String> kw6) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				/* 주소 */
				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				/* 숙소 타입 */
				List<Predicate> categoryPredicates1 = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates1.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates1.toArray(new Predicate[0]));

				/* 리뷰 */
				List<Predicate> categoryPredicates2 = new ArrayList<>();
				for (String category2 : kw6) {
					categoryPredicates2.add(criteriaBuilder.equal(root.get("acc_rating"), category2));
				}
				Predicate categoryPredicate2 = criteriaBuilder.or(categoryPredicates2.toArray(new Predicate[0]));

				return criteriaBuilder.and(keywordPredicate, categoryPredicate, categoryPredicate2);
			}
		};
	}

	/* 구역 + 숙소타입+투숙인원 */
	public Page<Accommodation> getList9(int page, String kw2, List<String> kw5, int kw7) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search9(kw2, kw5, kw7);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search9(String kw2, List<String> kw5, int kw7) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				/* 주소 */
				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				/* 숙소 타입 */
				List<Predicate> categoryPredicates1 = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates1.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates1.toArray(new Predicate[0]));

				/* 투숙 인원 */
				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), kw7);

				return criteriaBuilder.and(keywordPredicate, categoryPredicate, keywordPredicate2);
			}
		};
	}

	/* 구역 + 평점 */
	public Page<Accommodation> getList10(int page, String kw2, List<String> kw6) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search10(kw2, kw6);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search10(String kw2, List<String> kw6) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				/* 주소 */
				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				/* 리뷰 */
				List<Predicate> categoryPredicates2 = new ArrayList<>();
				for (String category2 : kw6) {
					categoryPredicates2.add(criteriaBuilder.equal(root.get("acc_rating"), category2));
				}
				Predicate categoryPredicate2 = criteriaBuilder.or(categoryPredicates2.toArray(new Predicate[0]));

				return criteriaBuilder.and(keywordPredicate, categoryPredicate2);
			}
		};
	}

	/* 구역 + 평점 + 투숙인원 */
	public Page<Accommodation> getList11(int page, String kw2, List<String> kw6, String kw7) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search11(kw2, kw6, kw7);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search11(String kw2, List<String> kw6, String kw7) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				/* 주소 */
				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				/* 리뷰 */
				List<Predicate> categoryPredicates2 = new ArrayList<>();
				for (String category2 : kw6) {
					categoryPredicates2.add(criteriaBuilder.equal(root.get("acc_rating"), category2));
				}
				Predicate categoryPredicate2 = criteriaBuilder.or(categoryPredicates2.toArray(new Predicate[0]));

				/* 투숙 인원 */
				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), Integer.parseInt(kw7));

				return criteriaBuilder.and(keywordPredicate, categoryPredicate2, keywordPredicate2);
			}
		};
	}

	/* 구역 + 평점 + 투숙인원 */
	public Page<Accommodation> getList12(int page, String kw2, String kw7) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search12(kw2, kw7);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search12(String kw2, String kw7) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				/* 주소 */
				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				/* 투숙 인원 */
				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), Integer.parseInt(kw7));

				return criteriaBuilder.and(keywordPredicate, keywordPredicate2);
			}
		};
	}

	/* 구역 + 평점 + 투숙인원 */
	public Page<Accommodation> getList13(int page, List<String> kw5, List<String> kw6) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search13(kw5, kw6);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search13(List<String> kw5, List<String> kw6) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				/* 숙소 타입 */
				List<Predicate> categoryPredicates1 = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates1.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates1.toArray(new Predicate[0]));

				/* 리뷰 */
				List<Predicate> categoryPredicates2 = new ArrayList<>();
				for (String category2 : kw6) {
					categoryPredicates2.add(criteriaBuilder.equal(root.get("acc_rating"), category2));
				}
				Predicate categoryPredicate2 = criteriaBuilder.or(categoryPredicates2.toArray(new Predicate[0]));

				return criteriaBuilder.and(categoryPredicate, categoryPredicate2);
			}

		};
	}

	/* 숙소타입 + 평점 + 투숙인원 */
	public Page<Accommodation> getList14(int page, List<String> kw5, List<String> kw6, String kw7) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search14(kw5, kw6, kw7);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search14(List<String> kw5, List<String> kw6, String kw7) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				/* 숙소 타입 */
				List<Predicate> categoryPredicates1 = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates1.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates1.toArray(new Predicate[0]));

				/* 리뷰 */
				List<Predicate> categoryPredicates2 = new ArrayList<>();
				for (String category2 : kw6) {
					categoryPredicates2.add(criteriaBuilder.equal(root.get("acc_rating"), category2));
				}
				Predicate categoryPredicate2 = criteriaBuilder.or(categoryPredicates2.toArray(new Predicate[0]));

				/* 투숙 인원 */
				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), Integer.parseInt(kw7));

				return criteriaBuilder.and(categoryPredicate, categoryPredicate2, keywordPredicate2);
			}
		};
	}

	/* 숙소타입 + 평점 + 투숙인원 */
	public Page<Accommodation> getList15(int page, List<String> kw6, String kw7) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search15(kw6, kw7);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search15(List<String> kw6, String kw7) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				/* 리뷰 */
				List<Predicate> categoryPredicates2 = new ArrayList<>();
				for (String category2 : kw6) {
					categoryPredicates2.add(criteriaBuilder.equal(root.get("acc_rating"), category2));
				}
				Predicate categoryPredicate2 = criteriaBuilder.or(categoryPredicates2.toArray(new Predicate[0]));

				/* 투숙 인원 */
				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), Integer.parseInt(kw7));

				return criteriaBuilder.and(categoryPredicate2, keywordPredicate2);
			}
		};
	}

	/* 최소금액 */
	public Page<Accommodation> getList16(int page, String kw8) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search16(kw8);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search16(String kw8) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				Predicate keywordPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8);

				return criteriaBuilder.and(keywordPredicate);
			}
		};

	}

	/* 최대금액 */
	public Page<Accommodation> getList17(int page, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search17(kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search17(String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				Predicate keywordPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9);

				return criteriaBuilder.and(keywordPredicate);
			}
		};

	}

	/* 최소+최대금액 */
	public Page<Accommodation> getList18(int page, String kw8, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search18(kw8, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search18(String kw8, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				int kw8Value = Integer.parseInt(kw8);
				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);
				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(keywordPredicate, keywordPredicate2);
			}
		};
	}

	/* 지역+최소금액 */
	public Page<Accommodation> getList19(int page, String kw2, String kw8) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search19(kw2, kw8);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search19(String kw2, String kw8) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				/* 주소 */
				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				int kw8Value = Integer.parseInt(kw8);

				Predicate keywordPredicate2 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);

				return criteriaBuilder.and(keywordPredicate, keywordPredicate2);
			}
		};
	}

	/* 지역+최소금액 */
	public Page<Accommodation> getList20(int page, String kw2, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search20(kw2, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search20(String kw2, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				/* 주소 */
				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(keywordPredicate, keywordPredicate2);
			}
		};
	}

	/* 지역+최소+최대금액 */
	public Page<Accommodation> getList21(int page, String kw2, String kw8, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search21(kw2, kw8, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search21(String kw2, String kw8, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");
				int kw8Value = Integer.parseInt(kw8);
				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate2 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);
				Predicate keywordPredicate3 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(keywordPredicate, keywordPredicate2, keywordPredicate3);
			}
		};
	}

	/* 구역+최소 */

	/* 숙소+최소금액 */
	public Page<Accommodation> getList22(int page, List<String> kw5, String kw8) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search22(kw5, kw8);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search22(List<String> kw5, String kw8) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				int kw8Value = Integer.parseInt(kw8);
				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				Predicate keywordPredicate2 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);

				return criteriaBuilder.and(categoryPredicate, keywordPredicate2);
			}
		};
	}

	/* 숙소+최대금액 */
	public Page<Accommodation> getList23(int page, List<String> kw5, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search23(kw5, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search23(List<String> kw5, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				int kw9Value = Integer.parseInt(kw9);

				/* 숙소 타입 */
				List<Predicate> categoryPredicates1 = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates1.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate1 = criteriaBuilder.or(categoryPredicates1.toArray(new Predicate[0]));

				return criteriaBuilder.and(categoryPredicate, categoryPredicate1);
			}
		};
	}

	/* 숙소+최소+최대금액 */
	public Page<Accommodation> getList24(int page, List<String> kw5, String kw8, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search24(kw5, kw8, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search24(List<String> kw5, String kw8, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));
				int kw8Value = Integer.parseInt(kw8);
				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate2 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);
				Predicate keywordPredicate3 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(categoryPredicate, keywordPredicate2, keywordPredicate3);
			}
		};
	}

	/* 지역+숙소+최소 */
	public Page<Accommodation> getList25(int page, String kw2, List<String> kw5, String kw8) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search25(kw2, kw5, kw8);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search25(String kw2, List<String> kw5, String kw8) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				int kw8Value = Integer.parseInt(kw8);

				Predicate keywordPredicate3 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);

				return criteriaBuilder.and(keywordPredicate, categoryPredicate, keywordPredicate3);
			}
		};
	}

	/* 지역+숙소+최소 */
	public Page<Accommodation> getList26(int page, String kw2, List<String> kw5, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search26(kw2, kw5, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search26(String kw2, List<String> kw5, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate2 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(keywordPredicate, categoryPredicate, keywordPredicate2);
			}
		};
	}

	/* 지역+숙소+최소+최대 */
	public Page<Accommodation> getList27(int page, String kw2, List<String> kw5, String kw8, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search27(kw2, kw5, kw8, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search27(String kw2, List<String> kw5, String kw8, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				int kw8Value = Integer.parseInt(kw8);
				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate2 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);
				Predicate keywordPredicate3 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(keywordPredicate, categoryPredicate, keywordPredicate2, keywordPredicate3);
			}
		};
	}

	/* 평점+최소 */
	public Page<Accommodation> getList28(int page, List<String> kw6, String kw8) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search28(kw6, kw8);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search28(List<String> kw6, String kw8) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw6) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_rating"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				int kw8Value = Integer.parseInt(kw8);
				Predicate keywordPredicate2 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);

				return criteriaBuilder.and(categoryPredicate, keywordPredicate2);
			}
		};
	}

	/* 평점+최대 */
	public Page<Accommodation> getList29(int page, List<String> kw6, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search29(kw6, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search29(List<String> kw6, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw6) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_rating"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				int kw9Value = Integer.parseInt(kw9);
				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(categoryPredicate, keywordPredicate2);
			}
		};
	}

	/* 평점+최소+최대 */
	public Page<Accommodation> getList30(int page, List<String> kw6, String kw8, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search30(kw6, kw8, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search30(List<String> kw6, String kw8, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw6) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_rating"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				int kw8Value = Integer.parseInt(kw8);
				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate2 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);
				Predicate keywordPredicate3 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(categoryPredicate, keywordPredicate2, keywordPredicate3);
			}
		};
	}

	/* 지역+숙소+평점+최소 */
	public Page<Accommodation> getList31(int page, String kw2, List<String> kw5, List<String> kw6, String kw8) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search31(kw2, kw5, kw6, kw8);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search31(String kw2, List<String> kw5, List<String> kw6, String kw8) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				List<Predicate> categoryPredicates2 = new ArrayList<>();
				for (String category : kw6) {
					categoryPredicates2.add(criteriaBuilder.equal(root.get("acc_rating"), category));
				}
				Predicate categoryPredicate2 = criteriaBuilder.or(categoryPredicates2.toArray(new Predicate[0]));

				int kw8Value = Integer.parseInt(kw8);

				Predicate keywordPredicate2 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);

				return criteriaBuilder.and(keywordPredicate, categoryPredicate, categoryPredicate2, keywordPredicate2);
			}
		};
	}

	/* 지역+숙소+평점+최대 */
	public Page<Accommodation> getList32(int page, String kw2, List<String> kw5, List<String> kw6, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search32(kw2, kw5, kw6, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search32(String kw2, List<String> kw5, List<String> kw6, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				List<Predicate> categoryPredicates2 = new ArrayList<>();
				for (String category : kw6) {
					categoryPredicates2.add(criteriaBuilder.equal(root.get("acc_rating"), category));
				}
				Predicate categoryPredicate2 = criteriaBuilder.or(categoryPredicates2.toArray(new Predicate[0]));

				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(keywordPredicate, categoryPredicate, categoryPredicate2, keywordPredicate2);
			}
		};
	}

	/* 지역+숙소+평점+최소+최대 */
	public Page<Accommodation> getList33(int page, String kw2, List<String> kw5, List<String> kw6, String kw8,
			String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search33(kw2, kw5, kw6, kw8, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search33(String kw2, List<String> kw5, List<String> kw6, String kw8,
			String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				List<Predicate> categoryPredicates2 = new ArrayList<>();
				for (String category : kw6) {
					categoryPredicates2.add(criteriaBuilder.equal(root.get("acc_rating"), category));
				}
				Predicate categoryPredicate2 = criteriaBuilder.or(categoryPredicates2.toArray(new Predicate[0]));

				int kw8Value = Integer.parseInt(kw8);
				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate2 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);
				Predicate keywordPredicate3 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(keywordPredicate, categoryPredicate, categoryPredicate2, keywordPredicate2,
						keywordPredicate3);
			}
		};
	}

	/* 지역+평점+최소 */
	public Page<Accommodation> getList34(int page, String kw2, List<String> kw6, String kw8) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search34(kw2, kw6, kw8);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search34(String kw2, List<String> kw6, String kw8) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				List<Predicate> categoryPredicates2 = new ArrayList<>();
				for (String category : kw6) {
					categoryPredicates2.add(criteriaBuilder.equal(root.get("acc_rating"), category));
				}
				Predicate categoryPredicate2 = criteriaBuilder.or(categoryPredicates2.toArray(new Predicate[0]));

				int kw8Value = Integer.parseInt(kw8);

				Predicate keywordPredicate2 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);

				return criteriaBuilder.and(keywordPredicate, categoryPredicate2, keywordPredicate2);
			}
		};
	}

	/* 지역+평점+최대 */
	public Page<Accommodation> getList35(int page, String kw2, List<String> kw6, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search35(kw2, kw6, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search35(String kw2, List<String> kw6, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				List<Predicate> categoryPredicates2 = new ArrayList<>();
				for (String category : kw6) {
					categoryPredicates2.add(criteriaBuilder.equal(root.get("acc_rating"), category));
				}
				Predicate categoryPredicate2 = criteriaBuilder.or(categoryPredicates2.toArray(new Predicate[0]));

				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(keywordPredicate, categoryPredicate2, keywordPredicate2);
			}
		};
	}

	/* 지역+평점+최소+최대 */
	public Page<Accommodation> getList36(int page, String kw2, List<String> kw6, String kw8, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search36(kw2, kw6, kw8, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search36(String kw2, List<String> kw6, String kw8, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				List<Predicate> categoryPredicates2 = new ArrayList<>();
				for (String category : kw6) {
					categoryPredicates2.add(criteriaBuilder.equal(root.get("acc_rating"), category));
				}
				Predicate categoryPredicate2 = criteriaBuilder.or(categoryPredicates2.toArray(new Predicate[0]));

				int kw8Value = Integer.parseInt(kw8);
				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate2 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);
				Predicate keywordPredicate3 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(keywordPredicate, categoryPredicate2, keywordPredicate2, keywordPredicate3);
			}
		};
	}

	/* 숙소+평점+최소 */
	public Page<Accommodation> getList37(int page, List<String> kw5, List<String> kw6, String kw8) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search37(kw5, kw6, kw8);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search37(List<String> kw5, List<String> kw6, String kw8) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				List<Predicate> categoryPredicates2 = new ArrayList<>();
				for (String category : kw6) {
					categoryPredicates2.add(criteriaBuilder.equal(root.get("acc_rating"), category));
				}
				Predicate categoryPredicate2 = criteriaBuilder.or(categoryPredicates2.toArray(new Predicate[0]));

				int kw8Value = Integer.parseInt(kw8);

				Predicate keywordPredicate2 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);

				return criteriaBuilder.and(categoryPredicate2, keywordPredicate2);
			}
		};
	}

	/* 숙소+평점+최대 */
	public Page<Accommodation> getList38(int page, List<String> kw5, List<String> kw6, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search38(kw5, kw6, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search38(List<String> kw5, List<String> kw6, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				List<Predicate> categoryPredicates2 = new ArrayList<>();
				for (String category : kw6) {
					categoryPredicates2.add(criteriaBuilder.equal(root.get("acc_rating"), category));
				}
				Predicate categoryPredicate2 = criteriaBuilder.or(categoryPredicates2.toArray(new Predicate[0]));

				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(categoryPredicate2, keywordPredicate2);
			}
		};
	}

	public Page<Accommodation> getList39(int page, List<String> kw5, List<String> kw6, String kw8, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search39(kw5, kw6, kw8, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search39(List<String> kw5, List<String> kw6, String kw8, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				List<Predicate> categoryPredicates2 = new ArrayList<>();
				for (String category : kw6) {
					categoryPredicates2.add(criteriaBuilder.equal(root.get("acc_rating"), category));
				}
				Predicate categoryPredicate2 = criteriaBuilder.or(categoryPredicates2.toArray(new Predicate[0]));

				int kw8Value = Integer.parseInt(kw8);
				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate2 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);
				Predicate keywordPredicate3 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(categoryPredicate, categoryPredicate2, keywordPredicate2, keywordPredicate3);
			}
		};
	}

	public Page<Accommodation> getList40(int page, int kw7, String kw8) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search40(kw7, kw8);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search40(int kw7, String kw8) {
		return new Specification<Accommodation>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true); // Remove duplicates

				Predicate keywordPredicate =criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), kw7);

				int kw8Value = Integer.parseInt(kw8);
				Predicate keywordPredicate2 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);

				return criteriaBuilder.and(keywordPredicate, keywordPredicate2);
			}
		};
	}

	public Page<Accommodation> getList41(int page, int kw7, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search41(kw7, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search41(int kw7, String kw9) {
		return new Specification<Accommodation>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true); // Remove duplicates

				Predicate keywordPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), kw7);

				int kw9Value = Integer.parseInt(kw9);
				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(keywordPredicate, keywordPredicate2);
			}
		};
	}

	public Page<Accommodation> getList42(int page, int kw7, String kw8, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search42(kw7, kw8, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search42(int kw7, String kw8, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				Predicate keywordPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), kw7);

				int kw8Value = Integer.parseInt(kw8);
				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate2 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);
				Predicate keywordPredicate3 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(keywordPredicate, keywordPredicate2, keywordPredicate3);
			}
		};
	}

	public Page<Accommodation> getList43(int page, String kw2, int kw7, String kw8) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search43(kw2, kw7, kw8);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search43(String kw2, int kw7, String kw8) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), kw7);

				int kw8Value = Integer.parseInt(kw8);

				Predicate keywordPredicate3 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);

				return criteriaBuilder.and(keywordPredicate, keywordPredicate2, keywordPredicate3);
			}
		};
	}

	/* 지역 + 투숙인원 + 최대 */
	public Page<Accommodation> getList44(int page, String kw2, int kw7, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search44(kw2, kw7, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search44(String kw2, int kw7, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), kw7);

				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate3 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(keywordPredicate, keywordPredicate2, keywordPredicate3);
			}
		};
	}

	public Page<Accommodation> getList45(int page, String kw2, int kw7, String kw8, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search45(kw2, kw7, kw8, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search45(String kw2, int kw7, String kw8, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), kw7);

				int kw8Value = Integer.parseInt(kw8);
				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate3 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);
				Predicate keywordPredicate4 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(keywordPredicate, keywordPredicate2, keywordPredicate3, keywordPredicate4);
			}
		};
	}

	public Page<Accommodation> getList46(int page, List<String> kw5, int kw7, String kw8) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search46(kw5, kw7, kw8);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search46(List<String> kw5, int kw7, String kw8) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				Predicate keywordPredicate2 =criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), kw7);

				int kw8Value = Integer.parseInt(kw8);

				Predicate keywordPredicate3 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);

				return criteriaBuilder.and(categoryPredicate, keywordPredicate2, keywordPredicate3);
			}
		};
	}

	public Page<Accommodation> getList47(int page, List<String> kw5, int kw7, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search47(kw5, kw7, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search47(List<String> kw5, int kw7, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), kw7);

				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate3 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(categoryPredicate, keywordPredicate2, keywordPredicate3);
			}
		};
	}

	public Page<Accommodation> getList48(int page, List<String> kw5, int kw7, String kw8, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search48(kw5, kw7, kw8, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search48(List<String> kw5, int kw7, String kw8, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), kw7);

				int kw8Value = Integer.parseInt(kw8);
				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate3 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);
				Predicate keywordPredicate4 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(categoryPredicate, keywordPredicate2, keywordPredicate3, keywordPredicate4);
			}
		};
	}

	public Page<Accommodation> getList49(int page, List<String> kw6, int kw7, String kw8) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search49(kw6, kw7, kw8);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search49(List<String> kw6, int kw7, String kw8) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw6) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_rating"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), kw7);

				int kw8Value = Integer.parseInt(kw8);

				Predicate keywordPredicate3 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);

				return criteriaBuilder.and(categoryPredicate, keywordPredicate2, keywordPredicate3);
			}
		};
	}

	public Page<Accommodation> getList50(int page, List<String> kw6, int kw7, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search50(kw6, kw7, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search50(List<String> kw6, int kw7, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw6) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_rating"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				Predicate keywordPredicate2 =criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), kw7);

				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate3 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(categoryPredicate, keywordPredicate2, keywordPredicate3);
			}
		};
	}

	public Page<Accommodation> getList51(int page, List<String> kw6, int kw7, String kw8, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search51(kw6, kw7, kw8, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search51(List<String> kw6, int kw7, String kw8, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw6) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_rating"), category));
				}

				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), kw7);

				int kw8Value = Integer.parseInt(kw8);
				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate3 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);
				Predicate keywordPredicate4 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(categoryPredicate, keywordPredicate2, keywordPredicate3, keywordPredicate4);
			}
		};
	}

	public Page<Accommodation> getList52(int page, List<String> kw5, List<String> kw6, int kw7, String kw8) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search52(kw5, kw6, kw7, kw8);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search52(List<String> kw5, List<String> kw6, int kw7, String kw8) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				List<Predicate> categoryPredicates2 = new ArrayList<>();
				for (String category : kw6) {
					categoryPredicates2.add(criteriaBuilder.equal(root.get("acc_rating"), category));
				}
				Predicate categoryPredicate2 = criteriaBuilder.or(categoryPredicates2.toArray(new Predicate[0]));

				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), kw7);

				int kw8Value = Integer.parseInt(kw8);

				Predicate keywordPredicate3 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);

				return criteriaBuilder.and(categoryPredicate, keywordPredicate2, keywordPredicate2, keywordPredicate3);
			}
		};
	}

	public Page<Accommodation> getList53(int page, List<String> kw5, List<String> kw6, int kw7, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search53(kw5, kw6, kw7, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search53(List<String> kw5, List<String> kw6, int kw7, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				List<Predicate> categoryPredicates2 = new ArrayList<>();
				for (String category : kw6) {
					categoryPredicates2.add(criteriaBuilder.equal(root.get("acc_rating"), category));
				}
				Predicate categoryPredicate2 = criteriaBuilder.or(categoryPredicates2.toArray(new Predicate[0]));

				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), kw7);

				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate3 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(categoryPredicate, keywordPredicate2, keywordPredicate2, keywordPredicate3);
			}
		};
	}

	public Page<Accommodation> getList54(int page, List<String> kw5, List<String> kw6, int kw7, String kw8,
			String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search54(kw5, kw6, kw7, kw8, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search54(List<String> kw5, List<String> kw6, int kw7, String kw8, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				List<Predicate> categoryPredicates2 = new ArrayList<>();
				for (String category : kw6) {
					categoryPredicates2.add(criteriaBuilder.equal(root.get("acc_rating"), category));
				}
				Predicate categoryPredicate2 = criteriaBuilder.or(categoryPredicates2.toArray(new Predicate[0]));

				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), kw7);

				int kw8Value = Integer.parseInt(kw8);
				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate3 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);
				Predicate keywordPredicate4 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(categoryPredicate, categoryPredicate2, keywordPredicate2, keywordPredicate3,
						keywordPredicate4);
			}
		};
	}

	public Page<Accommodation> getList55(int page, String kw2, List<String> kw5, int kw7, String kw8) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search55(kw2, kw5, kw7, kw8);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search55(String kw2, List<String> kw5, int kw7, String kw8) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), kw7);

				int kw8Value = Integer.parseInt(kw8);

				Predicate keywordPredicate3 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);

				return criteriaBuilder.and(keywordPredicate, categoryPredicate, keywordPredicate2, keywordPredicate3);
			}
		};
	}

	public Page<Accommodation> getList56(int page, String kw2, List<String> kw5, int kw7, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search56(kw2, kw5, kw7, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search56(String kw2, List<String> kw5, int kw7, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), kw7);

				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate3 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(keywordPredicate, categoryPredicate, keywordPredicate2, keywordPredicate3);
			}
		};
	}

	public Page<Accommodation> getList57(int page, String kw2, List<String> kw5, int kw7, String kw8, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search57(kw2, kw5, kw7, kw8, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search57(String kw2, List<String> kw5, int kw7, String kw8, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), kw7);

				int kw8Value = Integer.parseInt(kw8);
				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate3 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);
				Predicate keywordPredicate4 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(keywordPredicate, categoryPredicate, keywordPredicate2, keywordPredicate3,
						keywordPredicate4);
			}
		};
	}

	public Page<Accommodation> getList58(int page, String kw2, List<String> kw6, int kw7, String kw8) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search58(kw2, kw6, kw7, kw8);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search58(String kw2, List<String> kw6, int kw7, String kw8) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				List<Predicate> categoryPredicates2 = new ArrayList<>();
				for (String category : kw6) {
					categoryPredicates2.add(criteriaBuilder.equal(root.get("acc_rating"), category));
				}
				Predicate categoryPredicate2 = criteriaBuilder.or(categoryPredicates2.toArray(new Predicate[0]));

				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), kw7);

				int kw8Value = Integer.parseInt(kw8);

				Predicate keywordPredicate3 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);

				return criteriaBuilder.and(keywordPredicate, categoryPredicate2, keywordPredicate, keywordPredicate3);
			}
		};
	}

	public Page<Accommodation> getList59(int page, String kw2, List<String> kw6, int kw7, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search59(kw2, kw6, kw7, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search59(String kw2, List<String> kw6, int kw7, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				List<Predicate> categoryPredicates2 = new ArrayList<>();
				for (String category : kw6) {
					categoryPredicates2.add(criteriaBuilder.equal(root.get("acc_rating"), category));
				}
				Predicate categoryPredicate2 = criteriaBuilder.or(categoryPredicates2.toArray(new Predicate[0]));

				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), kw7);

				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate3 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(keywordPredicate, categoryPredicate2, keywordPredicate, keywordPredicate3);
			}
		};
	}

	public Page<Accommodation> getList60(int page, String kw2, List<String> kw6, int kw7, String kw8, String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search60(kw2, kw6, kw7, kw8, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search60(String kw2, List<String> kw6, int kw7, String kw8, String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw6) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_rating"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), kw7);

				int kw8Value = Integer.parseInt(kw8);
				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate3 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);
				Predicate keywordPredicate4 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(keywordPredicate, categoryPredicate, keywordPredicate2, keywordPredicate3,
						keywordPredicate4);
			}
		};
	}

	public Page<Accommodation> getList61(int page, String kw2, List<String> kw5, List<String> kw6, int kw7, String kw8,
			String kw9) {
		Pageable pageable = PageRequest.of(page, 10);
		Specification<Accommodation> spec = search61(kw2, kw5, kw6, kw7, kw8, kw9);
		return accommodationRepository.findAll(spec, pageable);
	}

	private Specification<Accommodation> search61(String kw2, List<String> kw5, List<String> kw6, int kw7, String kw8,
			String kw9) {
		return new Specification<Accommodation>() {
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				query.distinct(true);

				Predicate keywordPredicate = criteriaBuilder.like(root.get("acc_address"), "%" + kw2 + "%");

				List<Predicate> categoryPredicates = new ArrayList<>();
				for (String category : kw5) {
					categoryPredicates.add(criteriaBuilder.equal(root.get("acc_sectors"), category));
				}
				Predicate categoryPredicate = criteriaBuilder.or(categoryPredicates.toArray(new Predicate[0]));

				List<Predicate> categoryPredicates2 = new ArrayList<>();
				for (String category : kw6) {
					categoryPredicates2.add(criteriaBuilder.equal(root.get("acc_rating"), category));
				}
				Predicate categoryPredicate2 = criteriaBuilder.or(categoryPredicates2.toArray(new Predicate[0]));

				Predicate keywordPredicate2 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_max_people"), kw7);

				int kw8Value = Integer.parseInt(kw8);
				int kw9Value = Integer.parseInt(kw9);

				Predicate keywordPredicate3 = criteriaBuilder.greaterThanOrEqualTo(root.get("acc_averPrice"), kw8Value);
				Predicate keywordPredicate4 = criteriaBuilder.lessThanOrEqualTo(root.get("acc_averPrice"), kw9Value);

				return criteriaBuilder.and(keywordPredicate, categoryPredicate, categoryPredicate2, keywordPredicate2,
						keywordPredicate3, keywordPredicate4);
			}
		};
	}

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//도겸이가 추가한 메서드
	public Accommodation getAccomList(Long tempAccomId, List<BookingVO> filteredRooms) {
		Accommodation acc = new Accommodation();
		Product pro = new Product();
		// int count = 0;
		Long id = tempAccomId;
		Optional<Accommodation> accom = this.accommodationRepository.findById(id); // optional은 findById()만 가능한듯.
		if (accom.isPresent()) {
			// for(Accommodation accom: accomList) { // 숙소리스트에서 숙소 뽑아옴
			for (Product product : accom.get().getProducts()) { // 숙소에 있는 객실리스트에서 객실 뽑아옴
				int productCount = product.getProduct_count(); // 객실의 종류는 여러개고 각 객실마다 객실 수가 다르다. A객실의 카운트를 가져온다.
				// 만일 객실아이디가 여러개면 이 자리에 product.getProduct_id로 foreach문 돌린다. -> 여러개다.
				for (BookingVO reservedRoom : filteredRooms) { //
					if (product.getProduct_id() == reservedRoom.getTempRoomId()) {
						productCount -= 1; // int형이면 0이 담기겠지 null이 아니라?
					}
				}
				product.setProduct_count(productCount);
			}
			// }ㄴ

			return accom.get();
//  	return Optional.of(accommodationRepository.findAll());
		} else {
			throw new DataNotFoundException("accomList not found");
		}
	}

	public List<Accommodation> my_acc_list() {
		return this.accommodationRepository.findAll();

	}
}