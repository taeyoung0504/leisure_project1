package com.project.leisure.taeyoung.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.leisure.dogyeom.booking.BookRepository;
import com.project.leisure.dogyeom.booking.BookService;
import com.project.leisure.dogyeom.booking.BookingVO;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
	
	@Autowired
	protected MockMvc mockMvc; 
	
	@Autowired
	protected ObjectMapper objectMapper; // 직렬화, 역직렬화를 위한 메서드를 사용하기 위한 클래스
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	BookService bookService;
	
	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	UserService userService;

	@BeforeEach
	void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context) // 내가 자동주입 받은 context 객체를 만든다.
				.build();
	}

	@DisplayName("removeAndDelete : 조회/저장/삭제 성공")
	@Test
	public void test() throws Exception{
		final String url = "/user/mypage/my_booking_del/{id}";
		 final int idToDelete = 103; 
		final ResultActions result = mockMvc.perform(
				get(url, idToDelete));
		result.andExpect(status().isOk());
		List<BookingVO> bookingvo = bookRepository.findAll();
		assertThat(bookingvo.size()).isEqualTo(42);
	}

}
