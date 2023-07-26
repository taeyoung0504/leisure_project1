package com.project.leisure.taeyoung.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//회원DB 접근 레퍼지토리
@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
	Optional<Users> findByusername(String username);

	List<Users> findByUsername(String username);

	Optional<Users> findByEmail(String email);

	@Query("SELECT u FROM Users u WHERE u.username = :username AND u.email = :email")
	Optional<Users> findByUsernameAndEmail(@Param("username") String username, @Param("email") String email);

	List<Users> findByNickname(String nickname);
	
	Optional<Users> findById(Long id);
	

	@SuppressWarnings("unchecked")
	@Override
	Users save(Users user); // saveUser 메서드 추가
	

    // 회원 상태(islock)를 조회하는 메서드
	@Query("SELECT u.islock FROM Users u WHERE u.username = :username")
    int findIslockByUsername(@Param("username") String username);
	
	
	
	Page<Users> findAll(Pageable pageable);
	Page<Users> findAll(Specification<Users> spec, Pageable pageable);

	Users findFirstByUsername(String regUsername);

	void save(List<Users> user);
}

