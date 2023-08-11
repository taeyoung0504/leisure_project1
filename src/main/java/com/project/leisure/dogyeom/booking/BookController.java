package com.project.leisure.dogyeom.booking;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.leisure.DataNotFoundException;
import com.project.leisure.dogyeom.kakao.KakaoPayController;
import com.project.leisure.dogyeom.kakao.KakaoReadyResponseVO;
import com.project.leisure.dogyeom.toss.PaymentService;
import com.project.leisure.dogyeom.toss.TossPaymentConfig;
import com.project.leisure.dogyeom.toss.domain.CancelPaymentDto;
import com.project.leisure.dogyeom.toss.domain.PaymentDto;
import com.project.leisure.dogyeom.toss.domain.PaymentResDto;
import com.project.leisure.dogyeom.toss.domain.PaymentSuccessDto;
import com.project.leisure.dogyeom.totalPrice.TotalPrice;
import com.project.leisure.dogyeom.totalPrice.TotalPriceRepository;
import com.project.leisure.yuri.product.Accommodation;
import com.project.leisure.yuri.product.AccommodationService;
import com.project.leisure.yuri.product.Product;
import com.project.leisure.yuri.product.ProductImg;
import com.project.leisure.yuri.product.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
//@Controller
@Validated
@RequestMapping("/book/*")
@Slf4j
@RequiredArgsConstructor
public class BookController {
	
private final PaymentService paymentService;
    
	private final TossPaymentConfig tossPaymentConfig;


	private final BookService bookService;
	private final RoomService roomService;

	private final KakaoPayController kakaoPayController;
	private final ProductService productService;

	private final BookingVO bookingVO;
	
	private final AccommodationService accommodationService;

	private final TotalPriceRepository totalPriceRepository;
	
	private final CancelPaymentDto cancelPaymentDto;

	@GetMapping("/toss/{id}")
	public ResponseEntity<BookingVO> toss(@PathVariable("id") String id, @RequestParam("checkin") String checkin,
			@RequestParam("checkOut") String checkOut, @RequestParam("totalPrice") String totalPrice, Model model,
			HttpServletRequest params) {

		Product product = new Product();

		BookingVO bookingvo = new BookingVO();

//		TotalPrice total = new TotalPrice();
		
		

		LocalDate date1 = LocalDate.parse(checkin);
		LocalDate date2 = LocalDate.parse(checkOut);

		int convertedId = Integer.parseInt(id);
		Long convertedId2 = (long) convertedId;
		product = productService.getProduct(convertedId2);

		String totalPrice1 = null;
		
		List<TotalPrice> totalArrays = totalPriceRepository.findByTotalPrice(totalPrice);
		for(TotalPrice totals: totalArrays) {
			if(totalPrice.equals(totals.getTotalPrice())) {
				totalPrice1 = totalPrice;
				break;
			} else {
				totalPrice1 = null;
			}
		}

		bookingvo.setBookerID("username"); // principal 넣어야함.
		bookingvo.setBookerName(params.getParameter("username"));
		bookingvo.setAccomTitle(product.getAccommodation().getAcc_name());
		bookingvo.setRoomTitle(product.getProduct_type());
		bookingvo.setCheckin(date1);
		bookingvo.setCheckOut(date2);
		bookingvo.setBookHeadCount(4);
		bookingvo.setTotalPrice(totalPrice1);
//		bookingvo.setTotalPrice(totalPrice);
		bookingvo.setTempRoomId(product.getProduct_id());
		bookingvo.setTempAccomId(product.getAccommodation().getId());

		// 추가
		List<ProductImg> images = product.getProductImgs();
		String firstImageUrl = images.stream().map(ProductImg::getImg_url) // getImageUrl()은 imageUrl 필드의 getter 메서드를
																			// 가정합니다.
				.findFirst().orElse(null);

//첫 번째 객체의 이미지 URL을 출력
		System.out.println("First Image URL: " + firstImageUrl);

//		String img = (String) selectedImage;

		bookingvo.setProductImg(firstImageUrl);

		String name = "" + id; // 꺼내 쓸 땐 equals로 넘어온 id값과 비교하여 session객체 가져오고 parseInt로 바꿔서 사용.
		// 더 효율적인 방법 찾아보기

		// session에 예약 객체 담기 => name값은 넘겨받은 객실 id(임시) -> 이후 예약이 완료되었으면 해당 id의 세션을 삭제해야함?
		// -> 안해도 될듯?(같은 객실id가 있기때문)
		HttpServletRequest request = params;
		HttpSession session = request.getSession();
		session.setAttribute(name, bookingvo);

		BookingVO booking = (BookingVO) session.getAttribute(name);

		model.addAttribute("bookingvo", bookingvo);
		return new ResponseEntity<>(bookingvo, HttpStatus.OK);

	}

