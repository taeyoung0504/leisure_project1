package com.project.leisure.taeyoung.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cancel-requests")
public class CancelRequestController {
    private final CancelRequestService cancelRequestService;

    @Autowired
    public CancelRequestController(CancelRequestService cancelRequestService) {
        this.cancelRequestService = cancelRequestService;
    }

   
    @PostMapping("/submit")
    public ResponseEntity<?> submitCancelRequest(@RequestBody CancelRequest cancelRequest) {
        CancelRequest savedCancelRequest = cancelRequestService.saveCancelRequest(cancelRequest);
        return ResponseEntity.ok(savedCancelRequest);
    }
    
  
    @PostMapping("/reject/{id}")
    public ResponseEntity<?> rejectCancelRequest(@PathVariable Long id) {
        
        CancelRequest cancelRequest = cancelRequestService.getCancelRequestById(id);
        if (cancelRequest == null) {
            return ResponseEntity.notFound().build();
        }

        // 결제 취소 요청 결과 _ 1: 거절  
        cancelRequest.setResult("1");

        // Save the updated entity
        CancelRequest updatedCancelRequest = cancelRequestService.saveCancelRequest(cancelRequest);
        return ResponseEntity.ok(updatedCancelRequest);
    }
}