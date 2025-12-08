package com.example.demo.service;

import com.example.demo.dto.ComplaintHistoryRequestDto;
import com.example.demo.dto.ComplaintHistoryResponseDto;
import com.example.demo.models.Complaint;
import com.example.demo.models.ComplaintHistory;
import com.example.demo.models.User;
import com.example.demo.repository.ComplaintHistoryRepository;
import com.example.demo.repository.ComplaintRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplaintHistoryService {

    @Autowired
    private ComplaintHistoryRepository historyRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;

    // --- CREATE ENTRY ---
    // This can be called manually via Controller OR internally by ComplaintService
    public ComplaintHistoryResponseDto createHistoryLog(ComplaintHistoryRequestDto request) {
        ComplaintHistory history = new ComplaintHistory();

        // 1. Link Complaint
        Complaint complaint = complaintRepository.findById(request.getComplaintId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Complaint not found"));
        history.setComplaint(complaint);

        // 2. Link User (if provided)
        if (request.getChangedByUserId() != null) {
            User user = userRepository.findById(request.getChangedByUserId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            history.setChangedByUser(user);
        }

        history.setPreviousStatus(request.getPreviousStatus());
        history.setNewStatus(request.getNewStatus());
        history.setComment(request.getComment());

        ComplaintHistory savedHistory = historyRepository.save(history);
        return mapToDto(savedHistory);
    }

    // --- GET HISTORY FOR A SPECIFIC COMPLAINT ---
    public List<ComplaintHistoryResponseDto> getHistoryByComplaintId(Long complaintId) {
        // Assuming your repository has a method: findByComplaintId(Long id)
        // If not, add it to your ComplaintHistoryRepository interface.
        List<ComplaintHistory> logs = historyRepository.findByComplaintId(complaintId);
        
        return logs.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // --- HELPER: Map Entity to DTO ---
    private ComplaintHistoryResponseDto mapToDto(ComplaintHistory entity) {
        ComplaintHistoryResponseDto dto = new ComplaintHistoryResponseDto();
        dto.setId(entity.getId());
        dto.setComplaintId(entity.getComplaint().getId());
        
        if (entity.getChangedByUser() != null) {
            dto.setChangedByUserName(entity.getChangedByUser().getFullName());
            dto.setChangedByUserRole(entity.getChangedByUser().getRole().toString());
        } else {
            dto.setChangedByUserName("System / Automated");
        }

        dto.setPreviousStatus(entity.getPreviousStatus());
        dto.setNewStatus(entity.getNewStatus());
        dto.setComment(entity.getComment());
        dto.setChangedAt(entity.getChangedAt());
        
        return dto;
    }
}