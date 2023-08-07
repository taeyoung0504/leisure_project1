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
	@GetMapping("/daegu_food")
	public String foodSearch(Model model,
	                         @RequestParam(value = "page", defaultValue = "0") int page,
	                         @RequestParam(value = "kw", defaultValue = "") String kw,
	                         @RequestParam(value = "kw2", defaultValue = "") String kw2,
	                         @RequestParam(value = "kw3", defaultValue = "") List<String> kw3,
	                         @RequestParam(value = "kw4", defaultValue = "") String kw4) {

	    Page<Food> paging;

	    if (!kw2.isEmpty() && !kw3.isEmpty() && !kw4.isEmpty()) {
	        paging = this.foodService.getList6(page, kw2, kw3, kw4);
	        model.addAttribute("kw3", kw3);
	    } else if (!kw2.isEmpty() && !kw4.isEmpty()) {
	        paging = this.foodService.getList5(page, kw2, kw4);
	    } else if (!kw2.isEmpty() && !kw3.isEmpty()) {
	        paging = this.foodService.getList3(page, kw2, kw3);
	        model.addAttribute("kw3", kw3);
	    } else if (!kw2.isEmpty()) {
	        paging = this.foodService.getList4(page, kw2);
	    } else {
	        paging = this.foodService.getList2(page, kw);
	    }

	    model.addAttribute("paging", paging);
	    model.addAttribute("foodList", paging.getContent());
	    model.addAttribute("kw", kw);
	    model.addAttribute("kw2", kw2);
	    model.addAttribute("kw4", kw4);

	    if (kw3.isEmpty()) {
	        model.addAttribute("kw3", kw3);
	    }

	    if (kw4.isEmpty()) {
	        model.addAttribute("kw4", kw4);
	    }

	    return "kty/find_food/food_test" + (kw3.isEmpty() && kw4.isEmpty() ? "2" : "6");
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
	
	
	/* 1. 숙소 리스트 게시판 이동 컨트롤러_간소화 1 */
		@GetMapping({"/daegu_room", "/daegu_room2", "/daegu_room3", "/daegu_room4", "/daegu_room5", "/daegu_room6", "/daegu_room7", "/daegu_room8", "/daegu_room9"})
		public String room(Model model,
		                   @RequestParam(value = "page", defaultValue = "0") int page,
		                   @RequestParam(value = "kw", defaultValue = "") String kw,
		                   @RequestParam(value = "kw2", defaultValue = "") String kw2,
		                   @RequestParam(value = "kw5", defaultValue = "") List<String> kw5,
		                   @RequestParam(value = "kw6", defaultValue = "") List<String> kw6,
		                   @RequestParam(value = "kw7", defaultValue = "") String kw7) {
		    Page<Accommodation> paging = null;

		    if (kw2.isEmpty() && kw5.isEmpty() && kw6.isEmpty() && kw7.isEmpty()) {
		        paging = accommodationService.getList(page, kw);
		    } else if (!kw2.isEmpty() && kw5.isEmpty() && kw6.isEmpty() && kw7.isEmpty()) {
		        paging = accommodationService.getList2(page, kw2);
		    } else if (kw2.isEmpty() && !kw5.isEmpty() && kw6.isEmpty() && kw7.isEmpty()) {
		        paging = accommodationService.getList3(page, kw5);
		    } else if (kw2.isEmpty() && kw5.isEmpty() && !kw6.isEmpty() && kw7.isEmpty()) {
		        paging = accommodationService.getList5(page, kw6);
		    } else if (kw2.isEmpty() && kw5.isEmpty() && kw6.isEmpty() && !kw7.isEmpty()) {
		        paging = accommodationService.getList4(page, Integer.parseInt(kw7));
		    } else if (!kw2.isEmpty() && !kw5.isEmpty() && kw6.isEmpty() && kw7.isEmpty()) {
		        paging = accommodationService.getList7(page, kw2, kw5);
		    } else if (!kw2.isEmpty() && kw5.isEmpty() && !kw6.isEmpty() && kw7.isEmpty()) {
		        paging = accommodationService.getList8(page, kw2, kw5, kw6);
		    } else if (!kw2.isEmpty() && kw5.isEmpty() && kw6.isEmpty() && !kw7.isEmpty()) {
		        paging = accommodationService.getList9(page, kw2, kw5, kw7);
		    } else if (!kw2.isEmpty() && !kw5.isEmpty() && !kw6.isEmpty() && !kw7.isEmpty()) {
		        paging = accommodationService.getList6(page, kw2, kw5, kw6, kw7);
		    }

		    model.addAttribute("paging", paging);
		    model.addAttribute("kw", kw);
		    model.addAttribute("kw2", kw2);
		    model.addAttribute("kw5", kw5);
		    model.addAttribute("kw6", kw6);
		    model.addAttribute("kw7", kw7);

		    return "kty/find/daegu_room";
		}
		
		
		
		
		
	@GetMapping("/daegu_room10")
	public String room10(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "") String kw2,
			@RequestParam(value = "kw6", defaultValue = "") List<String> kw6) {
		Page<Accommodation> paging = accommodationService.getList10(page,kw2,kw6);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw6", kw6);

		return "kty/find/daegu_room2";
	}
	
	@GetMapping("/daegu_room11")
	public String room11(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "") String kw2,
			@RequestParam(value = "kw6", defaultValue = "") List<String> kw6,
			@RequestParam(value = "kw7", defaultValue = "") String kw7) {
		Page<Accommodation> paging = accommodationService.getList11(page,kw2,kw6,kw7);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw6", kw6);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room2";
	}
	
	

	@GetMapping("/daegu_room12")
	public String room12(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "") String kw2,
			@RequestParam(value = "kw7", defaultValue = "") String kw7) {
		Page<Accommodation> paging = accommodationService.getList12(page,kw2,kw7);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room2";
	}
	
	@GetMapping("/daegu_room13")
	public String room13(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw5", defaultValue = "") List<String> kw5,
			@RequestParam(value = "kw6", defaultValue = "")List<String> kw6) {
		Page<Accommodation> paging = accommodationService.getList13(page,kw5,kw6);
		model.addAttribute("paging", paging);
		model.addAttribute("kw5", kw5);
		model.addAttribute("kw6", kw6);

		return "kty/find/daegu_room2";
	}

	@GetMapping("/daegu_room14")
	public String room14(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw5", defaultValue = "") List<String> kw5,
			@RequestParam(value = "kw6", defaultValue = "")List<String> kw6,
			@RequestParam(value = "kw7", defaultValue = "")String kw7) {
		Page<Accommodation> paging = accommodationService.getList14(page,kw5,kw6,kw7);
		model.addAttribute("paging", paging);
		model.addAttribute("kw5", kw5);
		model.addAttribute("kw6", kw6);
		model.addAttribute("kw7", kw7);
		
		return "kty/find/daegu_room2";
	}
	
	@GetMapping("/daegu_room15")
	public String room15(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw6", defaultValue = "")List<String> kw6,
			@RequestParam(value = "kw7", defaultValue = "")String kw7) {
		Page<Accommodation> paging = accommodationService.getList15(page,kw6,kw7);
		model.addAttribute("paging", paging);
		model.addAttribute("kw6", kw6);
		model.addAttribute("kw7", kw7);
		
		return "kty/find/daegu_room2";
	}
	
	
	/* 최소 금액 */
	@GetMapping("/daegu_room16")
	public String room16(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw8", defaultValue = "")String kw8) {
		Page<Accommodation> paging = accommodationService.getList16(page,kw8);
		model.addAttribute("paging", paging);
		model.addAttribute("kw8", kw8);

		return "kty/find/daegu_room3";
	}
	
	/* 최대 */
	@GetMapping("/daegu_room17")
	public String room17(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList17(page,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw9", kw9);

		return "kty/find/daegu_room3";
	}
	
	/* 최대 + 최소 */
	@GetMapping("/daegu_room18")
	public String room18(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw8", defaultValue = "")String kw8,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList18(page,kw8, kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw9", kw9);

		return "kty/find/daegu_room3";
	}

	
	@GetMapping("/daegu_room19")
	public String room19(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "")String kw2,
			@RequestParam(value = "kw8", defaultValue = "")String kw8) {
		Page<Accommodation> paging = accommodationService.getList19(page,kw2, kw8);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw8", kw8);

		return "kty/find/daegu_room3";
	}
	
	
	@GetMapping("/daegu_room20")
	public String room20(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "")String kw2,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList20(page,kw2, kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw9", kw9);

		return "kty/find/daegu_room3";
	}

	
	@GetMapping("/daegu_room21")
	public String room21(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "")String kw2,
			@RequestParam(value = "kw8", defaultValue = "")String kw8,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList21(page,kw2,kw8, kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw9", kw9);

		return "kty/find/daegu_room3";
	}
	
	@GetMapping("/daegu_room22")
	public String room22(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw5", defaultValue = "") List<String> kw5,
			@RequestParam(value = "kw8", defaultValue = "")String kw8) {
		Page<Accommodation> paging = accommodationService.getList22(page,kw5,kw8);
		model.addAttribute("paging", paging);
		model.addAttribute("kw5", kw5);
		model.addAttribute("kw8", kw8);

		return "kty/find/daegu_room3";
	}
	
	@GetMapping("/daegu_room23")
	public String room23(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw5", defaultValue = "") List<String> kw5,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList23(page,kw5,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw5", kw5);
		model.addAttribute("kw9", kw9);

		return "kty/find/daegu_room3";
	}
	
	
	@GetMapping("/daegu_room24")
	public String room24(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw5", defaultValue = "") List<String> kw5,
			@RequestParam(value = "kw8", defaultValue = "")String kw8,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList24(page,kw5,kw8,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw5", kw5);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw9", kw9);

		return "kty/find/daegu_room3";
	}

	
	@GetMapping("/daegu_room25")
	public String room25(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "") String kw2,
			@RequestParam(value = "kw5", defaultValue = "")List<String> kw5,
			@RequestParam(value = "kw8", defaultValue = "")String kw8) {
		Page<Accommodation> paging = accommodationService.getList25(page,kw2,kw5,kw8);
		model.addAttribute("paging", paging);
		model.addAttribute("kw5", kw5);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw2", kw2);

		return "kty/find/daegu_room3";
	}
	
	@GetMapping("/daegu_room26")
	public String room26(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "") String kw2,
			@RequestParam(value = "kw5", defaultValue = "")List<String> kw5,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList26(page,kw2,kw5,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw9", kw9);
		model.addAttribute("kw5", kw5);

		return "kty/find/daegu_room3";
	}
	
	@GetMapping("/daegu_room27")
	public String room27(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "") String kw2,
			@RequestParam(value = "kw5", defaultValue = "")List<String> kw5,
			@RequestParam(value = "kw8", defaultValue = "")String kw8,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList27(page,kw2,kw5,kw8,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw9", kw9);
		model.addAttribute("kw5", kw5);

		return "kty/find/daegu_room3";
	}
	
	
	@GetMapping("/daegu_room28")
	public String room28(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw6", defaultValue = "")List<String> kw6,
			@RequestParam(value = "kw8", defaultValue = "")String kw8) {
		Page<Accommodation> paging = accommodationService.getList28(page,kw6,kw8);
		model.addAttribute("paging", paging);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw6", kw6);

		return "kty/find/daegu_room3";
	}
	
	
	@GetMapping("/daegu_room29")
	public String room29(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw6", defaultValue = "")List<String> kw6,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList29(page,kw6,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw9", kw9);
		model.addAttribute("kw6", kw6);

		return "kty/find/daegu_room3";
	}
	
	@GetMapping("/daegu_room30")
	public String room30(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw6", defaultValue = "")List<String> kw6,
			@RequestParam(value = "kw8", defaultValue = "")String kw8,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList30(page,kw6,kw8,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw9", kw9);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw6", kw6);

		return "kty/find/daegu_room3";
	}
	

	@GetMapping("/daegu_room31")
	public String room31(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "")String kw2,
			@RequestParam(value = "kw5", defaultValue = "")List<String> kw5,
			@RequestParam(value = "kw6", defaultValue = "")List<String> kw6,
			@RequestParam(value = "kw8", defaultValue = "")String kw8) {
		Page<Accommodation> paging = accommodationService.getList31(page,kw2,kw5,kw6,kw8);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw5", kw5);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw6", kw6);

		return "kty/find/daegu_room3";
	}
	
	
	@GetMapping("/daegu_room32")
	public String room32(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "")String kw2,
			@RequestParam(value = "kw5", defaultValue = "")List<String> kw5,
			@RequestParam(value = "kw6", defaultValue = "")List<String> kw6,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList32(page,kw2,kw5,kw6,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw5", kw5);
		model.addAttribute("kw9", kw9);
		model.addAttribute("kw6", kw6);

		return "kty/find/daegu_room3";
	}
	
	
	@GetMapping("/daegu_room33")
	public String room33(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "")String kw2,
			@RequestParam(value = "kw5", defaultValue = "")List<String> kw5,
			@RequestParam(value = "kw6", defaultValue = "")List<String> kw6,
			@RequestParam(value = "kw8", defaultValue = "")String kw8,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList33(page,kw2,kw5,kw6,kw8,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw5", kw5);
		model.addAttribute("kw9", kw9);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw6", kw6);

		return "kty/find/daegu_room3";
	}
	
	
	@GetMapping("/daegu_room34")
	public String room34(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "")String kw2,
			@RequestParam(value = "kw6", defaultValue = "")List<String> kw6,
			@RequestParam(value = "kw8", defaultValue = "")String kw8) {
		Page<Accommodation> paging = accommodationService.getList34(page,kw2,kw6,kw8);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw6", kw6);

		return "kty/find/daegu_room3";
	}
	
	@GetMapping("/daegu_room35")
	public String room35(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "")String kw2,
			@RequestParam(value = "kw6", defaultValue = "")List<String> kw6,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList35(page,kw2,kw6,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw9", kw9);
		model.addAttribute("kw6", kw6);

		return "kty/find/daegu_room3";
	}
	
	
	@GetMapping("/daegu_room36")
	public String room36(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "")String kw2,
			@RequestParam(value = "kw6", defaultValue = "")List<String> kw6,
			@RequestParam(value = "kw8", defaultValue = "")String kw8,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList36(page,kw2,kw6,kw8,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw9", kw9);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw6", kw6);

		return "kty/find/daegu_room3";
	}
	
	@GetMapping("/daegu_room37")
	public String room37(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw5", defaultValue = "")List<String> kw5,
			@RequestParam(value = "kw6", defaultValue = "")List<String> kw6,
			@RequestParam(value = "kw8", defaultValue = "")String kw8) {
		Page<Accommodation> paging = accommodationService.getList37(page,kw5,kw6,kw8);
		model.addAttribute("paging", paging);
		model.addAttribute("kw5", kw5);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw6", kw6);

		return "kty/find/daegu_room3";
	}
	
	
	@GetMapping("/daegu_room38")
	public String room38(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw5", defaultValue = "")List<String> kw5,
			@RequestParam(value = "kw6", defaultValue = "")List<String> kw6,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList38(page,kw5,kw6,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw5", kw5);
		model.addAttribute("kw9", kw9);
		model.addAttribute("kw6", kw6);

		return "kty/find/daegu_room3";
	}
	
	@GetMapping("/daegu_room39")
	public String room39(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw5", defaultValue = "")List<String> kw5,
			@RequestParam(value = "kw6", defaultValue = "")List<String> kw6,
			@RequestParam(value = "kw8", defaultValue = "")String kw8,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList39(page,kw5,kw6,kw8,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw5", kw5);
		model.addAttribute("kw9", kw9);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw6", kw6);

		return "kty/find/daegu_room3";
	}

	
	@GetMapping("/daegu_room40")
	public String room40(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw7", defaultValue = "") int kw7,
			@RequestParam(value = "kw8", defaultValue = "")String kw8) {
		Page<Accommodation> paging = accommodationService.getList40(page,kw7,kw8);
		model.addAttribute("paging", paging);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room3";
	}
	
	
	
	@GetMapping("/daegu_room41")
	public String room41(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw7", defaultValue = "") int kw7,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList41(page,kw7,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw9", kw9);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room3";
	}
	
	@GetMapping("/daegu_room42")
	public String room42(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw7", defaultValue = "") int kw7,
			@RequestParam(value = "kw8", defaultValue = "")String kw8,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList42(page,kw7,kw8,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw9", kw9);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room3";
	}
	
	@GetMapping("/daegu_room43")
	public String room43(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "") String kw2,
			@RequestParam(value = "kw7", defaultValue = "") int kw7,
			@RequestParam(value = "kw8", defaultValue = "")String kw8) {
		Page<Accommodation> paging = accommodationService.getList43(page,kw2,kw7,kw8);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room3";
	}
	
	
	@GetMapping("/daegu_room44")
	public String room44(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "") String kw2,
			@RequestParam(value = "kw7", defaultValue = "") int kw7,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList44(page,kw2,kw7,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw9", kw9);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room3";
	}
	
	@GetMapping("/daegu_room45")
	public String room45(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "") String kw2,
			@RequestParam(value = "kw7", defaultValue = "") int kw7,
			@RequestParam(value = "kw8", defaultValue = "")String kw8,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList45(page,kw2,kw7,kw8,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw9", kw9);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room3";
	}
	
	
	@GetMapping("/daegu_room46")
	public String room46(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw5", defaultValue = "") List<String> kw5,
			@RequestParam(value = "kw7", defaultValue = "") int kw7,
			@RequestParam(value = "kw8", defaultValue = "")String kw8) {
		Page<Accommodation> paging = accommodationService.getList46(page,kw5,kw7,kw8);
		model.addAttribute("paging", paging);
		model.addAttribute("kw5", kw5);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room3";
	}
	
	@GetMapping("/daegu_room47")
	public String room47(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw5", defaultValue = "") List<String> kw5,
			@RequestParam(value = "kw7", defaultValue = "") int kw7,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList47(page,kw5,kw7,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw5", kw5);
		model.addAttribute("kw9", kw9);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room3";
	}
	
	@GetMapping("/daegu_room48")
	public String room48(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw5", defaultValue = "") List<String> kw5,
			@RequestParam(value = "kw7", defaultValue = "") int kw7,
			@RequestParam(value = "kw8", defaultValue = "")String kw8,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList48(page,kw5,kw7,kw8,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw5", kw5);
		model.addAttribute("kw9", kw9);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room3";
	}
	
	
	@GetMapping("/daegu_room49")
	public String room49(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw6", defaultValue = "") List<String> kw6,
			@RequestParam(value = "kw7", defaultValue = "") int kw7,
			@RequestParam(value = "kw8", defaultValue = "")String kw8) {
		Page<Accommodation> paging = accommodationService.getList49(page,kw6,kw7,kw8);
		model.addAttribute("paging", paging);
		model.addAttribute("kw6", kw6);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room3";
	}
	
	@GetMapping("/daegu_room50")
	public String room50(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw6", defaultValue = "") List<String> kw6,
			@RequestParam(value = "kw7", defaultValue = "") int kw7,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList50(page,kw6,kw7,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw6", kw6);
		model.addAttribute("kw9", kw9);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room3";
	}
	
	@GetMapping("/daegu_room51")
	public String room51(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw6", defaultValue = "") List<String> kw6,
			@RequestParam(value = "kw7", defaultValue = "") int kw7,
			@RequestParam(value = "kw8", defaultValue = "")String kw8,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList51(page,kw6,kw7,kw8,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw6", kw6);
		model.addAttribute("kw9", kw9);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room3";
	}
	
	
	@GetMapping("/daegu_room52")
	public String room52(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw5", defaultValue = "") List<String> kw5,
			@RequestParam(value = "kw6", defaultValue = "") List<String> kw6,
			@RequestParam(value = "kw7", defaultValue = "") int kw7,
			@RequestParam(value = "kw8", defaultValue = "")String kw8) {
		Page<Accommodation> paging = accommodationService.getList52(page,kw5,kw6,kw7,kw8);
		model.addAttribute("paging", paging);
		model.addAttribute("kw6", kw6);
		model.addAttribute("kw5", kw5);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room3";
	}
	
	@GetMapping("/daegu_room53")
	public String room53(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw5", defaultValue = "") List<String> kw5,
			@RequestParam(value = "kw6", defaultValue = "") List<String> kw6,
			@RequestParam(value = "kw7", defaultValue = "") int kw7,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList53(page,kw5,kw6,kw7,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw6", kw6);
		model.addAttribute("kw5", kw5);
		model.addAttribute("kw9", kw9);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room3";
	}
	
	@GetMapping("/daegu_room54")
	public String room54(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw5", defaultValue = "") List<String> kw5,
			@RequestParam(value = "kw6", defaultValue = "") List<String> kw6,
			@RequestParam(value = "kw7", defaultValue = "") int kw7,
			@RequestParam(value = "kw8", defaultValue = "")String kw8,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList54(page,kw5,kw6,kw7,kw8,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw6", kw6);
		model.addAttribute("kw5", kw5);
		model.addAttribute("kw9", kw9);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room3";
	}
	
	@GetMapping("/daegu_room55")
	public String room55(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "")String kw2,
			@RequestParam(value = "kw5", defaultValue = "") List<String> kw5,
			@RequestParam(value = "kw7", defaultValue = "") int kw7,
			@RequestParam(value = "kw8", defaultValue = "")String kw8) {
		Page<Accommodation> paging = accommodationService.getList55(page,kw2,kw5,kw7,kw8);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw5", kw5);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room3";
	}
	
	
	@GetMapping("/daegu_room56")
	public String room56(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "")String kw2,
			@RequestParam(value = "kw5", defaultValue = "") List<String> kw5,
			@RequestParam(value = "kw7", defaultValue = "") int kw7,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList56(page,kw2,kw5,kw7,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw5", kw5);
		model.addAttribute("kw9", kw9);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room3";
	}
	
	
	
	@GetMapping("/daegu_room57")
	public String room57(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "")String kw2,
			@RequestParam(value = "kw5", defaultValue = "") List<String> kw5,
			@RequestParam(value = "kw7", defaultValue = "") int kw7,
			@RequestParam(value = "kw8", defaultValue = "")String kw8,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList57(page,kw2,kw5,kw7,kw8,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw5", kw5);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw9", kw9);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room3";
	}
	
	@GetMapping("/daegu_room58")
	public String room58(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "")String kw2,
			@RequestParam(value = "kw6", defaultValue = "") List<String> kw6,
			@RequestParam(value = "kw7", defaultValue = "") int kw7,
			@RequestParam(value = "kw8", defaultValue = "")String kw8) {
		Page<Accommodation> paging = accommodationService.getList58(page,kw2,kw6,kw7,kw8);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw6", kw6);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room3";
	}
	
	
	@GetMapping("/daegu_room59")
	public String room59(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "")String kw2,
			@RequestParam(value = "kw6", defaultValue = "") List<String> kw6,
			@RequestParam(value = "kw7", defaultValue = "") int kw7,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList59(page,kw2,kw6,kw7,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw6", kw6);
		model.addAttribute("kw9", kw9);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room3";
	}
	@GetMapping("/daegu_room60")
	public String room60(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw2", defaultValue = "")String kw2,
			@RequestParam(value = "kw6", defaultValue = "") List<String> kw6,
			@RequestParam(value = "kw7", defaultValue = "") int kw7,
			@RequestParam(value = "kw8", defaultValue = "")String kw8,
			@RequestParam(value = "kw9", defaultValue = "")String kw9) {
		Page<Accommodation> paging = accommodationService.getList60(page,kw2,kw6,kw7,kw8,kw9);
		model.addAttribute("paging", paging);
		model.addAttribute("kw2", kw2);
		model.addAttribute("kw6", kw6);
		model.addAttribute("kw9", kw9);
		model.addAttribute("kw8", kw8);
		model.addAttribute("kw7", kw7);

		return "kty/find/daegu_room3";
	}
	

@GetMapping("/daegu_room61")
public String room61(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "kw2", defaultValue = "")String kw2,
		@RequestParam(value = "kw5", defaultValue = "") List<String> kw5,
		@RequestParam(value = "kw6", defaultValue = "") List<String> kw6,
		@RequestParam(value = "kw7", defaultValue = "") int kw7,
		@RequestParam(value = "kw8", defaultValue = "")String kw8,
		@RequestParam(value = "kw9", defaultValue = "")String kw9) {
	Page<Accommodation> paging = accommodationService.getList61(page,kw2,kw5,kw6,kw7,kw8,kw9);
	model.addAttribute("paging", paging);
	model.addAttribute("kw2", kw2);
	model.addAttribute("kw5", kw5);
	model.addAttribute("kw6", kw6);
	model.addAttribute("kw9", kw9);
	model.addAttribute("kw8", kw8);
	model.addAttribute("kw7", kw7);

	return "kty/find/daegu_room3";
}

}