package com.project.leisure.hyokyeong.user;

import org.springframework.data.domain.Pageable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.leisure.dogyeom.booking.BookService;
import com.project.leisure.dogyeom.booking.BookingVO;
import com.project.leisure.son.board.Inquiry;
import com.project.leisure.son.board.InquiryService;
import com.project.leisure.son.notice.Notice;
import com.project.leisure.son.notice.NoticeService;
import com.project.leisure.taeyoung.review.Declaration;
import com.project.leisure.taeyoung.review.DeclarationRepository;
import com.project.leisure.taeyoung.review.Review;
import com.project.leisure.taeyoung.review.ReviewRepository;
import com.project.leisure.taeyoung.user.CancelRequest;
import com.project.leisure.taeyoung.user.CancelRequestService;
import com.project.leisure.taeyoung.user.UserRepository;
import com.project.leisure.taeyoung.user.UserRole;
import com.project.leisure.taeyoung.user.UserService;
import com.project.leisure.taeyoung.user.Users;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private final UserRepository userRepository;
	@Autowired
	private final UserListService userListService;
	private final UserService userService;
	private final DeclarationRepository declarationRepository;
	private final DeclarationListService declarationListService;
	private final ReviewRepository reviewRepository;
	private final InquiryService inquiryService;
	private final NoticeService noticeService;
	private final CancelRequestService cancelRequestService;
	private final BookService bookService;
	
	
	@GetMapping("/adminMain")
	public String adminMain() {
		System.out.println("메인페이지로 이동");
		return "khk/authorityPage";
	}

	// 권한 변경 페이지
	@GetMapping("/authorityPage")
	public String authorityPage(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw", defaultValue = "") String kw) {
		Page<Users> paging = this.userListService.getList(page, kw);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		userListService.userList(model);
		model.addAttribute("userList", paging.getContent()); // 회원 목록을 userList라는 속성으로 추가

		// 전체 객체 수를 전달
		model.addAttribute("objectCount", paging.getTotalElements());

		return "khk/authorityPage";
	}

	// 회원 목록
	@GetMapping("/userListPage")
	public String userListPage(Model model) {
		userListService.userList(model);
		return "khk/userListPage";
	}

	// 회원 상세 정보 모달
	@GetMapping("/authorityPage/{id}")
	public String userListPage2(@PathVariable("id") String encodedId, Model model) throws UnsupportedEncodingException {
		String decodedId = URLDecoder.decode(encodedId, "UTF-8");
		Long id = Long.parseLong(decodedId);
		userListService.getUserById(id, model); // 선택된 회원의 정보를 조회하는 메서드 호출
		return "khk/authorityPage";
	}

	// 회원 정보 가져오기
//	@GetMapping("/getUserByUsername")
//	public String getUserByUsername(@RequestParam("username") String username, Model model) {
//		Optional<Users> userOptional = userListService.getUserByUsername(username);
//		if (userOptional.isPresent()) {
//			Users user = userOptional.get();
//			model.addAttribute("user", user);
//			return "khk/authorityPage"; // 사용자 정보 페이지로 이동하는 뷰 이름을 반환하세요
//		} else {
//			return "khk/adminMain"; // 사용자를 찾을 수 없을 경우 메인으로
//		}
//	}

	// 회원 등급 변경 기능
	@PostMapping("/{id}/adminRole")
	public String updateUserRole(@PathVariable("id") Long id, @RequestParam("role") UserRole role, Model model,
			HttpServletRequest request) {
		try {
			userListService.updateUserRole(id, role); // 서비스를 통해 DB 업데이트 수행
			List<Users> userList = userListService.userList(model); // 변경된 회원 목록을 조회
			model.addAttribute("users", userList); // 변경된 회원 목록을 모델에 추가
			// 이전 페이지 URL 가져오기
			String referer = request.getHeader("Referer");
			// 이전 페이지로 리다이렉트
			return "redirect:" + referer;
		} catch (IllegalArgumentException e) {
			return "redirect:/admin/adminMain"; // 예외 발생 시 관리자 메인 페이지로 리다이렉트
		}
	}

	// 회원 계정 잠금 토글
	@PostMapping("/toggle-account")
	public ResponseEntity<Map<String, Integer>> toggleAccount(@RequestParam(name = "username") String username,
			HttpServletRequest request) {
		try {
			int newStatus = userListService.toggleAccountStatus(username); // 계정 상태 토글 메서드 호출
			System.out.println(username);
			// 토글된 사용자 및 새로운 상태를 Map으로 저장
			Map<String, Integer> response = new HashMap<>();
			response.put("newStatus", newStatus);

			HttpSession session = request.getSession();
			session.setAttribute("toggledUser", username); // 토글된 사용자를 세션에 저장

			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			// 예외 처리
			return ResponseEntity.badRequest().build();
		}
	}

	// 회원 상세정보 수정
	@PostMapping("/userInfoUpdate")
	public String updateUserInfo(Model model, @RequestParam("id") Long id, @RequestParam("nickname") String nickname,
			@RequestParam("zipcode") String zipcode, @RequestParam("address1") String address1,
			@RequestParam("address2") String address2) {
		userListService.getUserById(id, model);
		Users user = (Users) model.getAttribute("user");
		if (user != null) {
			user.setNickname(nickname);
			user.setAddr1(zipcode);
			user.setAddr2(address1);
			user.setAddr3(address2);
			userService.save(user);
			return "redirect:/admin/authorityPage";
		}
		return "redirect:/admin/authorityPage";

	}

	// 신고리스트 불러오기
	@GetMapping("/declarationList")
	public String declarationList(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw", defaultValue = "") String kw) {
		Page<Declaration> paging = this.declarationListService.getList(page, kw);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		declarationListService.declarationList(model);
		model.addAttribute("declarationList", paging.getContent()); // 신고 목록을 decalarationList라는 속성으로 추가
		// 전체 객체 수를 전달
		model.addAttribute("objectCount", paging.getTotalElements());
		return "khk/declaration";
	}

