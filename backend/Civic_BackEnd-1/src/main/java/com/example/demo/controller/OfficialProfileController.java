package com.example.demo.controller;

import com.example.demo.dto.OfficialProfileRequestDto;
import com.example.demo.dto.OfficialProfileResponseDto;
import com.example.demo.service.OfficialProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/official-profiles")
@CrossOrigin(origins = "*")
public class OfficialProfileController {

    @Autowired
    private OfficialProfileService profileService;

    // POST: Create a new profile for a user
    @PostMapping
    public ResponseEntity<OfficialProfileResponseDto> createProfile(@RequestBody OfficialProfileRequestDto request) {
        OfficialProfileResponseDto createdProfile = profileService.createProfile(request);
        return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);
    }

    // GET: Get profile by ID
    @GetMapping("/{id}")
    public ResponseEntity<OfficialProfileResponseDto> getProfileById(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.getProfileById(id));
    }

    // GET: Get profile by User ID (e.g., /api/official-profiles/user/5)
    @GetMapping("/user/{userId}")
    public ResponseEntity<OfficialProfileResponseDto> getProfileByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(profileService.getProfileByUserId(userId));
    }

    // PUT: Update profile details
    @PutMapping("/{id}")
    public ResponseEntity<OfficialProfileResponseDto> updateProfile(@PathVariable Long id, @RequestBody OfficialProfileRequestDto request) {
        return ResponseEntity.ok(profileService.updateProfile(id, request));
    }

    // PATCH: Toggle Availability (On Duty / Off Duty)
    // e.g., /api/official-profiles/1/toggle-availability
    @PatchMapping("/{id}/toggle-availability")
    public ResponseEntity<OfficialProfileResponseDto> toggleAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.toggleAvailability(id));
    }
}