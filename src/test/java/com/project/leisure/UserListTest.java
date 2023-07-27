package com.project.leisure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.leisure.taeyoung.user.UserService;
import com.project.leisure.yuri.product.AccommodationService;

@SpringBootTest
class UserListTest {
	@Autowired
	private UserService userService;
	@Autowired
	private AccommodationService accommodationService;

	
	@Test
	void testCreateUsers() {
		for (int i = 1; i <= 5; i++) {
            String username = String.format("user%03d", i);
            String email = String.format("test%d@example.com", i);
            String password = "password";
            String addr1 = "우편번호";
            String addr2 = "주소";
            String addr3 = "상세주소";
            

            userService.create(username, email, password, addr1, addr2, addr3); // Modified line
        }

	}

	
	
		
		
	
	
	

}
