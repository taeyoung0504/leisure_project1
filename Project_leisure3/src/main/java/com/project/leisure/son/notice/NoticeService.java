package com.project.leisure.son.notice;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

	public void updateNotice(Integer id, Notice updatedNotice) {
		Notice notice = getNotice(id);
		notice.setTitle(updatedNotice.getTitle());
		notice.setContent(updatedNotice.getContent());
		noticeRepository.save(notice);
	}
	
	public void deleteNotice(Integer id) {
	    noticeRepository.deleteById(id);
	}

}
