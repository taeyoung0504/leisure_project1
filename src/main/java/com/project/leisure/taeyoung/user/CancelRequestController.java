package com.project.leisure.taeyoung.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        // Retrieve the CancelRequest entity by its ID
        CancelRequest cancelRequest = cancelRequestService.getCancelRequestById(id);
        if (cancelRequest == null) {
            return ResponseEntity.notFound().build();
        }

        // Update the "result" field to "1" (assuming "1" means rejected)
        cancelRequest.setResult("1");

        // Save the updated entity
        CancelRequest updatedCancelRequest = cancelRequestService.saveCancelRequest(cancelRequest);
        return ResponseEntity.ok(updatedCancelRequest);
    }
}
