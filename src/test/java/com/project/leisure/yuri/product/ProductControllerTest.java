//package com.project.leisure.yuri.product;
//
//
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.List;
//
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@SpringBootTest // 테스트용 애플리케이션 컨텍스트
//@AutoConfigureMockMvc // MockMvc 생성 -> 굳이 포스트맨 이용할 필요 없음.
//class ProductControllerTest {
//	
//	@Autowired
//	protected MockMvc mockMvc;
//	
//	@Autowired
//	protected ObjectMapper objectMapper;
//	
//	@Autowired
//	private WebApplicationContext context;
//	
//	@Autowired
//	ProductRepository productRepository;
//	
//	@Autowired
//	AccommodationRepository accommodationRepository;
//	
//	@BeforeAll
//	static void setUpBeforeClass() throws Exception {
//	}
//
//	@AfterAll
//	static void tearDownAfterClass() throws Exception {
//	}
//
//	@BeforeEach
//	public void mockMvcSetup() {
//		this.mockMvc = MockMvcBuilders.webAppContextSetup(context) 
//				.build();
//		accommodationRepository.deleteAll();
//	}
//	
//	@DisplayName("AddAccoms : 숙소 추가 성공")
//	@Test
//	void AddAccoms() throws Exception {
//		// given - 블로그 글 추가에 필요한 요청 객체 생성
//		final String url = "/productNewMainReg";
//		final String username = "이강인";
//		final String acc_name = "자고가 호텔";
//		final String acc_img = "고맙다.png";
//		final String acc_sectors = "테스트 본문";
//		final int acc_max_people = 4;
//		final Accommodation accomRequest = new Accommodation(username, acc_name, acc_img,
//				acc_sectors, acc_max_people);
//		
//		// 객체 > JSON으로 직렬화
//		final String requestBody = objectMapper.writeValueAsString(accomRequest); // Exception 전가
//		
//		// when - 블로그 글 추가 API에 요청을 보냄
//		// 요청 타입 JSON, given절에서 만들어둔 객체를 본문으로 보냄
//		// 포스트맨 안쓰면 다음처럼 쓴다.
//		ResultActions result = mockMvc.perform(post(url) // post방식으로 정해둔 url에 요청
//				.contentType(MediaType.APPLICATION_JSON_VALUE) // 요청 보낼 본문 타입 JSON
//				.content(requestBody)); // content는 requestBody에 담은 것
//		// perform - 브라우저에서 서버에 URL 요청을 하듯 컨트롤러를 실행시킬 수 있는 메서드
//		// HTTP Method 요청 방식과 매핑되는 get(), post(), put(), delete() 메서드 제공
//		// 브라우저가 HTTP 요청 프로토콜에 요청 관련 정보를 설정하듯, 메서드를 이용하여 다양한 정보들을 설정할 수 있다. -? 쌤이 준 pdf에 있는 내용
//		// 요청 후 응답 결과를 검증할 수 있는 ResultActions 객체를 반환
//		// 응답 결과 검증 - andExpect()
//		
//		// then - 응답 코드가 201 Create인지 확인
//		result.andExpect(status().isCreated());
//		
//		// 내가 설정한 값이 잘들어 갔는지 확인
//		List<Accommodation> accoms = accommodationRepository.findAll();
//		assertThat(accoms.size()).isEqualTo(1);
//		assertThat(accoms.get(0).getUsername()).isEqualTo(username);
//		assertThat(accoms.get(0).getAcc_name()).isEqualTo(acc_name);
//		assertThat(accoms.get(0).getAcc_img()).isEqualTo(acc_img);
//		assertThat(accoms.get(0).getAcc_sectors()).isEqualTo(acc_sectors);
//		assertThat(accoms.get(0).getAcc_max_people()).isEqualTo(acc_max_people);
//		
//		
//	}
//	
//	
//
//	@AfterEach
//	void tearDown() throws Exception {
//	}
//
////	@Test
////	void test() {
////		IntStream.rangeClosed(1, 10).forEach(i -> {
////			Accommodation accom = Accommodation.builder()
////					.acc_name("test")
////					.acc_sectors("1234")
////					.acc_max_people(i)
////					.build();
////			accommodationRepository.save(accom);
////		});
////	}
//
//}
