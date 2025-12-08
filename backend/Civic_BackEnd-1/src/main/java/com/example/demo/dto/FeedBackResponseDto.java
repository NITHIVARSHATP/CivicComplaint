package com.example.demo.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class FeedBackResponseDto {
    private Long id;
    private Long complaintId;
    private String complaintTitle; // Helpful context
    private String userName;       // Who gave the feedback
    private int rating;
    private String comment;
    private Instant submittedAt;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getComplaintId() {
		return complaintId;
	}
	public void setComplaintId(Long complaintId) {
		this.complaintId = complaintId;
	}
	public String getComplaintTitle() {
		return complaintTitle;
	}
	public void setComplaintTitle(String complaintTitle) {
		this.complaintTitle = complaintTitle;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Instant getSubmittedAt() {
		return submittedAt;
	}
	public void setSubmittedAt(Instant submittedAt) {
		this.submittedAt = submittedAt;
	}
    
}