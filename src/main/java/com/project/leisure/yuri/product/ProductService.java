package com.project.leisure.yuri.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductImgRepository productImgRepository;
	private final AccommodationRepository accommodationRepository;

	// Product에 대한 모든 것을 출력
	public List<Product> getList() {
		return this.productRepository.findAll();
	}

	// ProductImg에 대한 모든 것을 출력
	public List<ProductImg> getImgList() {
		return this.productImgRepository.findAll();
	}

	// 해당 객실에 있는 상품들을 조회
	public List<Product> findProductsByAccommodationId(Long acc_id) {

		Optional<Accommodation> accommodation = accommodationRepository.findById(acc_id);

		if (accommodation.isPresent()) {
			return accommodation.get().getProducts();

		} else {
			return Collections.emptyList();
		}
	}

	// 해당 product_id로 상품의 정보를 조회
	public Product getProduct(Long product_id) {
		Optional<Product> findProduct = this.productRepository.findById(product_id);
		if (findProduct.isPresent()) {
			Product product = findProduct.get();

			return product;
		} else {
			return null;
		}
	}

	// 이미지와 product PK를 가져와서 이미지 URL 및 product 연결, 저장
	public Integer updateImg(MultipartFile product_photo, Long product_id) {

		Optional<Product> productOptional = this.productRepository.findById(product_id);

		if (productOptional.isPresent()) {
			Product product = productOptional.get();

			try {
				UUID uuid = UUID.randomUUID();

				// 파일을 저장할 경로와 파일명 설정
				String fileName = uuid.toString() + "_" + product_photo.getOriginalFilename(); // 원본 파일 이름을 반환

				// 이미지 파일 경로 설정 (절대 경로로 수정 필요)
				String filePath = "src/main/resources/static/img/product_img/" + fileName;

				// 파일 저장
				Path targetPath = Path.of(filePath);
				Files.copy(product_photo.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

				ProductImg productImg = new ProductImg();

				// 이미지와 상품 연결
				productImg.setProduct(product);

				// 이미지 경로 DB 저장
				productImg.setImg_url("/img/product_img/" + fileName);

				// 이미지를 저장한다
				this.productImgRepository.save(productImg);

				// 상품 정보 저장
				this.productRepository.save(product);

			} catch (IOException e) {
				// 예외 처리
				e.printStackTrace();
				return 0; // 예외 발생 시 0을 반환하도록 수정
			}
		}

		return 1; // 이미지 저장 성공 시 1을 반환하도록 수정
	}

	// 상품 등록 기능 구현
	public Long pdCreate(String product_type, String product_detail, Integer product_count, Integer product_amount,
			Integer product_pernum, LocalTime product_checkin, LocalTime product_checkout,
			Accommodation accommodation) {
		Product product = new Product();
		product.setProduct_type(product_type);
		product.setProduct_detail(product_detail);
		product.setProduct_count(product_count);
		product.setProduct_amount(product_amount);
		product.setProduct_pernum(product_pernum);
		product.setCheckin(product_checkin);
		product.setCheckout(product_checkout);
		product.setAccommodation(accommodation);

		this.productRepository.save(product);

		return product.getProduct_id();
	}

	// 상품 수정 기능 구현
	public Integer updateProduct(Long product_id, String product_type, String product_detail, Integer product_count,
			Integer product_amount, Integer product_pernum, LocalTime product_checkin, LocalTime product_checkout) {
		Optional<Product> productOptional = productRepository.findById(product_id);

		if (productOptional.isPresent()) {
			Product product = productOptional.get();

			product.setProduct_type(product_type);
			product.setProduct_detail(product_detail);
			product.setProduct_count(product_count);
			product.setProduct_amount(product_amount);
			product.setProduct_pernum(product_pernum);
			product.setCheckin(product_checkin);
			product.setCheckout(product_checkout);

			productRepository.save(product);

			return 1; // 성공
		}
		return 0; // 실패
	}

	// 상품 전체 삭제
	public Integer pdDelete(Long productId) {
		Optional<Product> productOptional = productRepository.findById(productId);
		if (productOptional.isPresent()) {
			Product product = productOptional.get();

			List<ProductImg> productImgs = product.getProductImgs();
			for (ProductImg productImg : productImgs) {
				deleteImage(productImg);
				productImgRepository.delete(productImg);
			}

			productRepository.delete(product);

			return 1; // 성공
		}

		return 0; // 실패
	}

	// 해당 이미지 삭제기능
	public void deleteImage(ProductImg productImg) {
		String baseDirectory = "src/main/resources/static/";
		String imgPath = productImg.getImg_url();
		String fileImgPath = baseDirectory + imgPath;

		try {
			Path imagePath = Paths.get(fileImgPath);
			Files.delete(imagePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 상품 수정시 이미지 삭제
	public void pdEditImgDelete(List<Long> deletedImageIds) {
		for (Long imageId : deletedImageIds) {
			Optional<ProductImg> productImgOptional = productImgRepository.findById(imageId);
			if (productImgOptional.isPresent()) {
				ProductImg productImg = productImgOptional.get();
				deleteImage(productImg);
				productImgRepository.delete(productImg);
			}
		}
	}

	// 자신의 숙소의 상품이 맞는지 확인
	public boolean isOwnerOfAccommodationProduct(String username, Product product) {

		if (username.equals(product.getAccommodation().getUsername())) {
			return true;
		}
		return false;
	}

}