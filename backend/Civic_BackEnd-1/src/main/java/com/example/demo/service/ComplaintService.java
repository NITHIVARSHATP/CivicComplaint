package com.example.demo.service;


import com.example.demo.dto.ComplaintRequestDto;
import com.example.demo.dto.ComplaintResponseDto;
import com.example.demo.enums.ComplaientStatus;
import com.example.demo.enums.ComplaintPriority;
import com.example.demo.models.Complaint;
import com.example.demo.models.User;
import com.example.demo.repository.ComplaintRepository;
import com.example.demo.repository.UserRepository; // Assuming you have this
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;

   
    // --- CREATE ---
    public ComplaintResponseDto createComplaint(ComplaintRequestDto request) {
        Complaint complaint = new Complaint();
        complaint.setTitle(request.getTitle());
        complaint.setDescription(request.getDescription());
        complaint.setCategory(request.getCategory());
        complaint.setImageUrl(request.getImageUrl());
        complaint.setStatus(ComplaientStatus.SUBMITTED);
        complaint.setPriority(ComplaintPriority.MEDIUM); // Default

        // 1. Link User
        User user = userRepository.findById(request.getSubmittedByUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        complaint.setSubmittedByUser(user);

     /*   // 2. Convert Lat/Lon to PostGIS Point
        Point point = geometryFactory.createPoint(new Coordinate(request.getLongitude(), request.getLatitude()));
        complaint.setLocation(point);*/
        
        complaint.setLongitude(null);
        complaint.setLatitude(null);

        Complaint savedComplaint = complaintRepository.save(complaint);
        return mapToDto(savedComplaint);
    }

    // --- READ ALL ---
    public List<ComplaintResponseDto> getAllComplaints() {
        return complaintRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // --- READ ONE ---
    public ComplaintResponseDto getComplaintById(Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Complaint not found"));
        return mapToDto(complaint);
    }

    // --- UPDATE STATUS ---
    public ComplaintResponseDto updateStatus(Long id, ComplaientStatus newStatus) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Complaint not found"));
        
        complaint.setStatus(newStatus);
        return mapToDto(complaintRepository.save(complaint));
    }

    // --- ASSIGN OFFICIAL ---
    public ComplaintResponseDto assignOfficial(Long complaintId, Long officialId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Complaint not found"));
        
        User official = userRepository.findById(officialId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Official not found"));

        complaint.setAssignedToOfficial(official);
        complaint.setStatus(ComplaientStatus.ASSIGNED); // Auto-update status
        
        return mapToDto(complaintRepository.save(complaint));
    }

    // --- HELPER: Entity to DTO ---
    private ComplaintResponseDto mapToDto(Complaint complaint) {
        ComplaintResponseDto dto = new ComplaintResponseDto();
        dto.setId(complaint.getId());
        dto.setTitle(complaint.getTitle());
        dto.setDescription(complaint.getDescription());
        dto.setStatus(complaint.getStatus());
        dto.setPriority(complaint.getPriority());
        dto.setCategory(complaint.getCategory());
        dto.setImageUrl(complaint.getImageUrl());
        dto.setCreatedAt(complaint.getCreatedAt());

        if (complaint.getSubmittedByUser() != null) {
            dto.setSubmittedByUserName(complaint.getSubmittedByUser().getFullName()); // Assuming User has getFullName
        }
        
        if (complaint.getAssignedToOfficial() != null) {
            dto.setAssignedOfficialName(complaint.getAssignedToOfficial().getFullName());
        }

        // Convert PostGIS Point back to Lat/Lon for the frontend
     /*   if (complaint.getLocation() != null) {
            dto.setLatitude(complaint.getLocation().getY());
            dto.setLongitude(complaint.getLocation().getX());
        }*/
        dto.setLatitude(0);
        dto.setLongitude(0);
        return dto;
    }
}
