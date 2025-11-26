package com.example.demo.dto;

import com.example.demo.enums.UserRoles;
import lombok.Data;

@Data
public class UserRequestDto {
    private String fullName;
    private String email;
    private String password; // Raw password from frontend
    private UserRoles role;
    private Long departmentId; // Optional: Only for OFFICIALS
   
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public UserRoles getRole() {
		return role;
	}
	public void setRole(UserRoles role) {
		this.role = role;
	}
	public Long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
}