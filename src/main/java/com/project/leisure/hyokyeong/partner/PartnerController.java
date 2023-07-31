package com.project.leisure.hyokyeong.partner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.leisure.hyokyeong.user.UserListService;
import com.project.leisure.taeyoung.user.RegPartner;
import com.project.leisure.taeyoung.user.RegRepository;
import com.project.leisure.taeyoung.user.RegService;
import com.project.leisure.taeyoung.user.UserRepository;
import com.project.leisure.yuri.product.Accommodation;
import com.project.leisure.yuri.product.AccommodationService;
import com.project.leisure.yuri.product.Product;
import com.project.leisure.yuri.product.ProductService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class PartnerController {

	private final PartnerListService partnerListService;
	private final RegService regService;
	private final RegRepository regRepository;
	private final HttpServletRequest request;
	private final UserListService userListService;
	private final UserRepository userRepository;
	private final AccomdationListService accomdationListService;
	private final ProductService productService;
	private final AccommodationService accommodationService;

	public PartnerController(PartnerListService partnerListService, RegService regService, RegRepository regRepository,
			UserListService userListService, HttpServletRequest request, UserRepository userRepository,
			AccomdationListService accomdationListService, ProductService productService,
			AccommodationService accommodationService) {
		this.partnerListService = partnerListService;
		this.regService = regService;
		this.regRepository = regRepository;
		this.request = request;
		this.userListService = userListService;
		this.userRepository = userRepository;
		this.accomdationListService = accomdationListService;
		this.productService = productService;
		this.accommodationService = accommodationService;
	}

	@GetMapping("/partnerListPage")
	public String getPartnerList(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw", defaultValue = "") String kw) {
		Page<RegPartner> partnerPage = this.partnerListService.getPartnerRegPage(page, kw);
		List<String> fileList = this.partnerListService.getFileList();
		model.addAttribute("paging", partnerPage);
		model.addAttribute("fileList", fileList);
		model.addAttribute("kw", kw);
		model.addAttribute("totalPages", partnerPage.getTotalPages());
		partnerListService.partnerList(model);
		model.addAttribute("partners", partnerPage.getContent());
		// 전체 객체 수를 전달
		model.addAttribute("objectCount", partnerPage.getTotalElements());

		return "khk/partnerListPage";
	}

	// 등록 업체 리스트
	@GetMapping("/accomdationListPage")
	public String getAccomdationList(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw", defaultValue = "") String kw) {
		Page<Accommodation> accomdationListPage = this.accomdationListService.getAccommodationList(page, kw);
		model.addAttribute("paging", accomdationListPage);
		model.addAttribute("kw", kw);
		model.addAttribute("totalPages", accomdationListPage.getTotalPages());
		accomdationListService.accommodationList(model);
		model.addAttribute("accomdations", accomdationListPage.getContent());
		model.addAttribute("objectCount", accomdationListPage.getTotalElements());

		return "khk/accomdationListPage";
	}

	@GetMapping("/showFile")
	public ResponseEntity<Resource> showFile(@RequestParam("filename") String filename, HttpServletRequest request)
			throws IOException {
		String fileUrl = partnerListService.getFileUrlFromDatabase(filename);
		if (fileUrl != null) {
			String baseUrl = partnerListService.getBaseUrl(request);
			String fullUrl = baseUrl + fileUrl;

			Resource resource = new UrlResource(fullUrl);
			if (resource.exists() && resource.isReadable()) {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.IMAGE_JPEG);
				return new ResponseEntity<>(resource, headers, HttpStatus.OK);
			}
		}
		throw new FileNotFoundException("파일을 찾을 수 없습니다.");
	}

	@PostMapping("/approve/{id}")
	public String handleApproval(@PathVariable("id") Long id, @RequestBody Map<String, Integer> payload,
			HttpServletRequest request) {
		int status = payload.get("status");
		partnerListService.handleApproval(id, status);

		// 이전 페이지 URL 가져오기
		String referer = request.getHeader("Referer");

		// 이전 페이지로 리다이렉트
		return "redirect:" + referer;
	}

//	// 해당 숙소를 삭제
//		@DeleteMapping(value = "deleteAcc/{id}")
//		public ResponseEntity<?> deleteAcc(@PathVariable("id") Long acc_id) {
//
//			// 해당 숙소에 연결된 상품들 조회
//			List<Product> products = productService.findProductsByAccommodationId(acc_id);
//
//			// 숙소 연결된 이미지 삭제 해당 숙소둘의 Pk를 가져온다
//
//			// 각 상품에 연결된 이미지들 삭제 및 상품 삭제
//			for (Product product : products) {
//				productService.pdDelete(product.getProduct_id());
//			}
//
//			// 숙소 삭제
//			accommodationService.deleteAcc(acc_id);
//
//			return ResponseEntity.ok().build(); // HTTP 200 상태 코드를 반환
//		}

	// 해당 숙소를 삭제
	@DeleteMapping(value = "deleteAcc/{accId}")
	public ResponseEntity<?> deleteAcc(@PathVariable("accId") Long acc_id) {
		try {

			// 해당 숙소에 연결된 상품들 조회
			List<Product> products = productService.findProductsByAccommodationId(acc_id);

			// 각 상품에 연결된 이미지들 삭제 및 상품 삭제
			for (Product product : products) {
				productService.pdDelete(product.getProduct_id());
			}

			// 숙소 삭제
			accommodationService.deleteAcc(acc_id);

			return ResponseEntity.ok().build(); // HTTP 200 상태 코드를 반환
		} catch (RuntimeException e) {
			// 예외 발생 시, 적절한 오류 상태 코드를 반환
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Accommodation not found");
		}
	}
}