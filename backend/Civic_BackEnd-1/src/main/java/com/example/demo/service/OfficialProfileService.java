package com.example.demo.service;

import com.example.demo.dto.OfficialProfileRequestDto;
import com.example.demo.dto.OfficialProfileResponseDto;
import com.example.demo.models.OfficialProfile;
import com.example.demo.models.User;
import com.example.demo.repository.OfficialProfileRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfficialProfileService {

    @Autowired
    private OfficialProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    // --- CREATE PROFILE ---
    public OfficialProfileResponseDto createProfile(OfficialProfileRequestDto request) {
        // 1. Check if User exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // 2. Check if User already has a profile (One-to-One constraint)
        if (profileRepository.findByUserId(request.getUserId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user already has an official profile.");
        }

        // 3. Check if Employee ID is unique
        if (profileRepository.existsByEmployeeId(request.getEmployeeId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee ID already exists.");
        }

        OfficialProfile profile = new OfficialProfile();
        profile.setUser(user);
        profile.setEmployeeId(request.getEmployeeId());
        profile.setProfileLevel(request.getProfileLevel());
        profile.setAvailable(request.isAvailable());

        return mapToDto(profileRepository.save(profile));
    }

    // --- GET PROFILE BY ID ---
    public OfficialProfileResponseDto getProfileById(Long id) {
        OfficialProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));
        return mapToDto(profile);
    }

    // --- GET PROFILE BY USER ID ---
    // Useful when the user logs in and you want to fetch their official details
    public OfficialProfileResponseDto getProfileByUserId(Long userId) {
        OfficialProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found for this user"));
        return mapToDto(profile);
    }

    // --- UPDATE PROFILE ---
    public OfficialProfileResponseDto updateProfile(Long id, OfficialProfileRequestDto request) {
        OfficialProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));

        profile.setEmployeeId(request.getEmployeeId());
        profile.setProfileLevel(request.getProfileLevel());
        profile.setAvailable(request.isAvailable());
        
        return mapToDto(profileRepository.save(profile));
    }
    
    // --- TOGGLE AVAILABILITY ---
    // Quick method for officials to go "On Duty" or "Off Duty"
    public OfficialProfileResponseDto toggleAvailability(Long id) {
        OfficialProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));
        
        profile.setAvailable(!profile.isAvailable());
        return mapToDto(profileRepository.save(profile));
    }

    // --- HELPER: Map Entity to DTO ---
    private OfficialProfileResponseDto mapToDto(OfficialProfile entity) {
        OfficialProfileResponseDto dto = new OfficialProfileResponseDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser().getId());
        dto.setFullName(entity.getUser().getFullName());
        dto.setEmail(entity.getUser().getEmail());
        dto.setEmployeeId(entity.getEmployeeId());
        dto.setProfileLevel(entity.getProfileLevel());
        dto.setAvailable(entity.isAvailable());

        // Add Department Name if available
        if (entity.getUser().getDepartment() != null) {
            dto.setDepartmentName(entity.getUser().getDepartment().getName());
        }

        return dto;
    }
}