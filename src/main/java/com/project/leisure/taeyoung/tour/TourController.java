package com.project.leisure.taeyoung.tour;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import com.project.leisure.dogyeom.booking.BookingVO;
import com.project.leisure.dogyeom.booking.reserveList.ReserveService;
import com.project.leisure.taeyoung.public_food.Food;
import com.project.leisure.taeyoung.public_food.FoodService;
import com.project.leisure.taeyoung.review.DeclarationRepository;
import com.project.leisure.taeyoung.review.Review;
import com.project.leisure.taeyoung.review.ReviewService;
import com.project.leisure.yuri.product.Accommodation;
import com.project.leisure.yuri.product.AccommodationService;
import com.project.leisure.yuri.product.Product;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/tour")
public class TourController {

	private final FoodService foodService;
	private final AccommodationService accommodationService;
	private final ReviewService reviewService;
	private final ReserveService reserveService;
	private final DeclarationRepository declarationRepository;
	
	@Autowired
	public TourController(FoodService foodService,AccommodationService accommodationService
			,ReviewService reviewService, ReserveService reserveService, DeclarationRepository declarationRepository) {
		this.foodService = foodService;
		this.accommodationService = accommodationService;
		this.reviewService = reviewService;
		this.reserveService = reserveService;
		this.declarationRepository = declarationRepository;
	}

	@GetMapping("/daegu_travel")
	public String address2() {
		// System.out.println("카카오 API 테스트");
		return "kty/daegu_tour10";
	}

	/* 아래 맛집 데이터 뽑는 과정 */

	@GetMapping("/food")
	public String food() {
		return "kty/food";
	}

	@PostMapping("/saveFood")
	@ResponseBody
	public ResponseEntity<String> saveFood(@RequestBody Food food) {
		try {
			foodService.saveFood(food);
			return ResponseEntity.ok("Food saved successfully");
		} catch (Exception e) {
			e.printStackTrace(); // Print the exception details to the console for debugging
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving food: " + e.getMessage());
		}
	}

	/* 키워드 검색 ㄴ*/
	@GetMapping({ "/daegu_food", "/daegu_food2", "/daegu_food3", "/daegu_food4", "/daegu_food5" })
	public String foodTest(Model model, 
	                      @RequestParam(value = "page", defaultValue = "0") int page,
	                      @RequestParam(value = "kw", defaultValue = "") String kw,
	                      @RequestParam(value = "kw2", defaultValue = "") String kw2,
	                      @RequestParam(value = "kw3", defaultValue = "") List<String> kw3,
	                      @RequestParam(value = "kw4", defaultValue = "") String kw4) {
	    Page<Food> paging;
	    String viewName;
	    
	    if (StringUtils.isEmpty(kw2)) {
	        paging = this.foodService.getList2(page, kw);
	        viewName = "kty/find_food/food_test2";
	    } else if (!kw3.isEmpty() && !StringUtils.isEmpty(kw4)) {
	        paging = this.foodService.getList6(page, kw2, kw3, kw4);
	        viewName = "kty/find_food/food_test6";
	    } else if (!StringUtils.isEmpty(kw4)) {
	        paging = this.foodService.getList5(page, kw2, kw4);
	        viewName = "kty/find_food/food_test5";
	    } else if (!kw3.isEmpty()) {
	        paging = this.foodService.getList3(page, kw2, kw3);
	        viewName = "kty/find_food/food_test3";
	    } else {
	        paging = this.foodService.getList4(page, kw2);
	        viewName = "kty/find_food/food_test4";
	    }
	    
	    model.addAttribute("paging", paging);
	    model.addAttribute("foodList", paging.getContent());
	    model.addAttribute("kw", kw);
	    model.addAttribute("kw2", kw2);
	    model.addAttribute("kw3", kw3);
	    model.addAttribute("kw4", kw4);
	    
	    return viewName;
	}
		
