package com.project.leisure.taeyoung.review;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.leisure.yuri.product.Accommodation;
import com.project.leisure.yuri.product.AccommodationRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final DeclarationRepository declarationRepository;
	private final AccommodationRepository accommodationRepository;

	// 상품의 리뷰 등록
	public Review create(Long id, int rating, String content, String name) {
		Optional<Accommodation> optionalAcc = accommodationRepository.findById(id);

		if (optionalAcc.isPresent()) {
			Accommodation acc = optionalAcc.get();
			Review review = new Review();
			review.setRating(rating);
			review.setContent(content);
			review.setAuthor(name);
			review.setCreate_reivew_Time(LocalDateTime.now());
			review.setAccommodation(acc); // Accommodation 객체 설정
			acc.getReviews().add(review);
			reviewRepository.save(review);

			// 리뷰 평균 계산

			int totalration = 0;

			int aver_ration = 0;

			// 2. 해당 숙소에 등록한 댓글의 수를 가져온다.
			int totalRatingCount = acc.getReviews().size();

			for (Review rev : acc.getReviews()) {

				totalration += rev.getRating();
			}

			if (totalRatingCount > 0) {
				aver_ration = (totalration / totalRatingCount);
			} else {
				aver_ration = 0;
			}

			acc.setAcc_rating(aver_ration);
			this.accommodationRepository.save(acc);

			return review;
		}
		return null;
	}

	public Page<Review> getList(int page) {
		Pageable pageable = PageRequest.of(page, 30, Sort.by("id").descending());
		return reviewRepository.findAll(pageable);
	}

	
	
	// 누적 신고 발생시 블라인드 처리+ 동일 사용자 중복 신고 불가능 기능 추가
		public Declaration addDeclarationToReview(Long reviewId, String declaration, String declarationDetail, String reporter) {
		    Optional<Review> optionalReview = reviewRepository.findById(reviewId);
		    if (optionalReview.isPresent()) {
		        Review review = optionalReview.get();

		       /*  중복신고 불가능  */
		        boolean reporterExists = review.getDeclarations().stream()
		                .anyMatch(existingDeclaration -> existingDeclaration.getRepoter().equals(reporter));
		        
		        if (reporterExists) {
		            return null;
		        }
		        
		        
		        Declaration newDeclaration = new Declaration();
		        newDeclaration.setDeclaration(declaration);
		        newDeclaration.setDeclarationDetail(declarationDetail);
		        newDeclaration.setRepoter(reporter);

		        // 양방향 관계 설정
		        newDeclaration.setReview(review);
		        review.getDeclarations().add(newDeclaration);

		        declarationRepository.save(newDeclaration);

		        // 같은 reviewId의 선언 수가 5개 이상인지 확인
		        int declarationCount = declarationRepository.countByReviewId(reviewId);
		        if (declarationCount >= 5) {
		            review.setContentBlinded(true);
		            // 리뷰의 내용을 블라인드 처리
		            review.setContent("Content is hidden");
		            reviewRepository.save(review); // 변경된 리뷰 저장
		        }

		        return newDeclaration;
		    }
		    return null;
		}

		public String getReviewContent(Long reviewId) {
		    Optional<Review> optionalReview = reviewRepository.findById(reviewId);
		    if (optionalReview.isPresent()) {
		        Review review = optionalReview.get();
		        return review.getContentForDisplay();
		    }
		    return null;
		}


}