	// 실제 요청 보내기

	@PostMapping("/create/{id}")
	public ResponseEntity<KakaoReadyResponseVO> booking(Model model, @PathVariable("id") Integer id,
			@RequestBody BookingVO bvo, HttpServletRequest params, HttpServletResponse response, Principal principal)
			throws IOException {
//		log.info("INFO {}", params);
		System.out.println(id);

		// 세션에서 현재 로그인한 사람의 로그인 아이디도 가져와야함.
		String username = principal.getName();

		String realName = bvo.getBookerName(); 

		String tel = bvo.getBookerTel();

		String payType = bvo.getPayType();
		
		
		Long convertedId2 = (long) id;
		Product product = new Product();
		product = productService.getProduct(convertedId2);
		
		
		Long convertedId3 = product.getAccommodation().getId();
		Accommodation accommodation = new Accommodation();
		accommodation = accommodationService.getAccommodation(convertedId3);

		String name = String.valueOf(id);
		HttpServletRequest request = params;
		HttpSession session = request.getSession();
		BookingVO bookingVO = (BookingVO) session.getAttribute(name);
		bookingVO.setBookerID(username);
		bookingVO.setBookerName(realName);
		bookingVO.setBookerTel(tel);
		bookingVO.setPayType(payType);

		bookingVO.setProduct(product);
		bookingVO.setAccommodation(accommodation);
//		bookingVO.setBookerName(params.getParameter(username));
//		String userID = (String) session.getAttribute("submarine");

		if (bookingVO == null) {
			// bookingVO가 세션에 없는 경우 적절한 예외를 던져버리기~
			throw new DataNotFoundException("bookingVO not found - 왜 없노");
		}
		log.info("INFO {}", "============================" + bookingVO.toString());

            int result = bookService.create(bookingVO); // params랑 세션에서 불러온 객체 같이 넘겨주기
    		int bookId = id;

    		if (result != 0) {
    			log.info("INFO {}", result);
    			bookingVO = bookService.getBookVO(result);
    			bookingVO.setBookNum(result);
    			log.info("INFO {}", bookingVO);
    			model.addAttribute("bookingVO", bookingVO);
    			log.info("INFO {}", model);

    			Long bookNum = Long.valueOf(result);

    			// bookingVO를 다음 메서드로 전달하기 위해 @ModelAttribute를 사용하여 전달
    			KakaoReadyResponseVO res = kakaoPayController.kakaoPay(bookingVO);

    			// 여기선 반환받은 예약번호인 result로 해당 예약정보를 찾아서 tid를 넣어준다.(이후 이 tid는 승인, 취소등에서 해당 예약정보를
    			// 찾을 때 사용)
    			bookService.updateTid(bookNum, res.getTid());

    			return new ResponseEntity<>(res, HttpStatus.OK);

    		}
    		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@PostMapping("/api/v1/payments/toss/{id}")
	public ResponseEntity<PaymentResDto> requestTossPayment(Model model, @PathVariable("id") Integer id,
			@RequestBody @Valid PaymentDto paymentReqDto, 
			HttpServletRequest params, 
			HttpServletResponse response,
			Principal principal
			)
					throws IOException {
//		log.info("INFO {}", params);
//		@AuthenticationPrincipal Users principal
		// 세션에서 현재 로그인한 사람의 로그인 아이디도 가져와야함.
		String username = principal.getName();
		
//		Users user = 
		
//		String email = principal.getEmail();
		
		String realName = paymentReqDto.getCustomerName();
		
		String tel = paymentReqDto.getCustomerTel();
		
		Enum payType = paymentReqDto.getPayType();
		
		
		PaymentResDto paymentResDto = paymentService.requestTossPayment(paymentReqDto.toEntity(), principal.getName()).toPaymentResDto();
        
    	paymentResDto.setSuccessUrl(paymentReqDto.getYourSuccessUrl() == null ? tossPaymentConfig.getSuccessUrl() : paymentReqDto.getYourSuccessUrl());
        
    	paymentResDto.setFailUrl(paymentReqDto.getYourFailUrl() == null ? tossPaymentConfig.getFailUrl() : paymentReqDto.getYourFailUrl());
		
		Long convertedId2 = (long) id;
		Product product = new Product();
		product = productService.getProduct(convertedId2);
		
		String payTypeString = payType.name();
		
		Long convertedId3 = product.getAccommodation().getId();
		Accommodation accommodation = new Accommodation();
		accommodation = accommodationService.getAccommodation(convertedId3);
		
		String name = String.valueOf(id);
		HttpServletRequest request = params;
		HttpSession session = request.getSession();
		BookingVO bookingVO = (BookingVO) session.getAttribute(name);
		bookingVO.setBookerID(username);
		bookingVO.setBookerName(realName);
		bookingVO.setBookerTel(tel);
		bookingVO.setPayType(payTypeString);
		bookingVO.setProduct(product);
		bookingVO.setAccommodation(accommodation);
		bookingVO.setTid(paymentResDto.getOrderId());
//		bookingVO.setBookerName(params.getParameter(username));
//		String userID = (String) session.getAttribute("submarine");
		
		if (bookingVO == null) {
			// bookingVO가 세션에 없는 경우 적절한 예외를 던져버리기~
			throw new DataNotFoundException("bookingVO not found - 왜 없노");
		}
		log.info("INFO {}", "============================" + bookingVO.toString());
		
		int result = bookService.create(bookingVO); // params랑 세션에서 불러온 객체 같이 넘겨주기
		int bookId = id;
		
		if (result != 0) {
			log.info("INFO {}", result);
			bookingVO = bookService.getBookVO(result);
			bookingVO.setBookNum(result);
			log.info("INFO {}", bookingVO);
			model.addAttribute("bookingVO", bookingVO);
			log.info("INFO {}", model);
			
			Long bookNum = Long.valueOf(result);
			
			// bookingVO를 다음 메서드로 전달하기 위해 @ModelAttribute를 사용하여 전달
			
			// 여기선 반환받은 예약번호인 result로 해당 예약정보를 찾아서 tid를 넣어준다.(이후 이 tid는 승인, 취소등에서 해당 예약정보를
			// 찾을 때 사용)
			
			// -> 여기서 받아온 tid바로 예약테이블에 저장하고 받아온 아이디에 tid update하기
			
			return ResponseEntity.ok().body(paymentResDto);
			
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@GetMapping("/api/v1/payments/toss/success")
    public ResponseEntity<PaymentSuccessDto> tossPaymentSuccess(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount
    ) {
		
		PaymentSuccessDto result = paymentService.tossPaymentSuccess(paymentKey, orderId, amount);
		
		String payDate = result.getApprovedAt();
		String tid = result.getOrderId();
		String status = "예약완료";
		
		bookService.updatePaymentDate(tid, payDate, status);
		
        return ResponseEntity.ok().body(result);
    }
	
	@PostMapping("/toss/cancel")
	  public  ResponseEntity<CancelPaymentDto> tossPaymentCancelPoint(
	  		@RequestBody Map<String, String> requestParams
	  ) {
	      
	  	String paymentKey = requestParams.get("paymentKey");
	      String cancelReason = requestParams.get("cancelReason");
	  	
	  	CancelPaymentDto cres = paymentService.tossPaymentCancel(paymentKey, cancelReason);
	  	
	  	String tid = paymentKey;
	  	String status = cres.getStatus();
	  	String canceled_at = cres.getApprovedAt();
	  	bookService.updateCancel(tid, status, canceled_at);
	  	
	//  	model.addAttribute("info", cres);
	  	
	  	return ResponseEntity.ok().body(cres);
	  }
	

}