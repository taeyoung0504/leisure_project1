package com.project.leisure.taeyoung.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

}
