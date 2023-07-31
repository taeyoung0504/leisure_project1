package com.project.leisure.taeyoung.user;

import org.springframework.beans.factory.annotation.Autowired;
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
}
