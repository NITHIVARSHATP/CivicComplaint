package com.example.demo.service;

import com.example.demo.dto.FeedBackRequestDto;
import com.example.demo.dto.FeedBackResponseDto;
import com.example.demo.models.Complaint;
import com.example.demo.models.FeedBack;
import com.example.demo.models.User;
import com.example.demo.repository.ComplaintRepository;
import com.example.demo.repository.FeedBackRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FeedBackService {

    @Autowired
    private FeedBackRepository feedBackRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;

    // --- SUBMIT FEEDBACK ---
    public FeedBackResponseDto submitFeedback(FeedBackRequestDto request) {
        // 1. Check if feedback already exists for this complaint (One-to-One rule)
        if (feedBackRepository.existsByComplaintId(request.getComplaintId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Feedback already submitted for this complaint.");
        }

        // 2. Fetch Complaint
        Complaint complaint = complaintRepository.findById(request.getComplaintId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Complaint not found"));

        // 3. Fetch User
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Optional Logic: You might want to verify that the user giving feedback 
        // is the same user who submitted the complaint.
        // if (!complaint.getSubmittedByUser().getId().equals(user.getId())) {
        //     throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the creator can give feedback.");
        // }

        FeedBack feedBack = new FeedBack();
        feedBack.setComplaint(complaint);
        feedBack.setUser(user);
        feedBack.setRating(request.getRating());
        feedBack.setComment(request.getComment());

        FeedBack savedFeedBack = feedBackRepository.save(feedBack);
        return mapToDto(savedFeedBack);
    }

    // --- GET FEEDBACK BY COMPLAINT ID ---
    public FeedBackResponseDto getFeedbackByComplaintId(Long complaintId) {
        FeedBack feedBack = feedBackRepository.findByComplaintId(complaintId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No feedback found for this complaint"));
        return mapToDto(feedBack);
    }

    // --- HELPER: Map Entity to DTO ---
    private FeedBackResponseDto mapToDto(FeedBack entity) {
        FeedBackResponseDto dto = new FeedBackResponseDto();
        dto.setId(entity.getId());
        dto.setComplaintId(entity.getComplaint().getId());
        dto.setComplaintTitle(entity.getComplaint().getTitle());
        dto.setUserName(entity.getUser().getFullName());
        dto.setRating(entity.getRating());
        dto.setComment(entity.getComment());
        dto.setSubmittedAt(entity.getSubmittedAt());
        return dto;
    }
}