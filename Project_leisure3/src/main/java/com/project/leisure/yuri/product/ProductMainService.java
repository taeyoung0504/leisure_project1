//package com.project.leisure.yuri.product;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.StandardCopyOption;
//import java.util.Optional;
//import java.util.UUID;
//
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.project.leisure.taeyoung.user.RegPartner;
//import com.project.leisure.taeyoung.user.RegRepository;
//import com.project.leisure.taeyoung.user.Users;
//
//import lombok.RequiredArgsConstructor;
//
//@RequiredArgsConstructor
//@Service
//public class ProductMainService {
//
//	private final ProductMainRepository productMainRepository;
//	private final RegRepository regRepository;
//
//	// username으로 RegPartner의 정보를 가져온다
//	public RegPartner getUserNameId(String username) {
//		Optional<RegPartner> findRegId = this.regRepository.findByusername(username);
//		if (findRegId.isPresent()) {
//			RegPartner partner = findRegId.get();
//
//			return partner;
//		}
//		return null;
//	}
//
////	// product Main  해당하는 값들을 저장한다
////	public int updateMainPrduct(String product_main_detail, MultipartFile main_product_img_url, RegPartner regPartner) {
////
////		try {
////			
////			UUID uuid = UUID.randomUUID();
////
////			// 파일을 저장할 경로와 파일명 설정
////			String fileName = uuid.toString() + "_" + main_product_img_url.getOriginalFilename(); // 원본 파일 이름을 반환
////			
////			String filePath = "src/main/resources/static/img/product_main_img/" + fileName;
////
////			// 파일 저장
////			Path targetPath = Path.of(filePath);
////			Files.copy(main_product_img_url.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
////			
////			ProductMain productMain = new ProductMain();
////		
////			productMain.setProduct_main_detail(product_main_detail); 
////			productMain.setRegPartner(regPartner);
////			
////			// 이미지를 저장한 파일 경로 설정
////			productMain.setMain_product_img_url("/img/product_main_img/" + fileName);
////			
////			
////			this.productMainRepository.save(productMain);
////			
////			
////		} catch (IOException e) {
////			// 예외 처리
////			e.printStackTrace();
////		}
////		
////
////		return 1; // 등록 성공
////	}
////	
// 
//	// 해당 이미지가 없는 경우
//	public int updateMainProductWithoutImage(String product_main_detail, RegPartner regPartner) {
//
//		regPartner.getProductMain().setProduct_main_detail(product_main_detail);
//		this.productMainRepository.save(regPartner.getProductMain()); // 데이터베이스 업데이트
//
//		return 1; // 등록 성공
//	}
//
//	// product Main 해당하는 값들을 저장한다
//	public int updateMainPrduct(String product_main_detail, MultipartFile main_product_img_url, RegPartner regPartner) {
//
//		ProductMain productMain = new ProductMain();
//
//		// 해당 regPartner가 productMain에 값이 있는지 없는지 확인
//		if (regPartner.getProductMain() != null) { // 만일 값이 있다면
//
//			// 기존 사장님 한마디를 새로운 내용으로 업데이트
//			regPartner.getProductMain().setProduct_main_detail(product_main_detail);
//
//			// 새로운 사진 업로드 처리
//			try {
//
//				UUID uuid = UUID.randomUUID();
//				String fileName = uuid.toString() + "_" + main_product_img_url.getOriginalFilename();
//				String filePath = "src/main/resources/static/img/product_main_img/" + fileName;
//				Path targetPath = Path.of(filePath);
//				Files.copy(main_product_img_url.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
//
//				// 기존 사진 삭제
//				String oldImagePathStr = regPartner.getProductMain().getMain_product_img_url();
//				if (oldImagePathStr != null && !oldImagePathStr.isEmpty()) {
//					Path oldImagePath = Path.of("src/main/resources/static" + oldImagePathStr);
//					Files.deleteIfExists(oldImagePath);
//				}
//
//				// 기존 사진 경로 업데이트
//				regPartner.getProductMain().setMain_product_img_url("/img/product_main_img/" + fileName);
//
//				// productMain 저장
//				this.productMainRepository.save(regPartner.getProductMain());
//
//				return 1; // 등록 성공
//			} catch (IOException e) {
//				e.printStackTrace();
//				return 0; // 등록 실패
//			}
//
//		}
//
//		
//		// 만약 처음 등록한다면
//		try {
//
//			UUID uuid = UUID.randomUUID();
//
//			// 파일을 저장할 경로와 파일명 설정
//			String fileName = uuid.toString() + "_" + main_product_img_url.getOriginalFilename(); // 원본 파일 이름을 반환
//
//			String filePath = "src/main/resources/static/img/product_main_img/" + fileName;
//
//			// 파일 저장
//			Path targetPath = Path.of(filePath);
//			Files.copy(main_product_img_url.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
//
//			productMain.setProduct_main_detail(product_main_detail);
//			productMain.setRegPartner(regPartner);
//
//			// 이미지를 저장한 파일 경로 설정
//			productMain.setMain_product_img_url("/img/product_main_img/" + fileName);
//
//			this.productMainRepository.save(productMain);
//
//		} catch (IOException e) {
//			// 예외 처리
//			e.printStackTrace();
//		}
//
//		return 1; // 등록 성공
//	}
//
//}
