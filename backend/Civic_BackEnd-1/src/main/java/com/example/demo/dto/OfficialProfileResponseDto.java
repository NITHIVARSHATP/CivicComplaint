package com.example.demo.dto;

import com.example.demo.enums.OfficialProfileLevel;
import lombok.Data;

@Data
public class OfficialProfileResponseDto {
    private Long id;
    private Long userId;
    private String fullName;     // Fetched from User entity
    private String email;        // Fetched from User entity
    private String departmentName; // Fetched from User -> Department
    private String employeeId;
    private OfficialProfileLevel profileLevel;
    private boolean isAvailable;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public OfficialProfileLevel getProfileLevel() {
		return profileLevel;
	}
	public void setProfileLevel(OfficialProfileLevel profileLevel) {
		this.profileLevel = profileLevel;
	}
	public boolean isAvailable() {
		return isAvailable;
	}
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
    
}