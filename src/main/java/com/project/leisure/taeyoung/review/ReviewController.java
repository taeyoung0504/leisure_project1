package com.project.leisure.taeyoung.review;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.leisure.yuri.product.Accommodation;
import com.project.leisure.yuri.product.AccommodationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ReviewController {

	private final ReviewService reviewService;
	private final DeclarationRepository declarationRepository;
	private final AccommodationService accommodationService;

	@PostMapping("/create/review/{id}")
	public String createAnswer(Model model, @PathVariable("id") Long id, @RequestParam("reviewStar") int rating,
			@RequestParam("reviewContents") String content, Principal principal) {
		Accommodation aid = this.accommodationService.getAccommodation(id);
		String name = principal.getName();
		this.reviewService.create(id, rating, content, name);
		return String.format("redirect:/tour/product/detail/%s", id);
	}
	
	
	// 누적 신고 발생시 블라인드 처리!
		@PostMapping("/user/decl")
		public String submitDeclaration(@RequestParam("reviewId") Long reviewId, @RequestParam("reason") String declaration,
		                                 @RequestParam("decl_detail") String declarationDetail, Principal principal) {
		    String reporter = principal.getName(); // Set the reporter value here (e.g., get it from Principal)

		    Declaration newDeclaration = reviewService.addDeclarationToReview(reviewId, declaration, declarationDetail,
		            reporter);
		    if (newDeclaration != null) {
		        String reviewContent = reviewService.getReviewContent(reviewId);
		        System.out.println("Review Content: " + reviewContent);
		        return "redirect:/review"; 
		    } else {
		        return "redirect:/error"; 
		    }
		}

}
