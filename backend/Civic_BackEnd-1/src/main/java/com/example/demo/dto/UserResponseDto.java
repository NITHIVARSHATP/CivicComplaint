package com.example.demo.dto;

import com.example.demo.enums.UserRoles;
import lombok.Data;
import java.time.Instant;

@Data
public class UserResponseDto {
    private Long id;
    private String fullName;
    private String email;
    private UserRoles role;
    private String departmentName; // easier to read than a full Department object
    private Instant createdAt;
    private Long imageId; 
	public Long getImageId() {
		return imageId;
	}
	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public UserRoles getRole() {
		return role;
	}
	public void setRole(UserRoles role) {
		this.role = role;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public Instant getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	
    
    // We NEVER include passwordHash here for security!
}