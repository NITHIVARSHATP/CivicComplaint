package com.example.demo.service;

import com.example.demo.dto.UserRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.models.Department;
import com.example.demo.models.User;
import com.example.demo.models.UserImage;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.UserImageRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder; // optional
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserImageRepository userImageRepository;

    // If you have Spring Security configured, a PasswordEncoder bean (BCrypt) should be available.
    // If not available, leave it null (we fall back to storing plain password — not recommended).
    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    // --- CREATE / REGISTER (JSON) ---
    public UserResponseDto createUser(UserRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());

        // role: request.getRole() should already map to your enum or be converted elsewhere
        user.setRole(request.getRole());

        // Password handling
        if (passwordEncoder != null) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        } else {
            // NOTE: Plain text fallback — replace with PasswordEncoder in production
            user.setPasswordHash(request.getPassword());
        }

        // Link Department if provided
        if (request.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
            user.setDepartment(dept);
        }

        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }

    // --- CREATE / REGISTER with photo (multipart, store image in DB) ---
    public UserResponseDto createUserWithPhoto(UserRequestDto request, MultipartFile photo) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());

        if (passwordEncoder != null) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        } else {
            user.setPasswordHash(request.getPassword());
        }

        if (request.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
            user.setDepartment(dept);
        }

        // Save user first (so we have an ID to link image to, if desired)
        User savedUser = userRepository.save(user);

        // Handle photo saving into DB (UserImage entity)
        if (photo != null && !photo.isEmpty()) {
            try {
                UserImage img = new UserImage();
                String original = StringUtils.cleanPath(photo.getOriginalFilename());
                img.setFilename(original);
                img.setContentType(photo.getContentType() == null ? "application/octet-stream" : photo.getContentType());
                img.setSize(photo.getSize());
                img.setData(photo.getBytes());
                img.setCreatedAt(Instant.now());

                // Link to user
                img.setUser(savedUser);
                UserImage savedImg = userImageRepository.save(img);

                // Depending on mapping, set image reference on user and save
                savedUser.setImage(savedImg);
                savedUser = userRepository.save(savedUser);

            } catch (IOException ex) {
                // rollback by deleting saved user? For simplicity, throw error
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store photo", ex);
            }
        }

        return mapToDto(savedUser);
    }

    // --- Attach or replace image for existing user ---
    public UserResponseDto saveUserImageToDb(Long userId, MultipartFile photo) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (photo == null || photo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Photo is empty");
        }

        try {
            // If an image already exists, remove it (or replace)
            UserImage img = user.getImage();
            if (img != null) {
                userImageRepository.delete(img);
            }

            UserImage newImg = new UserImage();
            String original = StringUtils.cleanPath(photo.getOriginalFilename());
            newImg.setFilename(original);
            newImg.setContentType(photo.getContentType() == null ? "application/octet-stream" : photo.getContentType());
            newImg.setSize(photo.getSize());
            newImg.setData(photo.getBytes());
            newImg.setCreatedAt(Instant.now());
            newImg.setUser(user);

            UserImage savedImg = userImageRepository.save(newImg);
            user.setImage(savedImg);
            userRepository.save(user);

            return mapToDto(user);

        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to read photo", ex);
        }
    }

    // --- GET ALL ---
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // --- GET ONE ---
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return mapToDto(user);
    }

    // --- UPDATE ---
    public UserResponseDto updateUser(Long id, UserRequestDto request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setFullName(request.getFullName());

        if (request.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
            user.setDepartment(dept);
        }

        // Optionally update password/email if you intend to
        return mapToDto(userRepository.save(user));
    }

    // --- DELETE ---
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(id);
    }

    // --- LOGIN ---
    public UserResponseDto login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        User user = optionalUser.get();

        if (passwordEncoder != null) {
            if (!passwordEncoder.matches(password, user.getPasswordHash())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
            }
        } else {
            // Plain comparison (not secure) — replace with encoder
            if (!user.getPasswordHash().equals(password)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
            }
        }

        return mapToDto(user);
    }

    // --- Fetch image data for user ---
    public UserImage getImageByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        UserImage img = user.getImage();
        if (img == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User image not found");
        }
        return img;
    }

    // --- HELPER: Map Entity to DTO ---
    private UserResponseDto mapToDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        if (user.getDepartment() != null) {
            dto.setDepartmentName(user.getDepartment().getName());
        }

        if (user.getImage() != null) {
            dto.setImageId(user.getImage().getId()); // optional field in response DTO
            // Do not include binary data in DTO
        }

        return dto;
    }
}
