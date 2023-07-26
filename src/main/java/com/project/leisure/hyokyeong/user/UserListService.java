package com.project.leisure.hyokyeong.user;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import org.aspectj.weaver.patterns.TypePatternQuestions.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.project.leisure.taeyoung.user.RegPartner;
import com.project.leisure.taeyoung.user.RegRepository;
import com.project.leisure.taeyoung.user.UserRepository;
import com.project.leisure.taeyoung.user.UserRole;
import com.project.leisure.taeyoung.user.Users;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
@EnableScheduling
public class UserListService {
	private final UserRepository userRepository;
	private final RegRepository regRepository;

	public UserListService(UserRepository userRepository, RegRepository regRepository) {
		this.userRepository = userRepository;
		this.regRepository = regRepository;
	}

	public List<Users> findAllUsers() {
		return userRepository.findAll();
	}

	public void updateUserRole(Long userId, UserRole role) {
		Optional<Users> userOptional = userRepository.findById(userId);
		if (userOptional.isPresent()) {
			Users user = userOptional.get();
			user.setRole(role);
			updateAdminCode(user);
			userRepository.save(user);
		} else {
			throw new IllegalArgumentException("해당 사용자를 찾을 수 없습니다.");
		}
	}

	private void updateAdminCode(Users user) {
		if (user.getRole() == UserRole.ADMIN) {
			user.setPartner_code(0);
			user.setAdmin_code(1111);
			userRepository.save(user);
		} else if (user.getRole() == UserRole.PARTNER) {
			user.setAdmin_code(0);
			user.setPartner_code(3333);
			userRepository.save(user);
		} else {
			user.setAdmin_code(0);
			user.setPartner_code(0);
			userRepository.save(user);
		}
	}

	public int toggleAccountStatus(String username) {
		Optional<Users> userOptional = getUserByUsername(username);
		if (userOptional.isPresent()) {
			Users user = userOptional.get();
			int currentStatus = user.getIslock();
			int newStatus = (currentStatus == 0) ? 1 : 0; // 현재 상태와 반대로 토글
			user.setIslock(newStatus);
			System.out.println(username + "======================계정 상태 변경=====================" + newStatus);

			if (newStatus == 1) {
				// 계정이 잠금 상태로 변경되었을 때
				LocalDateTime lockTime = LocalDateTime.now(); // 현재 시간을 잠금 시간으로 설정
				user.setLockTime(lockTime);
				System.currentTimeMillis();
			} else {
				// 계정이 잠금 해제 상태로 변경되었을 때
				user.setLockTime(null); // 잠금 시간 초기화
			}

			saveUser(user);
			return newStatus;
		} else {
			// 사용자가 존재하지 않을 경우 예외 처리
			throw new IllegalArgumentException("User not found: " + username);
		}
	}

	public Optional<Users> getUserByUsername(String username) {
		return userRepository.findByusername(username);
	}

	public void saveUser(Users user) {
		// TODO Auto-generated method stub
		userRepository.save(user);
	}

	public List<Users> userList(Model model) {
		List<Users> users = this.userRepository.findAll();
		model.addAttribute("users", users);
		return users;
	}

	@Scheduled(fixedDelay = 10000)
	public void toggleAccountStatusScheduled() {
		System.out.println("Scheduled task: Toggle account status");

		List<Users> users = userRepository.findAll();
		for (Users user : users) {
			if (user.getIslock() == 1) {
				LocalDateTime lockTime = user.getLockTime(); // 잠금 시간 가져오기
				LocalDateTime unlockTime = lockTime.plusMinutes(1); // 1분 후에 잠금 해제 시간 계산

				LocalDateTime now = LocalDateTime.now();
				if (now.isAfter(unlockTime)) {
					// 현재 시간이 잠금 해제 시간을 지났을 경우
					int newStatus = toggleAccountStatus(user.getUsername());
					System.out.println(
							"Account status toggled for user: " + user.getUsername() + ", New status: " + newStatus);
				} else {
					// 잠금 해제 시간을 아직 지나지 않았을 경우
					scheduleUnlockTask(user.getUsername(), unlockTime);
				}
			}
		}
	}

	private void scheduleUnlockTask(String username, LocalDateTime unlockTime) {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				int newStatus = toggleAccountStatus(username);
				System.out.println("Account status toggled for user: " + username + ", New status: " + newStatus);
			}
		};

		long delay = ChronoUnit.MILLIS.between(LocalDateTime.now(), unlockTime); // 잠금 해제까지 남은 시간 계산
		Timer timer = new Timer();
		timer.schedule(task, delay); // 남은 시간 후에 실행
	}

	public void getUserById(Long id, Model model) {
		Optional<Users> userOptional = userRepository.findById(id);
		if (userOptional.isPresent()) {
			Users user = userOptional.get();
			model.addAttribute("user", user);
		} else {
			throw new IllegalArgumentException("해당 사용자를 찾을 수 없습니다.");
		}
	}

	public Page<Users> getList(int page, String kw) {
		Pageable pageable = PageRequest.of(page, 10);
		 Specification<Users> spec = search(kw);
		return this.userRepository.findAll(spec, pageable);
	}

	public Optional<RegPartner> findByUsers(Users users) {
		return regRepository.findByUsers(users);
	}

	public void updateUserRoleByUsername(String username) {
		Optional<Users> userOptional = userRepository.findByusername(username);
		if (userOptional.isPresent()) {
			Users user = userOptional.get();

			Optional<RegPartner> partnerOptional = findByUsers(user);
			if (partnerOptional.isPresent()) {
				// RegPartner의 users 필드와 Users의 username이 일치함
				user.setRole(UserRole.PARTNER);
				updateAdminCode(user);
				userRepository.save(user);
			}
		} else {
			throw new IllegalArgumentException("해당 사용자를 찾을 수 없습니다.");
		}
	}
	
	
	private Specification<Users> search(String kw) {
	    return new Specification<Users>() {
	        private static final long serialVersionUID = 1L;

	        @Override
	        public Predicate toPredicate(Root<Users> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
	            query.distinct(true);  // 중복을 제거
	            
	            String searchKeyword = "%" + kw + "%";
	            Predicate usernamePredicate = cb.like(root.get("username"), searchKeyword);

	            return cb.or(usernamePredicate);
	        }
	    };
	}


}