package com.project.leisure.son.notice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Service
public class NoticeService {
	private final NoticeRepository noticeRepository;

	public NoticeService(NoticeRepository noticeRepository) {
		this.noticeRepository = noticeRepository;
	}

	public void createNotice(Notice notice) {
		noticeRepository.save(notice);
	}

	public Page<Notice> getAllNotices(Pageable pageable) {
		return noticeRepository.findAllByOrderByCreateDateDesc(pageable);
	}

	public Notice getNotice(Integer id) {
		return noticeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid notice Id:" + id));
	}

//	public void updateNotice(Integer id, Notice updatedNotice) {
//		Notice notice = getNotice(id);
//		notice.setTitle(updatedNotice.getTitle());
//		notice.setContent(updatedNotice.getContent());
//		noticeRepository.save(notice);
//	}
	
	// 이미지를 저장할 경로를 아래 변수에 지정합니다.
    private static final String UPLOAD_DIR = "src/main/resources/static/img/notice_img/";
	
    public void updateNotice(Integer id, Notice updatedNotice, @RequestParam("image") MultipartFile imageFile) {
        Notice notice = getNotice(id);
        notice.setTitle(updatedNotice.getTitle());
        notice.setContent(updatedNotice.getContent());
        
        // 이미지 업로드와 이미지 경로 저장을 처리합니다.
        if (!imageFile.isEmpty()) {
            try {
                String imageName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path imagePath = Paths.get(UPLOAD_DIR + imageName);
                Files.createDirectories(imagePath.getParent());
                Files.write(imagePath, imageFile.getBytes());
                String imagePathString = imageName;
                notice.setImagePath(imagePathString);
            } catch (IOException e) {
                e.printStackTrace();
                // 필요에 따라 예외를 처리할 수 있습니다.
            }
        }

        noticeRepository.save(notice);
    }

	
	public void deleteNotice(Integer id) {
	    noticeRepository.deleteById(id);
	}
	
	public String uploadImage(MultipartFile imageFile) {
	    try {
	        // 이미지가 저장될 경로
	        String uploadDir = "src/main/resources/static/img/notice_img/";

	        // 업로드할 이미지 파일의 확장자
	        String fileExtension = Objects.requireNonNull(imageFile.getOriginalFilename()).substring(imageFile.getOriginalFilename().lastIndexOf("."));

	        // 이미지 파일의 새로운 이름 생성 (UUID를 이용하여 중복 방지)
	        String newFileName = UUID.randomUUID().toString() + fileExtension;

	        // 이미지를 저장할 디렉토리 생성
	        File directory = new File(uploadDir);
	        if (!directory.exists()) {
	            directory.mkdirs();
	        }

	        // 이미지 파일을 서버에 저장
	        java.nio.file.Path filePath = Paths.get(uploadDir, newFileName);
	        Files.write(filePath, imageFile.getBytes());

	        // 업로드된 이미지의 경로 반환
	        return uploadDir + newFileName;
	    } catch (IOException e) {
	        e.printStackTrace();
	        // 이미지 업로드 실패 시 기본 이미지 경로를 반환하거나 오류 처리를 해야합니다.
	        return "default_image_path.jpg"; // 예시로 기본 이미지 경로를 반환하도록 설정
	    }
	}


}
