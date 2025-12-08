package com.example.demo.dto;



import com.example.demo.enums.ComplaientStatus; // Using your spelling
import com.example.demo.enums.ComplaintPriority;
import lombok.Data;
import java.time.Instant;

@Data
public class ComplaintResponseDto {
    private Long id;
    private String title;
    private String description;
    private ComplaientStatus status;
    private ComplaintPriority priority;
    private String category;
    private String imageUrl;
    private String submittedByUserName;
    private String assignedOfficialName;
    
    // Sending back simple coordinates
    private double latitude;
    private double longitude;
    
    private Instant createdAt;
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ComplaientStatus getStatus() {
		return status;
	}

	public void setStatus(ComplaientStatus status) {
		this.status = status;
	}

	public ComplaintPriority getPriority() {
		return priority;
	}

	public void setPriority(ComplaintPriority priority) {
		this.priority = priority;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getSubmittedByUserName() {
		return submittedByUserName;
	}

	public void setSubmittedByUserName(String submittedByUserName) {
		this.submittedByUserName = submittedByUserName;
	}

	public String getAssignedOfficialName() {
		return assignedOfficialName;
	}

	public void setAssignedOfficialName(String assignedOfficialName) {
		this.assignedOfficialName = assignedOfficialName;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	
}
