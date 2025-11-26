package com.example.demo.controller;

import com.example.demo.dto.FeedBackRequestDto;
import com.example.demo.dto.FeedBackResponseDto;
import com.example.demo.service.FeedBackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "*")
public class FeedBackController {

    @Autowired
    private FeedBackService feedBackService;

    // POST: Submit feedback for a complaint
    @PostMapping
    public ResponseEntity<FeedBackResponseDto> submitFeedback(@Valid @RequestBody FeedBackRequestDto request) {
        FeedBackResponseDto response = feedBackService.submitFeedback(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET: Get feedback for a specific complaint
    // Endpoint: /api/feedback/complaint/1
    @GetMapping("/complaint/{complaintId}")
    public ResponseEntity<FeedBackResponseDto> getFeedbackByComplaintId(@PathVariable Long complaintId) {
        return ResponseEntity.ok(feedBackService.getFeedbackByComplaintId(complaintId));
    }
}