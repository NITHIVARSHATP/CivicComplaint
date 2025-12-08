package com.example.demo.dto;

import com.example.demo.enums.OfficialProfileLevel;
import lombok.Data;

@Data
public class OfficialProfileRequestDto {
    private Long userId; // To link this profile to a specific User
    private String employeeId;
    private OfficialProfileLevel profileLevel;
    private boolean isAvailable;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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