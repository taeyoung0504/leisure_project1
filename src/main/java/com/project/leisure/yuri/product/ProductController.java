
package com.project.leisure.yuri.product;

import java.security.Principal;
import java.time.LocalTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.leisure.dogyeom.booking.BookService;
import com.project.leisure.taeyoung.user.UserRole;
import com.project.leisure.taeyoung.user.UserService;
import com.project.leisure.taeyoung.user.Users;

import lombok.RequiredArgsConstructor;

@RequestMapping("/partner/product")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@Controller
public class ProductController {

	private final ProductService productService;

	private final AccommodationService accommodationService;

	private final BookService bookService;
	private final UserService userService;

	// productNewMainReg 해당 유저가 새로운 숙소를 추가
	@GetMapping("/productNewMainReg")
	public String productNewMainReg(Principal principal, @RequestParam(name = "companyName") String companyName,
			@RequestParam(name = "companyAdd") String companyAdd, @RequestParam(name = "companySec") String companySec,
			Model model) {

		model.addAttribute("companyName", companyName);
		model.addAttribute("companyAdd", companyAdd);
		model.addAttribute("companySec", companySec);
		return "pyr/productNewMainReg";
	}

	// 새로운 숙소를 생성
	@PostMapping("/productNewMainReg")
	public ResponseEntity<String> productNewMainReg(Principal principal, @RequestParam("acc_name") String acc_name,
			@RequestParam("acc_address") String acc_address, @RequestParam("acc_sectors") String acc_sectors,
			@RequestParam("acc_explain") String acc_explain, @RequestParam("acc_img") MultipartFile acc_img,
			@RequestParam("acc_max_people") int acc_max_people, @RequestParam("acc_info") String acc_info) {
		String username = principal.getName(); // 현재 로그인 한 유저 이름

		int result;

		result = this.accommodationService.addNewAccommodation(username, acc_name, acc_explain, acc_img, acc_sectors,
				acc_address, acc_max_people, acc_info);

		if (result == 1) {
			return ResponseEntity.ok().build(); // HTTP 200 상태 코드를 반환
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("등록에 실패하였습니다"); // 에러 메시지와 함께 HTTP 400
	}

	// 숙박업소 수정
	@GetMapping(value = "productMainReg/{id}")
	public String productMainReg(Model model, Principal principal, @PathVariable("id") Long acc_id) {

		String username = principal.getName();

		List<Users> userList = this.userService.check(username);
		boolean isAdmin = userList.stream().anyMatch(user -> user.getRole().equals(UserRole.PARTNER));
		// 만약 admin이 아닌 경우 로그인 페이지로 이동
		if (!isAdmin) {
			return "redirect:/user/login";
		}

		// 해당 숙소의 소유자인지 확인 하는 코드 생성해야함
		boolean isOwner = accommodationService.isAccommodationOwner(acc_id, username);

		if (!isOwner) {
			return "redirect:error/403.html"; // 자신의 숙소가 아닌 경우 403에러 페이지
		}

		try {
			// id를 사용하여 해당 데이터 조회
			Accommodation accommodation = accommodationService.findByAccId(acc_id);

			model.addAttribute("accommodation", accommodation); // 해당 내용 보여주기
			return "pyr/productMainReg";
		} catch (RuntimeException e) {
			// Accommodation not found 예외 처리(id로 찾았으나 해당 숙소는 없음)
			return "redirect:error/500.html";
		}

	}

	// productMainReg 해당 유저 등록된 숙소 수정하기에서 수정된 값을 받아서 update
	@PostMapping("/productMainInfo") // required = false => 해당 파라미터가 필수가 아님
	public ResponseEntity<String> productMainInfo(Principal principal, @RequestParam("acc_name") String acc_name,
			@RequestParam("acc_address") String acc_address, @RequestParam("acc_sectors") String acc_sectors,
			@RequestParam("acc_explain") String acc_explain,
			@RequestParam(value = "acc_img", required = false) MultipartFile acc_img,
			@RequestParam("acc_max_people") int acc_max_people, @RequestParam("acc_id") Long acc_id,
			@RequestParam("acc_info") String acc_info) {
		String username = principal.getName(); // 현재 로그인 한 유저 이름

		int result;

		result = this.accommodationService.addAccommodation(username, acc_name, acc_explain, acc_img, acc_sectors,
				acc_address, acc_max_people, acc_id, acc_info);

		if (result == 1) {
			return ResponseEntity.ok().body(acc_id.toString()); // Long을 String으로 변환
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("수정에 실패하였습니다");
	}

	// 등록한 객실을 조회 => 해당 pk(id)를 가져와서 해당 객실을 조회
	@GetMapping(value = "registerRoom/{id}")
	public String showRegisterRoom(Model model, @PathVariable("id") Long acc_id, Principal principal) {

		String username = principal.getName();

		List<Users> userList = this.userService.check(username);
		boolean isAdmin = userList.stream().anyMatch(user -> user.getRole().equals(UserRole.PARTNER));

		if (!isAdmin) {
			return "redirect:/user/login"; // If not an admin, redirect to /user/login
		}

		// 해당 숙소의 소유자인지 확인 하는 코드 생성해야함
		boolean isOwner = accommodationService.isAccommodationOwner(acc_id, username);

		if (!isOwner) {
			return "redirect:error/403.html"; // 자신의 숙소가 아닌 경우 403에러 페이지
		}

		// id를 사용하여 해당 데이터 조회
		Accommodation accommodation = accommodationService.findByAccId(acc_id);

		model.addAttribute("accommodation", accommodation); // 해당 내용 보여주기

		// 해당 id로 product를 조회
		List<Product> product = productService.findProductsByAccommodationId(acc_id);

		if (!product.isEmpty()) {
			model.addAttribute("productList", product);
		} else {
			model.addAttribute("message", "No products found.");
		}

		return "pyr/registerRoom";
	}

	// 객실 등록 페이지 이동 및 해당 pk조회하여 product 가져오기
	@GetMapping(value = "addproduct/{id}")
	public String setAddProduct(Model model, @PathVariable("id") Long acc_id, Principal principal) {

		String username = principal.getName();

		List<Users> userList = this.userService.check(username);
		boolean isAdmin = userList.stream().anyMatch(user -> user.getRole().equals(UserRole.PARTNER));

		if (!isAdmin) {
			return "redirect:/user/login"; // 다시 로그인 페이지로 리다이렉트
		}

		// 해당 숙소의 소유자인지 확인 하는 코드 생성
		boolean isOwner = accommodationService.isAccommodationOwner(acc_id, username);

		if (!isOwner) {
			return "redirect:error/403.html"; // 자신의 숙소가 아닌 경우 403에러 페이지
		}

		// 해당 id로 product를 조회
		List<Product> product = productService.findProductsByAccommodationId(acc_id);

		if (!product.isEmpty()) {
			model.addAttribute("productList", product);
			model.addAttribute("acc_id", acc_id);
		} else {
			model.addAttribute("message", "No products found.");
			model.addAttribute("acc_id", acc_id);
		}
		return "pyr/add_product";

	}

	// 숙소를 등록한다. 객실 등록 시 해당 숙소의 pk도 가져온다
	@PostMapping("/addproduct")
	public ResponseEntity<String> addProduct(@RequestParam("product_photo") MultipartFile[] product_photo,
			@RequestParam("product_type") String product_type, @RequestParam("product_detail") String product_detail,
			@RequestParam("product_count") Integer product_count,
			@RequestParam("product_amount") Integer product_amount,
			@RequestParam("product_pernum") Integer product_pernum,
			@RequestParam("product_checkin") LocalTime product_checkin,
			@RequestParam("product_checkout") LocalTime product_checkout, @RequestParam("acc_id") Long acc_id)

	{

		// id를 사용하여 해당 acc를 조회
		Accommodation accommodation = accommodationService.findByAccId(acc_id);

		// 방 등록시 인원수가 만약 숙소 최대 인원수 보다 많을 경우에 대한 조건 검사
		if (accommodation.getAcc_max_people() < product_pernum) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("숙소 최대 인원수보다 많습니다");
		}

		// 상품 등록 처리
		long product_id = this.productService.pdCreate(product_type, product_detail, product_count, product_amount,
				product_pernum, product_checkin, product_checkout, accommodation);

		// 평균 가격 계산
		int averagePrice = calculateAveragePrice(accommodation);

		// 금액을 서비스에 저장
		this.accommodationService.saveAverPrice(accommodation, averagePrice);

		// for 문으로 여려개의 이미지를 상품PK와 연결해서 저장
		for (MultipartFile product_photos : product_photo) {
			this.productService.updateImg(product_photos, product_id);
		}

		// 등록된 상품 조회 하여 prouct 저장
		Product product = this.productService.getProduct(product_id);

		// 성공하면 해당 product_id값을 보냄 추후 비동기로 상품 추가 보여준다.
		return ResponseEntity.ok().build(); // HTTP 200 상태 코드를 반환

	}

	// 해당 숙소를 삭제
	@DeleteMapping(value = "deleteAcc/{id}")
	public ResponseEntity<String> deleteAcc(@PathVariable("id") Long acc_id, Principal principal) {
		try {
			// 현재 로그인한 사용자의 이름
			String username = principal.getName();

			// 해당 숙소를 소유한 사용자인지 확인
			Accommodation accommodation = accommodationService.findByAccId(acc_id);
			if (accommodation == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 숙소를 찾을 수 없습니다");
			}
			if (!accommodation.getUsername().equals(username)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 숙소의 소유자만 삭제할 수 있습니다");
			}

			// 해당 숙소에 연결된 상품들 조회
			List<Product> products = productService.findProductsByAccommodationId(acc_id);

			// 숙소 연결된 이미지 삭제 해당 숙소둘의 Pk를 가져온다
			// 각 상품에 연결된 이미지들 삭제 및 상품 삭제
			for (Product product : products) {
				this.bookService.updateAllBookingVoProductToNull(product);
				productService.pdDelete(product.getProduct_id());
			}
			// 숙소 삭제
			accommodationService.deleteAcc(acc_id);
		} catch (Exception e) {

			// 예외가 발생한 경우, 에러 메시지와 함께 HTTP 500 상태 코드를 반환
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패하였습니다");
		}
		return ResponseEntity.ok().build(); // HTTP 200 상태 코드를 반환
	}

	// 버튼을 누르면 숙소의 상품을 삭제한다
	@PostMapping("/deleteProduct")
	public ResponseEntity<?> deleateProduct(@RequestParam("productId") Long product_id, Principal principal) {
		try {
			// id를 사용하여 해당 상품을 조회
			Product product = productService.getProduct(product_id);

			// product가 null인 경우
			if (product == null) {
				return new ResponseEntity<>("상품을 찾을 수 없습니다", HttpStatus.NOT_FOUND);
			}

			// 상품의 소유자 검증

			String username = principal.getName();
			Accommodation accommodation = product.getAccommodation();
			boolean isOwner = accommodationService.isAccommodationOwner(accommodation.getId(), username);

			// 만일 자신의 숙소의 상품이 아닌 경우
			if (!isOwner) {
				return new ResponseEntity<>("삭제 불가능 합니다", HttpStatus.FORBIDDEN);
			}

			// 해당하는 product를 book 에 전송 => book 에서는 그 정보를 토대로 null로 변경
			this.bookService.updateBookingVoProductToNull(product);

			int deleteStatus = this.productService.pdDelete(product_id);
			if (deleteStatus == 0) { // 상품 삭제에 실패한 경우 0을 반환
				return new ResponseEntity<>("상품 삭제에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
			}

			// 평균 가격 계산
			int averagePrice = calculateAveragePrice(accommodation);

			// 금액을 서비스에 저장
			this.accommodationService.saveAverPrice(accommodation, averagePrice);

			return ResponseEntity.ok("삭제되었습니다");
		} catch (Exception e) { // 서버 내부 오류가 발생했을 때
			return new ResponseEntity<>("예상치 못한 오류가 발생하였습니다", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 수정된 상품의 값, 이미지를 받아와서 적용
	@PostMapping("/updateProduct")
	public ResponseEntity<?> uploadFiles(@RequestParam("productId") Long product_id,
			@RequestParam("detail") String detail,
			@RequestParam(value = "newImages[]", required = false) MultipartFile[] editedImages,
			@RequestParam(value = "deletedImages[]", required = false) List<Long> deletedImageIds,
			@RequestParam("count") Integer count, @RequestParam("type") String type,
			@RequestParam("pernum") Integer pernum, @RequestParam("amount") Integer amount,
			@RequestParam("checkin") LocalTime checkin, @RequestParam("checkout") LocalTime checkout,
			Principal principal) {

		// 현재 로그인한 사용자의 이름
		String username = principal.getName();
		// 상품 조회 해당하는 상픔의 pk를 가져와서 조회한다.
		Product product = this.productService.getProduct(product_id);

		// product가 null 인 경우
		if (product == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("접근 불가능합니다");
		}

		boolean isOwner = productService.isOwnerOfAccommodationProduct(username, product);

		if (!isOwner) {
			// 자신의 숙소가 아닌 경우 403에러 페이지
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 불가능합니다");
		}

		// 방 수정시 인원수가 만약 숙소 최대 인원수 보다 많을 경우에 대한 조건 검사
		if (product.getAccommodation().getAcc_max_people() < pernum) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("숙소 최대 인원수보다 많습니다");
		}

		// 개별 이미지 삭제 (해당 이미지의 pk를 가져와서 삭제한다)
		if (deletedImageIds != null && !deletedImageIds.isEmpty()) {
			this.productService.pdEditImgDelete(deletedImageIds);
		}

		// 들어온 이미지 추가 for 문으로 여러 개의 이미지를 상품 PK와 연결해서 저장
		if (editedImages != null && editedImages.length > 0) {
			for (MultipartFile productPhoto : editedImages) {
				if (!productPhoto.isEmpty()) {
					this.productService.updateImg(productPhoto, product_id);
				}
			}
		}

		// 수정된 것을 기준으로 다시 업데이트
		this.productService.updateProduct(product_id, type, detail, count, amount, pernum, checkin, checkout);

		// 금액 평균 구함
		// 해당하는 상품의 accommodation
		Accommodation accommodation = product.getAccommodation();

		// 평균 가격 계산
		int averagePrice = calculateAveragePrice(accommodation);

		// 금액을 서비스에 저장
		this.accommodationService.saveAverPrice(accommodation, averagePrice);

		return ResponseEntity.ok(accommodation.getId()); // HTTP 200 상태 코드를 반환하고 accommodation.getId()를 본문으로 포함
	}

	// 평균 계산 함수
	private int calculateAveragePrice(Accommodation accommodation) {
		int totalPrice = 0;
		int accProductCount = accommodation.getProducts().size();

		for (Product p : accommodation.getProducts()) {
			totalPrice += p.getProduct_amount();
		}

		if (accProductCount > 0) {
			return (totalPrice / accProductCount);
		} else {
			return 0;
		}
	}

//방 수정하기위해 해당 방 ID를 가져오고 또한 해당 방을 조회해서 찾음
	// @PostMapping(value = "modifyRoom")
	@GetMapping(value = "modifyRoom/{productId}")
	public String modifyRoom(Model model, @PathVariable("productId") Long productId, Principal principal) {

		try {
			String username = principal.getName();

			List<Users> userList = this.userService.check(username);
			boolean isAdmin = userList.stream().anyMatch(user -> user.getRole().equals(UserRole.PARTNER));

			if (!isAdmin) {
				return "redirect:/user/login"; // If not an admin, redirect to /user/login
			}

			Product product = this.productService.getProduct(productId);

			// 만약 자신의 숙소 상품이 아닌 경우
			boolean isOwner = productService.isOwnerOfAccommodationProduct(username, product);

			if (!isOwner) {
				return "redirect:error/403.html"; // 자신의 숙소가 아닌 경우 403에러 페이지
			}

			model.addAttribute("product", product);
			return "pyr/modifyRoom";

		} catch (Exception e) {
			// 해당 숙소의 방의 id가 없는 경우 500 페이지 이동
			return "redirect:error/500.html";
		}
	}

}