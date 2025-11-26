package com.example.demo.controller;

import com.example.demo.dto.ComplaintRequestDto;

import com.example.demo.dto.ComplaintResponseDto;
import com.example.demo.enums.ComplaientStatus;
import com.example.demo.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "*") // Allow frontend access
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    // POST: Create a new complaint
    @PostMapping("/add")
    public ResponseEntity<ComplaintResponseDto> createComplaint(@RequestBody ComplaintRequestDto request) {
        ComplaintResponseDto response = complaintService.createComplaint(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET: Get all complaints
    @GetMapping
    public ResponseEntity<List<ComplaintResponseDto>> getAllComplaints() {
        return ResponseEntity.ok(complaintService.getAllComplaints());
    }

    // GET: Get single complaint
    @GetMapping("/{id}")
    public ResponseEntity<ComplaintResponseDto> getComplaintById(@PathVariable Long id) {
        return ResponseEntity.ok(complaintService.getComplaintById(id));
    }

    // PATCH: Update Status (e.g. /api/complaints/1/status?status=RESOLVED)
    @PatchMapping("/{id}/status")
    public ResponseEntity<ComplaintResponseDto> updateStatus(@PathVariable Long id, @RequestParam ComplaientStatus status) {
        return ResponseEntity.ok(complaintService.updateStatus(id, status));
    }

    // PATCH: Assign Official (e.g. /api/complaints/1/assign/5) -> Assigns official with ID 5
    @PatchMapping("/{id}/assign/{officialId}")
    public ResponseEntity<ComplaintResponseDto> assignOfficial(@PathVariable Long id, @PathVariable Long officialId) {
        return ResponseEntity.ok(complaintService.assignOfficial(id, officialId));
    }
}