package com.example.demo.controller;

import com.example.demo.dto.ComplaintHistoryRequestDto;
import com.example.demo.dto.ComplaintHistoryResponseDto;
import com.example.demo.service.ComplaintHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaint-history")
@CrossOrigin(origins = "*")
public class ComplaintHistoryController {

    @Autowired
    private ComplaintHistoryService historyService;

    // GET: Fetch timeline for a specific complaint
    // Endpoint: /api/complaint-history/complaint/1
    @GetMapping("/complaint/{complaintId}")
    public ResponseEntity<List<ComplaintHistoryResponseDto>> getHistoryByComplaint(@PathVariable Long complaintId) {
        List<ComplaintHistoryResponseDto> history = historyService.getHistoryByComplaintId(complaintId);
        return ResponseEntity.ok(history);
    }

    // POST: Manually add a log entry (Optional)
    // Useful for adding remarks/comments without changing status
    @PostMapping
    public ResponseEntity<ComplaintHistoryResponseDto> addHistoryLog(@RequestBody ComplaintHistoryRequestDto request) {
        ComplaintHistoryResponseDto newLog = historyService.createHistoryLog(request);
        return ResponseEntity.ok(newLog);
    }
}