		// 예약불가 객실 조회 하여
		// 예약가능 객실 조회
		@GetMapping(value = "/product/detail/{id}")
		  public String detail(Model model, @PathVariable("id") Long id, 
				  @RequestParam(value="page", defaultValue="0") int page,
				  @RequestParam(name = "checkin", required = false) String checkin,
					@RequestParam(name = "checkOut", required = false) String checkOut) {
			// 넘어온 아이다와 현재의 날짜로 사용자가 아무 날짜도 입력하지 않고 상세페이지에 들어왔을 때
			// 보여질 객실 목록 - 현재 날짜를 기준으로 체크인, 아웃을 비교하여 예약 가능한 객실만 보이기
			Long tempAccomId = id;
			LocalDate currentDate = LocalDate.now();
	        String reqDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	        LocalDate nextDate = currentDate.plusDays(1);
	        String reqNextDate = nextDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	        System.out.println("현재 날짜: " + reqDate);
	        System.out.println("다음 날짜: " + reqNextDate);
	        
	        LocalDate localDate = null;
	        LocalDate localDate2 = null;
	        
	        int num = 0;

	        if (checkin != null && checkOut != null) {
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	            localDate = LocalDate.parse(checkin, formatter);
	            localDate2 = LocalDate.parse(checkOut, formatter);
	         // 두 날짜 사이의 차이를 구함
	            long difference = ChronoUnit.DAYS.between(localDate, localDate2);

	            // int 타입의 변수에 저장
	            num = (int) difference;

	            System.out.println("두 날짜 사이의 차이는 " + num + "일 입니다.");	
	        } else {
	            // 기본값 설정 또는 null 처리
	            // 예시: 현재 날짜와 다음 날짜로 기본값 설정
	            LocalDate currentDate1 = LocalDate.now();
	            localDate = currentDate1;
	            localDate2 = currentDate1.plusDays(1);
	            
	         // 두 날짜 사이의 차이를 구함
	            long difference = ChronoUnit.DAYS.between(localDate, localDate2);

	            // int 타입의 변수에 저장
	            num = (int) difference;

	        }

	        System.out.println(localDate);
	        System.out.println(localDate2);
	        
	        List<BookingVO> reservedRoomList;
	        
	        if(checkin != null && checkOut != null) {
	        	reservedRoomList = reserveService.getReservedRoomList(tempAccomId, localDate, localDate2);
	        }else {
	        	reservedRoomList = reserveService.getReservedRoomList(tempAccomId, currentDate, nextDate);
	        }

	        Page<Review> paging = reviewService.getList(page);
		     model.addAttribute("paging", paging);
	        //model.addAttribute("reservedRoomList", reservedRoomList);
	        if(reservedRoomList != null) {
	        	Accommodation accom = accommodationService.getAccomList(tempAccomId, reservedRoomList);
	        	if(accom != null) {
			            for(Product product: accom.getProducts()) {
			            	
			            	String result = String.valueOf(num * product.getProduct_amount());
			            	System.out.println("!@!@!@!@!@!@!@!@!@!@!@!@!@!@!@ result : " + result);
			            	product.setTotalPrice(result);
			        }
			        model.addAttribute("acc", accom);
	        		return "pyr/product_detail";
	        	}else {
	        		model.addAttribute("message", "No products found."); // 유리 코드 -> 나중에 내가 내걸로 교체
	        	}
	        	 
	        }else {
	        	model.addAttribute("message", "No products found."); // 유리 코드 -> 나중에 내가 내걸로 교체
	        }
	        //return "pyr/product_detail"; // else니까 에러 메시지로 대체해야 할듯?
	       return null;
		}
	
	
	
	
	/* 숙소 검색 관련 컨트롤러 */
	
	
	/* 1. 숙소 리스트 게시판 이동 컨트롤러 */
	@GetMapping("/daegu_room")
	public String room1(Model model, @RequestParam(value="page", defaultValue="0") int page,
			  @RequestParam(value = "kw", defaultValue = "") String kw) {
		Page<Accommodation> paging = accommodationService.getList(page,kw);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		return "kty/find/daegu_room";
	}
	

