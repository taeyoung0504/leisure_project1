package com.project.leisure.taeyoung.user;


// 스프링 시큐리티 -> 로그인 처리
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Users> _siteUser = this.userRepository.findByusername(username);
		if (_siteUser.isEmpty()) {
			throw new UsernameNotFoundException("없는 계정이거나 ID/PW가 틀렸습니다.");
		}

		Users siteUser = _siteUser.get();
		Integer admin = siteUser.getAdmin_code();
		Integer partner = siteUser.getPartner_code();

		if (siteUser.getIslock() == 1) {
			throw new LockedException("계정이 잠겨 있습니다. 관리자에게 문의하세요.");
		}

		List<GrantedAuthority> authorities = new ArrayList<>();
		if (admin > 1000 ) {
			siteUser.setRole(UserRole.ADMIN); 
			authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
		} else if (partner > 3000 ) {
			siteUser.setRole(UserRole.PARTNER); 
			authorities.add(new SimpleGrantedAuthority(UserRole.PARTNER.getValue()));
		} else {
			siteUser.setRole(UserRole.USER); 
			authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
		}

		this.userRepository.save(siteUser);

		return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
	}
}