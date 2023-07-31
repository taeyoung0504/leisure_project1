package com.project.leisure.dogyeom.booking;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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
import lombok.extern.slf4j.Slf4j;

@RestController
//@Controller
@RequestMapping("/book/*")
@Slf4j
public class BookController {


	private BookService bookService;
	private RoomService roomService;

	private KakaoPayController kakaoPayController;
	private ProductService productService;

	private BookingVO bookingVO;
	
	private AccommodationService accommodationService;

	private final TotalPriceRepository totalPriceRepository;

	@Autowired
	public BookController(BookService bookService, RoomService roomService, KakaoPayController kakaoPayController,
			ProductService productService, TotalPriceRepository totalPriceRepository, AccommodationService accommodationService) {
		this.bookService = bookService;
		this.roomService = roomService;
		this.kakaoPayController = kakaoPayController;
		this.productService = productService;
		this.totalPriceRepository = totalPriceRepository;
		this.accommodationService = accommodationService;
	}

	@GetMapping("/toss/{id}")
	public ResponseEntity<BookingVO> toss(@PathVariable("id") String id, @RequestParam("checkin") String checkin,
			@RequestParam("checkOut") String checkOut, @RequestParam("totalPrice") String totalPrice, Model model,
			HttpServletRequest params) {

		Product product = new Product();

		BookingVO bookingvo = new BookingVO();

		TotalPrice total = new TotalPrice();
		
		

		LocalDate date1 = LocalDate.parse(checkin);
		LocalDate date2 = LocalDate.parse(checkOut);

		int convertedId = Integer.parseInt(id);
		Long convertedId2 = (long) convertedId;
		product = productService.getProduct(convertedId2);

		String totalPrice1 = null;

		List<TotalPrice> totalArrays = totalPriceRepository.findByTotalPrice(totalPrice);
		for (TotalPrice totals : totalArrays) {
			if (totalPrice.equals(totals.getTotalPrice())) {
				totalPrice1 = totalPrice;
				break;
			} else {
				totalPrice1 = null;
			}
		}

		// request로 받은 값들 예약객체에 넣기 // -> build 써야하나? -> 몇몇 정보만 넣고 싶다.
		bookingvo.setBookerID("username");
		bookingvo.setBookerName(params.getParameter("username"));
		bookingvo.setAccomTitle(product.getAccommodation().getAcc_name());
		bookingvo.setRoomTitle(product.getProduct_type());
		bookingvo.setCheckin(date1);
		bookingvo.setCheckOut(date2);
		bookingvo.setBookHeadCount(4);
		bookingvo.setTotalPrice(totalPrice1);
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

}