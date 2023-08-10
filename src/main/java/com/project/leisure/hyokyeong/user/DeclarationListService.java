package com.project.leisure.hyokyeong.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.project.leisure.taeyoung.review.Declaration;
import com.project.leisure.taeyoung.review.DeclarationRepository;
import com.project.leisure.taeyoung.user.RegRepository;
import com.project.leisure.taeyoung.user.UserRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
@EnableScheduling
public class DeclarationListService {
	private final UserRepository userRepository;
	private final RegRepository regRepository;
    private final DeclarationRepository declarationRepository;


	public DeclarationListService(UserRepository userRepository, RegRepository regRepository,DeclarationRepository declarationRepository) {
		this.userRepository = userRepository;
		this.regRepository = regRepository;
		this.declarationRepository = declarationRepository;
	}

	
	public List<Declaration> declarationList(Model model) {
		List<Declaration> users = this.declarationRepository.findAll();
		model.addAttribute("users", users);
		return users;
	}

	public Page<Declaration> getList(int page, String kw) {
		Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Direction.ASC, "id"));
		 Specification<Declaration> spec = search(kw);
		return this.declarationRepository.findAll(spec, pageable);
	}

	private Specification<Declaration> search(String kw) {
	    return new Specification<Declaration>() {
	        private static final long serialVersionUID = 1L;

	        @Override
	        public Predicate toPredicate(Root<Declaration> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
	            query.distinct(true);  // 중복을 제거
	            
	            String searchKeyword = "%" + kw + "%";
	            Predicate authorPredicate = cb.like(root.get("review").get("author"), searchKeyword);

	            return cb.or(authorPredicate);
	        }
	    };
	}

	
	
}