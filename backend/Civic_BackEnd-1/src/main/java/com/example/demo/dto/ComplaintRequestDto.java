package com.example.demo.dto;


import lombok.Data;

@Data
public class ComplaintRequestDto {
    private String title;
    private String description;
    private String category;
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
	public Long getSubmittedByUserId() {
		return submittedByUserId;
	}
	public void setSubmittedByUserId(Long submittedByUserId) {
		this.submittedByUserId = submittedByUserId;
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
	private String imageUrl;
    private Long submittedByUserId; // We only need the ID to link the user
    
    // Frontend sends simple numbers, Backend converts to Geometry
    private double latitude; 
    private double longitude; 
}
