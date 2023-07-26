package com.project.leisure.yuri.product;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.leisure.taeyoung.user.RegPartner;
import com.project.leisure.taeyoung.user.UserRepository;
import com.project.leisure.taeyoung.user.Users;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/acc")
public class AccommodationController {

	
	@GetMapping("/productPartnerList")
	@ResponseBody
	public String productPartnerList(Principal principal) {
	    String username = principal.getName(); // 현재 로그인 한 유저 이름

	    System.out.println(username);

	    return "Hello, " + username;
	}

	
}
