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
		Sort sort = Sort.by(Sort.Direction.DESC, "id"); // Assuming "id" is the field used for sorting
		return cancelRequestRepository.findAll(sort);
	}
	
	 public CancelRequest getCancelRequestById(Long id) {
	        return cancelRequestRepository.findById(id).orElse(null);
	    }
}
