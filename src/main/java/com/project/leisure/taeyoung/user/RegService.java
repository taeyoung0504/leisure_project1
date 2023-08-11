package com.project.leisure.taeyoung.user;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RegService {
	private final RegRepository regRepository;

	public RegPartner create(String reg_username, String company_name, String company_address, String partner_name,
			String partner_tel, String partner_sectors, String partner_region, MultipartFile file) {
		RegPartner reg = new RegPartner();
		reg.setCompany_name(company_name);
		reg.setReg_username(reg_username);
		reg.setCompany_address(company_address);
		reg.setPartner_name(partner_name);
		reg.setPartner_tel(partner_tel);
		reg.setPartner_sectors(partner_sectors);
		reg.setPartner_region(partner_region);

		if (file != null && !file.isEmpty()) {
			String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
			String fileName = System.currentTimeMillis() + "_" + originalFilename;

			String uploadDir = "src/main/resources/static/img/partner_regi_img/";
			Path uploadPath = Path.of(uploadDir);

			try {
				Files.createDirectories(uploadPath);
				Path filePath = uploadPath.resolve(fileName);
				Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
				reg.setOrigFilename(originalFilename);
				reg.setFilename(fileName);
				reg.setFilePath(filePath.toString());
			} catch (IOException e) {
			
				e.printStackTrace();
			}
		}
		regRepository.save(reg);
		return reg;

	}

	public List<RegPartner> getList() {
		return regRepository.findAll();
	}


}