	/* 구역검색 컨트롤러*/
	
	
	@GetMapping("/daegu_room2")
	public String room2(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "") String kw2) {
		Page<Accommodation> paging = accommodationService.getList2(page,kw2);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);

		return "kty/find/daegu_room";
	}
	
	
	@GetMapping("/daegu_room3")
	public String room3(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw5", defaultValue = "") List<String> kw5) {
		Page<Accommodation> paging = accommodationService.getList3(page,kw5);
		model.addAttribute("paging", paging);
		model.addAttribute("kw5", kw5);

		return "kty/find/daegu_room";
	}
	

	@GetMapping("/daegu_room4")
	public String room4(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw6", defaultValue = "") List<String> kw6) {
		Page<Accommodation> paging = accommodationService.getList5(page,kw6);
		model.addAttribute("paging", paging);
		model.addAttribute("kw6", kw6);

		return "kty/find/daegu_room";
	}
	
	@GetMapping("/daegu_room5")
	public String room5(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw7", defaultValue = "") int kw7) {
		Page<Accommodation> paging = accommodationService.getList4(page,kw7);
		model.addAttribute("paging", paging);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room";
	}
	
	
	/* 구역+숙소 타입+평점+투숙인원 */
	@GetMapping("/daegu_room6")
	public String room6(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "") String kw2,
			@RequestParam(value = "kw5", defaultValue = "") List<String> kw5,
			@RequestParam(value = "kw6", defaultValue = "") List<String> kw6,
			@RequestParam(value = "kw7", defaultValue = "") int kw7) {
		Page<Accommodation> paging = accommodationService.getList6(page,kw2,kw5,kw6,kw7);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw5", kw5);
		model.addAttribute("kw6", kw6);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room";
	}
	
	/* 구역 + 숙소타입 */
	@GetMapping("/daegu_room7")
	public String room7(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "") String kw2,
			@RequestParam(value = "kw5", defaultValue = "") List<String> kw5) {
		Page<Accommodation> paging = accommodationService.getList7(page,kw2,kw5);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw5", kw5);

		return "kty/find/daegu_room";
	}
	
	
	/* 구역 + 숙소타입 + 평점 s*/
	@GetMapping("/daegu_room8")
	public String room8(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "") String kw2,
			@RequestParam(value = "kw5", defaultValue = "") List<String> kw5,
			@RequestParam(value = "kw6", defaultValue = "") List<String> kw6) {
		Page<Accommodation> paging = accommodationService.getList8(page,kw2,kw5,kw6);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw5", kw5);
		model.addAttribute("kw6", kw6);

		return "kty/find/daegu_room";
	}
	
	/* 구역 + 숙소타입 + 투숙인원 */
	
	@GetMapping("/daegu_room9")
	public String room9(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "") String kw2,
			@RequestParam(value = "kw5", defaultValue = "") List<String> kw5,
			@RequestParam(value = "kw7", defaultValue = "") int kw7) {
		Page<Accommodation> paging = accommodationService.getList9(page,kw2,kw5,kw7);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw5", kw5);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room";
	}
	
	@GetMapping({"/daegu_room10", "/daegu_room11", "/daegu_room12", "/daegu_room13", "/daegu_room14", "/daegu_room15"})
	public String handleRoomRequests2(
	        Model model,
	        @RequestParam(value = "page", defaultValue = "0") int page,
	        @RequestParam(value = "kw2", defaultValue = "") String kw2,
	        @RequestParam(value = "kw5", defaultValue = "") List<String> kw5,
	        @RequestParam(value = "kw6", defaultValue = "") List<String> kw6,
	        @RequestParam(value = "kw7", defaultValue = "0") int kw7,
	        @RequestParam(value = "kw8", defaultValue = "") String kw8,
	        @RequestParam(value = "kw9", defaultValue = "") String kw9,
	        HttpServletRequest request) {

	    String path = request.getRequestURI();
	    Page<Accommodation> paging = null;

	    if (path.contains("daegu_room10")) {
	        paging = accommodationService.getList10(page, kw2, kw6);
	        model.addAttribute("kw2", kw2);
	        model.addAttribute("kw6", kw6);
	    } else if (path.contains("daegu_room11")) {
	        paging = accommodationService.getList11(page, kw2, kw6, kw7);
	        model.addAttribute("kw2", kw2);
	        model.addAttribute("kw6", kw6);
	        model.addAttribute("kw7", kw7);
	    } else if (path.contains("daegu_room12")) {
	        paging = accommodationService.getList12(page, kw2, kw7);
	        model.addAttribute("kw2", kw2);
	        model.addAttribute("kw7", kw7);
	    } else if (path.contains("daegu_room13")) {
	        paging = accommodationService.getList13(page, kw5, kw6);
	        model.addAttribute("kw5", kw5);
	        model.addAttribute("kw6", kw6);
	    } else if (path.contains("daegu_room14")) {
	        paging = accommodationService.getList14(page, kw5, kw6, kw7);
	        model.addAttribute("kw5", kw5);
	        model.addAttribute("kw6", kw6);
	        model.addAttribute("kw7", kw7);
	    } else if (path.contains("daegu_room15")) {
	        paging = accommodationService.getList15(page, kw6, kw7);
	        model.addAttribute("kw6", kw6);
	        model.addAttribute("kw7", kw7);
	    }

	    model.addAttribute("paging", paging);

	    return "kty/find/daegu_room2";
	}
	
	
	/* 최소 금액 */
	@GetMapping({
	    "/daegu_room16", "/daegu_room17", "/daegu_room18", "/daegu_room19", "/daegu_room20",
	    "/daegu_room21", "/daegu_room22", "/daegu_room23", "/daegu_room24", "/daegu_room25",
	    "/daegu_room26", "/daegu_room27", "/daegu_room28", "/daegu_room29", "/daegu_room30",
	    "/daegu_room31", "/daegu_room32", "/daegu_room33", "/daegu_room34", "/daegu_room35",
	    "/daegu_room36", "/daegu_room37", "/daegu_room38", "/daegu_room39", "/daegu_room40",
	    "/daegu_room41", "/daegu_room42", "/daegu_room43", "/daegu_room44", "/daegu_room45",
	    "/daegu_room46","/daegu_room47","/daegu_room48","/daegu_room49","/daegu_room50",
	    "/daegu_room51","/daegu_room52","/daegu_room53","/daegu_room54","/daegu_room55",
	    "/daegu_room56","/daegu_room57","/daegu_room58","/daegu_room59","/daegu_room60",
	    "/daegu_room61"
	})
	public String handleRoomRequests(
	        Model model,
	        @RequestParam(value = "page", defaultValue = "0") int page,
	        @RequestParam(value = "kw2", defaultValue = "") String kw2,
	        @RequestParam(value = "kw5", defaultValue = "") List<String> kw5,
	        @RequestParam(value = "kw6", defaultValue = "") List<String> kw6,
	        @RequestParam(value = "kw7", defaultValue = "0") int kw7,
	        @RequestParam(value = "kw8", defaultValue = "") String kw8,
	        @RequestParam(value = "kw9", defaultValue = "") String kw9,
	        HttpServletRequest request) {

	    String path = request.getRequestURI();
	    Page<Accommodation> paging = null;

	    if (path.contains("daegu_room16")) {
	        paging = accommodationService.getList16(page, kw8);
	        model.addAttribute("kw8", kw8);
	    } else if (path.contains("daegu_room17")) {
	        paging = accommodationService.getList17(page, kw9);
	        model.addAttribute("kw9", kw9);
	    } else if (path.contains("daegu_room18")) {
	        paging = accommodationService.getList18(page, kw8, kw9);
	        model.addAttribute("kw8", kw8);
	        model.addAttribute("kw9", kw9);
	    } else if (path.contains("daegu_room19")) {
	        paging = accommodationService.getList19(page, kw2, kw8);
	        model.addAttribute("kw2", kw2);
	        model.addAttribute("kw8", kw8);
	    } else if (path.contains("daegu_room20")) {
	        paging = accommodationService.getList20(page, kw2, kw9);
	        model.addAttribute("kw2", kw2);
	        model.addAttribute("kw9", kw9);
	    } else if (path.contains("daegu_room21")) {
	        paging = accommodationService.getList21(page, kw2, kw8, kw9);
	        model.addAttribute("kw2", kw2);
	        model.addAttribute("kw8", kw8);
	        model.addAttribute("kw9", kw9);
	    } else if (path.contains("daegu_room22")) {
	        paging = accommodationService.getList22(page, kw5, kw8);
	        model.addAttribute("kw5", kw5);
	        model.addAttribute("kw8", kw8);
	    } else if (path.contains("daegu_room23")) {
	        paging = accommodationService.getList23(page, kw5, kw9);
	        model.addAttribute("kw5", kw5);
	        model.addAttribute("kw9", kw9);
	    } else if (path.contains("daegu_room24")) {
	        paging = accommodationService.getList24(page, kw5, kw8, kw9);
	        model.addAttribute("kw5", kw5);
	        model.addAttribute("kw8", kw8);
	        model.addAttribute("kw9", kw9);
	    } else if (path.contains("daegu_room25")) {
	        paging = accommodationService.getList25(page, kw2, kw5, kw8);
	        model.addAttribute("kw2", kw2);
	        model.addAttribute("kw5", kw5);
	        model.addAttribute("kw8", kw8);
	    } else if (path.contains("daegu_room26")) {
	        paging = accommodationService.getList26(page, kw2, kw5, kw9);
	        model.addAttribute("kw2", kw2);
	        model.addAttribute("kw5", kw5);
	        model.addAttribute("kw9", kw9);
	    } else if (path.contains("daegu_room27")) {
	        paging = accommodationService.getList27(page, kw2, kw5, kw8, kw9);
	        model.addAttribute("kw2", kw2);
	        model.addAttribute("kw5", kw5);
	        model.addAttribute("kw8", kw8);
	        model.addAttribute("kw9", kw9);
	    } else if (path.contains("daegu_room28")) {
	        paging = accommodationService.getList28(page, kw6, kw8);
	        model.addAttribute("kw6", kw6);
	        model.addAttribute("kw8", kw8);
	    } else if (path.contains("daegu_room29")) {
	        paging = accommodationService.getList29(page, kw6, kw9);
	        model.addAttribute("kw6", kw6);
	        model.addAttribute("kw9", kw9);
	    } else if (path.contains("daegu_room30")) {
	        paging = accommodationService.getList30(page, kw6, kw8, kw9);
	        model.addAttribute("kw6", kw6);
	        model.addAttribute("kw8", kw8);
	        model.addAttribute("kw9", kw9);
	    } else if (path.contains("daegu_room31")) {
	        paging = accommodationService.getList31(page, kw2, kw5, kw6, kw8);
	        model.addAttribute("kw2", kw2);
	        model.addAttribute("kw5", kw5);
	        model.addAttribute("kw8", kw8);
	        model.addAttribute("kw6", kw6);
	    } else if (path.contains("daegu_room32")) {
	        paging = accommodationService.getList32(page, kw2, kw5, kw6, kw9);
	        model.addAttribute("kw2", kw2);
	        model.addAttribute("kw5", kw5);
	        model.addAttribute("kw9", kw9);
	        model.addAttribute("kw6", kw6);
	    } else if (path.contains("daegu_room33")) {
	        paging = accommodationService.getList33(page, kw2, kw5, kw6, kw8, kw9);
	        model.addAttribute("kw2", kw2);
	        model.addAttribute("kw5", kw5);
	        model.addAttribute("kw9", kw9);
	        model.addAttribute("kw8", kw8);
	        model.addAttribute("kw6", kw6);
	    } else if (path.contains("daegu_room34")) {
	        paging = accommodationService.getList34(page, kw2, kw6, kw8);
	        model.addAttribute("kw2", kw2);
	        model.addAttribute("kw8", kw8);
	        model.addAttribute("kw6", kw6);
	    } else if (path.contains("daegu_room35")) {
	        paging = accommodationService.getList35(page, kw2, kw6, kw9);
	        model.addAttribute("kw2", kw2);
	        model.addAttribute("kw9", kw9);
	        model.addAttribute("kw6", kw6);
	    } else if (path.contains("daegu_room36")) {
	        paging = accommodationService.getList36(page, kw2, kw6, kw8, kw9);
	        model.addAttribute("kw2", kw2);
	        model.addAttribute("kw9", kw9);
	        model.addAttribute("kw8", kw8);
	        model.addAttribute("kw6", kw6);
	    } else if (path.contains("daegu_room37")) {
	        paging = accommodationService.getList37(page, kw5, kw6, kw8);
	        model.addAttribute("kw5", kw5);
	        model.addAttribute("kw8", kw8);
	        model.addAttribute("kw6", kw6);
	    } else if (path.contains("daegu_room38")) {
	        paging = accommodationService.getList38(page, kw5, kw6, kw9);
	        model.addAttribute("kw5", kw5);
	        model.addAttribute("kw9", kw9);
	        model.addAttribute("kw6", kw6);
	    } else if (path.contains("daegu_room39")) {
	        paging = accommodationService.getList39(page, kw5, kw6, kw8, kw9);
	        model.addAttribute("kw5", kw5);
	        model.addAttribute("kw9", kw9);
	        model.addAttribute("kw8", kw8);
	        model.addAttribute("kw6", kw6);
	    } else if (path.contains("daegu_room40")) {
	        paging = accommodationService.getList40(page, kw7, kw8);
	        model.addAttribute("kw7", kw7);
	        model.addAttribute("kw8", kw8);
	    } else if  (path.contains("daegu_room41")) {
	    	paging = accommodationService.getList41(page,kw7,kw9);
	    	model.addAttribute("kw9", kw9);
			model.addAttribute("kw7", kw7);
	    } else if  (path.contains("daegu_room42")) {
	    	paging = accommodationService.getList42(page,kw7,kw8,kw9);
	    	model.addAttribute("kw9", kw9);
			model.addAttribute("kw8", kw8);
			model.addAttribute("kw7", kw7);
	    }  else if  (path.contains("daegu_room43")) {
	    	paging = accommodationService.getList43(page,kw2,kw7,kw8);
	    	model.addAttribute("kw2", kw2);
			model.addAttribute("kw8", kw8);
			model.addAttribute("kw7", kw7);
	    } else if  (path.contains("daegu_room44")) {
	    	paging = accommodationService.getList44(page,kw2,kw7,kw9);
	    	model.addAttribute("kw2", kw2);
			model.addAttribute("kw9", kw9);
			model.addAttribute("kw7", kw7);
	    }  else if  (path.contains("daegu_room45")) {
	    	paging = accommodationService.getList45(page,kw2,kw7,kw8,kw9);
	    	model.addAttribute("kw2", kw2);
			model.addAttribute("kw9", kw9);
			model.addAttribute("kw8", kw8);
			model.addAttribute("kw7", kw7);
	    } else if  (path.contains("daegu_room46")) {
	    	paging = accommodationService.getList46(page,kw5,kw7,kw8);
	    	model.addAttribute("kw5", kw5);
			model.addAttribute("kw8", kw8);
			model.addAttribute("kw7", kw7);
	    } else if  (path.contains("daegu_room47")) {
	    	paging = accommodationService.getList47(page,kw5,kw7,kw9);
	    	model.addAttribute("kw5", kw5);
			model.addAttribute("kw9", kw9);
			model.addAttribute("kw7", kw7);
	    } 
	    else if  (path.contains("daegu_room48")) {
	    	paging = accommodationService.getList48(page,kw5,kw7,kw8,kw9);
	    	model.addAttribute("kw5", kw5);
			model.addAttribute("kw9", kw9);
			model.addAttribute("kw8", kw8);
			model.addAttribute("kw7", kw7);
	    } 
	    else if  (path.contains("daegu_room49")) {
	    	paging = accommodationService.getList49(page,kw6,kw7,kw8);
	    	model.addAttribute("kw6", kw6);
			model.addAttribute("kw8", kw8);
			model.addAttribute("kw7", kw7);
	    } 
	    else if  (path.contains("daegu_room50")) {
	    	paging = accommodationService.getList50(page,kw6,kw7,kw9);
	    	model.addAttribute("kw6", kw6);
			model.addAttribute("kw9", kw9);
			model.addAttribute("kw7", kw7);
	    }   else if  (path.contains("daegu_room51")) {
	    	paging = accommodationService.getList51(page,kw6,kw7,kw8,kw9);
	    	model.addAttribute("kw6", kw6);
			model.addAttribute("kw9", kw9);
			model.addAttribute("kw8", kw8);
			model.addAttribute("kw7", kw7);
	    } else if  (path.contains("daegu_room52")) {
	    	paging = accommodationService.getList52(page,kw5,kw6,kw7,kw8);
	    	model.addAttribute("kw6", kw6);
			model.addAttribute("kw5", kw5);
			model.addAttribute("kw8", kw8);
			model.addAttribute("kw7", kw7);
	    } else if  (path.contains("daegu_room53")) {
	    	paging = accommodationService.getList53(page,kw5,kw6,kw7,kw9);
	    	model.addAttribute("kw6", kw6);
			model.addAttribute("kw5", kw5);
			model.addAttribute("kw9", kw9);
			model.addAttribute("kw7", kw7);
	    } else if  (path.contains("daegu_room54")) {
	    	paging = accommodationService.getList54(page,kw5,kw6,kw7,kw8,kw9);
	    	model.addAttribute("kw6", kw6);
			model.addAttribute("kw5", kw5);
			model.addAttribute("kw9", kw9);
			model.addAttribute("kw8", kw8);
			model.addAttribute("kw7", kw7);
	    } else if  (path.contains("daegu_room55")) {
	    	paging = accommodationService.getList55(page,kw2,kw5,kw7,kw8);
	    	model.addAttribute("kw2", kw2);
			model.addAttribute("kw5", kw5);
			model.addAttribute("kw8", kw8);
			model.addAttribute("kw7", kw7);
	    } else if  (path.contains("daegu_room56")) {
	    	paging = accommodationService.getList56(page,kw2,kw5,kw7,kw9);
	    	model.addAttribute("kw2", kw2);
			model.addAttribute("kw5", kw5);
			model.addAttribute("kw9", kw9);
			model.addAttribute("kw7", kw7);
	    } else if  (path.contains("daegu_room57")) {
	    	paging = accommodationService.getList57(page,kw2,kw5,kw7,kw8,kw9);
	    	model.addAttribute("kw2", kw2);
			model.addAttribute("kw5", kw5);
			model.addAttribute("kw8", kw8);
			model.addAttribute("kw9", kw9);
			model.addAttribute("kw7", kw7);
	    } else if  (path.contains("daegu_room58")) {
	    	paging = accommodationService.getList58(page,kw2,kw6,kw7,kw8);
	    	model.addAttribute("kw2", kw2);
			model.addAttribute("kw6", kw6);
			model.addAttribute("kw8", kw8);
			model.addAttribute("kw7", kw7);

	    } else if  (path.contains("daegu_room59")) {
	    	paging = accommodationService.getList59(page,kw2,kw6,kw7,kw9);
	    	model.addAttribute("kw2", kw2);
			model.addAttribute("kw6", kw6);
			model.addAttribute("kw9", kw9);
			model.addAttribute("kw7", kw7);

	    } else if  (path.contains("daegu_room60")) {
	    	 paging = accommodationService.getList60(page,kw2,kw6,kw7,kw8,kw9);
	    	 model.addAttribute("kw2", kw2);
	 		model.addAttribute("kw6", kw6);
	 		model.addAttribute("kw9", kw9);
	 		model.addAttribute("kw8", kw8);
	 		model.addAttribute("kw7", kw7);

	    } else if  (path.contains("daegu_room61")) {
	    	paging = accommodationService.getList61(page,kw2,kw5,kw6,kw7,kw8,kw9);
	    	 model.addAttribute("kw2", kw2);
	    	 model.addAttribute("kw2", kw2);
	    		model.addAttribute("kw5", kw5);
	    		model.addAttribute("kw6", kw6);
	    		model.addAttribute("kw9", kw9);
	    		model.addAttribute("kw8", kw8);
	    		model.addAttribute("kw7", kw7);

	    }

	    if (paging != null) {
	        model.addAttribute("paging", paging);
	    }

	    return "kty/find/daegu_room3";
	}

}