//	// 리뷰 블라인드 해제
//	@PostMapping("/unblind-review/{reviewId}")
//	public String unblindReview(@PathVariable("reviewId") String reviewId) {
//		// 리뷰 ID를 사용하여 리뷰 정보를 가져옵니다.
//		Long id = Long.parseLong(reviewId);
//		Optional<Review> reviewOptional = reviewRepository.findById(id);
//		if (reviewOptional.isPresent()) {
//			Review review = reviewOptional.get();
//
//			// 리뷰의 블라인드 상태를 변경합니다.
//			review.setContentBlinded(false);
//			reviewRepository.save(review);
//
//			return "redirect:/admin/declaration";
//		} else {
//			return "redirect:/admin/authorityPage";
//		}
//	}

	@PostMapping("/unblind-review/{reviewId}")
	public String unblindReview(@PathVariable("reviewId") String reviewId) {
		Long id = Long.parseLong(reviewId);
		Optional<Review> reviewOptional = reviewRepository.findById(id);
		if (reviewOptional.isPresent()) {
			Review review = reviewOptional.get();
			review.setContentBlinded(false);
			reviewRepository.save(review);
		}
		return "redirect:/admin/decalarationList";
	}

	@PostMapping("/blind-review/{reviewId}")
	public String blindReview(@PathVariable("reviewId") String reviewId) {
		Long id = Long.parseLong(reviewId);
		Optional<Review> reviewOptional = reviewRepository.findById(id);
		if (reviewOptional.isPresent()) {
			Review review = reviewOptional.get();
			review.setContentBlinded(true);
			reviewRepository.save(review);
		}
		return "redirect:/admin/decalarationList";
	}

///////////////////////// 1:1 문의 컨트롤러 /////////////////////////////////////

	@GetMapping("/inquiryList")
	public String inquiryList(Model model, Principal principal, 
	    @RequestParam(required = false) String filter,
	    @PageableDefault(size = 10, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
	        
	    String username = principal.getName();
	    Page<Inquiry> page;

	    if ("answered".equalsIgnoreCase(filter)) {
	        page = inquiryService.getAnsweredInquiries(pageable);
	    } else if ("pending".equalsIgnoreCase(filter)) {
	        page = inquiryService.getPendingInquiries(pageable);
	    } else {
	        page = inquiryService.getAllInquiries(pageable);
	    }
	    
	    model.addAttribute("page", page);
	    return "syw/inquiry_list";
	}

	@GetMapping("/inquiryAnswer/{id}")
	public String inquiryAnswer(@PathVariable("id") Integer id, Model model, Principal principal) {
		String username = principal.getName();
		System.out.print(username);
		Optional<Inquiry> inquiry = inquiryService.findById(id);
		model.addAttribute("inquiry", inquiry);
		return "syw/inquiry_answer";
	}

//////////////////////공지사항 /////////////////////////////////

	@GetMapping("/adminNoticeList")
	public String adminNoticeList(Model model,
			@PageableDefault(size = 5, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<Notice> page = noticeService.getAllNotices(pageable);
		model.addAttribute("page", page);
		return "syw/adminNotice_list";
	}

	@GetMapping("/createNotice")
	public String createNotice(Model model, Principal principal) {
		String username = principal.getName();
		System.out.println(username);
		model.addAttribute("notice", new Notice());
		return "syw/notice";
	}

	@PostMapping("/createNotice")
	public String saveNotice(@ModelAttribute("notice") Notice notice, Principal principal) {
		String username = principal.getName();
		notice.setUsername(username);
		notice.setCreateDate(LocalDateTime.now());
		noticeService.createNotice(notice);
		return "redirect:/admin/adminNoticeList";
	}

	@GetMapping("/modify/{id}")
	public String editNotice(@PathVariable Integer id, Model model, Principal principal) {
		Notice notice = noticeService.getNotice(id);
		model.addAttribute("notice", notice);
		return "syw/notice_modify";
	}

	@PostMapping("/modify/{id}")
	public String updateNotice(@PathVariable Integer id, @ModelAttribute("notice") Notice updatedNotice,
			Principal principal) {
		Notice notice = noticeService.getNotice(id);
		notice.setTitle(updatedNotice.getTitle());
		notice.setContent(updatedNotice.getContent());
		notice.setUsername(principal.getName());
		noticeService.updateNotice(id, notice);
		return "redirect:/admin/adminNoticeList";
	}

	@GetMapping("/delete/{id}")
	public String deleteNotice(@PathVariable Integer id) {
		noticeService.deleteNotice(id);
		return "redirect:/admin/adminNoticeList";
	}

	@GetMapping("/main")
	public String admin_dashboard(Model model) {
		LocalDate currentDate = LocalDate.now();
		model.addAttribute("currentDate", currentDate);
		return "kty/admin_main";
	}
	
	@GetMapping("/cancle_req")
	public String cancle_req(Model model) {
		List<CancelRequest> canclereqList = this.cancelRequestService.getCancleReq();
		List<BookingVO> book = this.bookService.getbooklist();
		
		model.addAttribute("book",book);
		model.addAttribute("cancleList",canclereqList);
		return "kty/cancle_request_list";
		
	}
}