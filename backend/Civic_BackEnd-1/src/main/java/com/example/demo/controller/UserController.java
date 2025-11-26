package com.example.demo.controller;

import com.example.demo.dto.UserRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.enums.UserRoles;
import com.example.demo.models.UserImage;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") 
public class UserController {

	 @Autowired
	    private UserService userService;

	    /**
	     * JSON registration endpoint.
	     * Expects JSON body matching UserRequestDto (role is an enum UserRoles).
	     */
	    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<UserResponseDto> registerJson(@RequestBody UserRequestDto request) {
	        UserResponseDto created = userService.createUser(request);
	        return ResponseEntity.status(HttpStatus.CREATED).body(created);
	    }

	    /**
	     * Multipart registration endpoint.
	     * Expects form fields:
	     *  - fullName (String)
	     *  - email (String)
	     *  - password (String)
	     *  - role (String) e.g. "ADMIN", "USER", "OFFICIAL"
	     *  - departmentId (optional) e.g. "3"
	     *  - photo (file) optional
	     */
	    @PostMapping(path = "/register/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	    public ResponseEntity<?> registerWithPhoto(
	            @RequestPart("fullName") String fullName,
	            @RequestPart("email") String email,
	            @RequestPart("password") String password,
	            @RequestPart("role") String roleStr,
	            @RequestPart(value = "departmentId", required = false) String departmentIdStr,
	            @RequestPart(value = "photo", required = false) MultipartFile photo) {

	        // Validate and convert role string -> enum
	        UserRoles role;
	        try {
	            role = UserRoles.valueOf(roleStr.trim().toUpperCase());
	        } catch (IllegalArgumentException | NullPointerException ex) {
	            return ResponseEntity.badRequest().body(Map.of("error", "Invalid role value. Allowed: " + java.util.Arrays.toString(UserRoles.values())));
	        }

	        Long departmentId = null;
	        if (!StringUtils.isEmpty(departmentIdStr)) {
	            try {
	                departmentId = Long.valueOf(departmentIdStr.trim());
	            } catch (NumberFormatException ex) {
	                return ResponseEntity.badRequest().body(Map.of("error", "departmentId must be a number"));
	            }
	        }

	        UserRequestDto dto = new UserRequestDto();
	        dto.setFullName(fullName);
	        dto.setEmail(email);
	        dto.setPassword(password);
	        dto.setRole(role);
	        dto.setDepartmentId(departmentId);

	        try {
	            UserResponseDto created = userService.createUserWithPhoto(dto, photo);
	            return ResponseEntity.status(HttpStatus.CREATED).body(created);
	        } catch (ResponseStatusException ex) {
	            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", ex.getReason()));
	        }
	    }

	    /**
	     * Login endpoint.
	     * Accepts JSON: { "email": "...", "password": "..." }
	     */
	    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
	        String email = payload.get("email");
	        String password = payload.get("password");

	        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
	            return ResponseEntity.badRequest().body(Map.of("error", "email and password are required"));
	        }

	        try {
	            UserResponseDto user = userService.login(email, password);
	            // In future: return JWT token + user DTO. For now return user DTO.
	            return ResponseEntity.ok(user);
	        } catch (ResponseStatusException ex) {
	            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", ex.getReason()));
	        }
	    }

	    /**
	     * Return user's image bytes. Frontend can call:
	     * GET /users/{id}/image
	     */
	    @GetMapping(path = "/users/{id}/image")
	    public ResponseEntity<byte[]> getUserImage(@PathVariable("id") Long id) {
	        try {
	            UserImage img = userService.getImageByUserId(id);
	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.parseMediaType(img.getContentType()));
	            headers.setContentLength(img.getSize());
	            headers.setCacheControl(CacheControl.noCache().getHeaderValue());
	            return new ResponseEntity<>(img.getData(), headers, HttpStatus.OK);
	        } catch (ResponseStatusException ex) {
	            // propagate proper status
	            throw ex;
	        }
	    }

	    /**
	     * Attach or replace image for an existing user.
	     * Multipart: file part named "photo"
	     * Example: POST /users/5/image (multipart/form-data)
	     */
	    @PostMapping(path = "/users/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	    public ResponseEntity<?> uploadUserImage(@PathVariable("id") Long id,
	                                             @RequestPart("photo") MultipartFile photo) {
	        if (photo == null || photo.isEmpty()) {
	            return ResponseEntity.badRequest().body(Map.of("error", "Photo is required"));
	        }

	        try {
	            UserResponseDto updated = userService.saveUserImageToDb(id, photo);
	            return ResponseEntity.ok(updated);
	        } catch (ResponseStatusException ex) {
	            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", ex.getReason()));
	        }
	    }

}