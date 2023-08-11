package com.project.leisure.hyokyeong.partner;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.project.leisure.dogyeom.booking.BookService;
import com.project.leisure.taeyoung.user.RegPartner;
import com.project.leisure.taeyoung.user.RegRepository;
import com.project.leisure.taeyoung.user.UserRepository;
import com.project.leisure.taeyoung.user.UserRole;
import com.project.leisure.taeyoung.user.Users;
import com.project.leisure.yuri.product.Accommodation;
import com.project.leisure.yuri.product.AccommodationRepository;
import com.project.leisure.yuri.product.AccommodationService;
import com.project.leisure.yuri.product.Product;
import com.project.leisure.yuri.product.ProductService;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class PartnerListService {

	@Autowired
	private final RegRepository regRepository;
	// private UserListService userListService;
	private UserRepository userRepository;

	@Autowired
	private ProductService productService;

	@Autowired
	private AccommodationRepository accommodationRepository;
	@Autowired
	private BookService bookService;
	@Autowired
	private AccommodationService accommodationService;

	@Autowired
	public PartnerListService(RegRepository regRepository, UserRepository userRepository) {
		this.regRepository = regRepository;
		this.userRepository = userRepository;
	}

	// 파트너 리스트 가져오기
	public List<RegPartner> partnerList(Model model) {
		List<RegPartner> partnerList = this.getPartnerRegList();
		model.addAttribute("partners", partnerList);
		return partnerList;
	}

	public List<RegPartner> getPartnerRegList() {
		return regRepository.findAll();
	}

	public void saveUser(Users users) {
		regRepository.save(users);
	}

	public String getFileUrlFromDatabase(String filename) {
		RegPartner partner = regRepository.findByFilename(filename);
		if (partner != null) {
			String encodedFilename = URLEncoder.encode(partner.getFilename(), StandardCharsets.UTF_8);
			return "/img/partner_regi_img/" + encodedFilename;
		}
		return null;
	}

	public String getBaseUrl(HttpServletRequest request) {
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		int serverPort = request.getServerPort();
		String contextPath = request.getContextPath();
		return scheme + "://" + serverName + ":" + serverPort + contextPath;
	}

	// 파일리스트 가져오기
	public List<String> getFileList() {
		List<RegPartner> partners = regRepository.findAll();
		List<String> fileList = new ArrayList<>();
		for (RegPartner partner : partners) {
			String fileName = partner.getFilename();
			fileList.add(fileName);
		}
		return fileList;
	}

	// 파트너신청목록 불러오기
	public Page<RegPartner> getPartnerRegPage(int page, String kw) {
		Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Direction.DESC, "id"));
		Specification<RegPartner> spec = search(kw);
		return this.regRepository.findAll(spec, pageable);
	}

	// 파트너 승인 시 권한 변경 로직
	public Long handleApproval(Long id, int status) {
		Optional<RegPartner> partnerOptional = regRepository.findById(id);

		if (partnerOptional.isPresent()) {
			RegPartner partner = partnerOptional.get();
			partner.setResult_partner_reg(status);
			regRepository.save(partner);

			if (status == 1) {
				String regUsername = partner.getReg_username();
				Users user = userRepository.findFirstByUsername(regUsername);
				if (user != null) {
					user.setRole(UserRole.PARTNER);
					user.setAdmin_code(0);
					user.setPartner_code(3333);
					userRepository.saveAndFlush(user); // 변경된 값을 즉시 저장
				}
			} else if (status == 0) {
				String regUsername = partner.getReg_username();
				Users user = userRepository.findFirstByUsername(regUsername);
				// 해당 사용자 이름을 가진 숙소 정보를 조회
				List<Accommodation> partnersWithUsername = accommodationRepository.findByusername(regUsername);
				// 해당 사용자 이름을 가진 파트너 등록 정보를 조회
				List<RegPartner> partnerRegList = regRepository.findByReg_username(regUsername);

				// 등록된 숙소가 0일 경우에 사용자로 전환
				if (partnersWithUsername.size() == 0) {
					// 파트너 신청을 이미 해서 승인된 경우가 존재.(아직 등록전)
					if (partnerRegList.size() > 0) {
						user.setRole(UserRole.PARTNER);
						user.setAdmin_code(0);
						user.setPartner_code(3333);
						userRepository.saveAndFlush(user); // 변경된 값을 즉시 저장
					}
					// 파트너 신청을 했던 안했던 그냥 승인이 없는 경우. 그럼 USER
					if (partnerRegList.size() == 0) {
						user.setRole(UserRole.USER);
						user.setAdmin_code(0);
						user.setPartner_code(0);
						userRepository.saveAndFlush(user); // 변경된 값을 즉시 저장
					}

				}
				// 등록된 숙소는 있는 경우
				else {
					// 파트너신청을 해서 승인이 있는경우
					if (partnerRegList.size() > 0) {
						user.setRole(UserRole.PARTNER);
						user.setAdmin_code(0);
						user.setPartner_code(3333);
						userRepository.saveAndFlush(user); // 변경된 값을 즉시 저장

						// 주소를 비교해서 찾음
						String addr = partnerOptional.get().getCompany_address();

						Long foundAccId = null; // 주소가 맞는 숙소의 id를 저장할 변수

						for (Accommodation accInfo : partnersWithUsername) {
							if (addr.equals(accInfo.getAcc_address())) {
								foundAccId = accInfo.getId();
								break; // 주소가 맞는 숙소를 찾았으므로 루프 종료
							}
						}
						return foundAccId;

					}
					// 파트너 신청했지만 승인다 취소==없음. 근데 숙소 있음. => 숙소지워줘야함.
					else {

						user.setRole(UserRole.USER);
						user.setAdmin_code(0);
						user.setPartner_code(0);
						userRepository.saveAndFlush(user); // 변경된 값을 즉시 저장

						// 주소를 비교해서 찾음
						String addr = partnerOptional.get().getCompany_address();

						Long foundAccId = null; // 주소가 맞는 숙소의 id를 저장할 변수

						for (Accommodation accInfo : partnersWithUsername) {
							if (addr.equals(accInfo.getAcc_address())) {
								foundAccId = accInfo.getId();
								break; // 주소가 맞는 숙소를 찾았으므로 루프 종료
							}
						}

					
						return foundAccId;

					}

				}
			}
		}
		return null;

	}



	// 검색기능
	private Specification<RegPartner> search(String kw) {
		return new Specification<RegPartner>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<RegPartner> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true); // 중복을 제거

				String searchKeyword = "%" + kw + "%";
				Predicate usernamePredicate = cb.like(root.get("reg_username"), searchKeyword);

				return cb.or(usernamePredicate);
			}
		};
	}

}