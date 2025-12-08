package com.example.demo.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class DepartmentDto {

    private Long id;
    @NotEmpty(message = "Department name is required")
    private String name;

    private String description;
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}