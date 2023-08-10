package com.project.leisure.taeyoung.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CancelRequestService {
	private final CancelRequestRepository cancelRequestRepository;

	@Autowired
	public CancelRequestService(CancelRequestRepository cancelRequestRepository) {
		this.cancelRequestRepository = cancelRequestRepository;
	}

	public CancelRequest saveCancelRequest(CancelRequest cancelRequest) {
		return cancelRequestRepository.save(cancelRequest);
	}

	public List<CancelRequest> getCancleReq() {
		Sort sort = Sort.by(Sort.Direction.DESC, "id"); // 취소 요청 건이 최신순으로 보이도록 정렬
		return cancelRequestRepository.findAll(sort);
	}
	
	 public CancelRequest getCancelRequestById(Long id) {
	        return cancelRequestRepository.findById(id).orElse(null);
	    }
}
