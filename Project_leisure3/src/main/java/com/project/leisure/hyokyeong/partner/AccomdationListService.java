package com.project.leisure.hyokyeong.partner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.project.leisure.hyokyeong.user.UserListService;
import com.project.leisure.taeyoung.user.RegPartner;
import com.project.leisure.taeyoung.user.UserRepository;
import com.project.leisure.taeyoung.user.Users;
import com.project.leisure.yuri.product.Accommodation;
import com.project.leisure.yuri.product.AccommodationRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class AccomdationListService {

	@Autowired
	private final AccommodationRepository accommodationRepository;
	private UserListService userListService;
	private UserRepository userRepository;

	@Autowired
	public AccomdationListService(AccommodationRepository accommodationRepository, UserRepository userRepository) {
		this.accommodationRepository = accommodationRepository;
		this.userRepository = userRepository;
	}

	// 등록 숙소 목록 가져오기
	public List<Accommodation> accommodationList(Model model) {
		List<Accommodation> accommodationList = this.getaccommodationList();
		model.addAttribute("accommodations", accommodationList);
		return accommodationList;
	}

	public List<Accommodation> getaccommodationList() {
		return accommodationRepository.findAll();
	}

	public void saveUser(Users users) {
		accommodationRepository.save(users);
	}

	// 등록 숙소 목록 불러오기
	public Page<Accommodation> getAccommodationList(int page, String kw) {
		Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Direction.ASC, "id"));
		Specification<Accommodation> spec = search(kw);
		return this.accommodationRepository.findAll(spec, pageable);
	}
	
	
	// 검색기능
	private Specification<Accommodation> search(String kw) {
		return new Specification<Accommodation>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Predicate toPredicate(Root<Accommodation> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true); // 중복을 제거
				
				String searchKeyword = "%" + kw + "%";
				Predicate usernamePredicate = cb.like(root.get("acc_name"), searchKeyword);
				
				return cb.or(usernamePredicate);
			}
		};

	}

	
	
//	public String getFileUrlFromDatabase(String filename) {
//		RegPartner partner = regRepository.findByFilename(filename);
//		if (partner != null) {
//			String encodedFilename = URLEncoder.encode(partner.getFilename(), StandardCharsets.UTF_8);
//			return "/img/partner_regi_img/" + encodedFilename;
//		}
//		return null;
//	}
//
//	public String getBaseUrl(HttpServletRequest request) {
//		String scheme = request.getScheme();
//		String serverName = request.getServerName();
//		int serverPort = request.getServerPort();
//		String contextPath = request.getContextPath();
//		return scheme + "://" + serverName + ":" + serverPort + contextPath;
//	}
//
//	// 파일리스트 가져오기
//	public List<String> getFileList() {
//		List<Accommodation> partners = regRepository.findAll();
//		List<String> fileList = new ArrayList<>();
//		for (RegPartner partner : partners) {
//			String fileName = partner.getFilename();
//			fileList.add(fileName);
//		}
//		return fileList;
//	}


}