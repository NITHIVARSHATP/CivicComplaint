package com.example.demo.dto;

import com.example.demo.enums.ComplaientStatus;
import lombok.Data;
import java.time.Instant;

@Data
public class ComplaintHistoryResponseDto {
    private Long id;
    private Long complaintId;
    private String changedByUserName; // Easier to read than a User ID
    private String changedByUserRole;
    private ComplaientStatus previousStatus;
    private ComplaientStatus newStatus;
    private String comment;
    private Instant changedAt;
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
	public String getChangedByUserName() {
		return changedByUserName;
	}
	public void setChangedByUserName(String changedByUserName) {
		this.changedByUserName = changedByUserName;
	}
	public String getChangedByUserRole() {
		return changedByUserRole;
	}
	public void setChangedByUserRole(String changedByUserRole) {
		this.changedByUserRole = changedByUserRole;
	}
	public ComplaientStatus getPreviousStatus() {
		return previousStatus;
	}
	public void setPreviousStatus(ComplaientStatus previousStatus) {
		this.previousStatus = previousStatus;
	}
	public ComplaientStatus getNewStatus() {
		return newStatus;
	}
	public void setNewStatus(ComplaientStatus newStatus) {
		this.newStatus = newStatus;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Instant getChangedAt() {
		return changedAt;
	}
	public void setChangedAt(Instant changedAt) {
		this.changedAt = changedAt;
	}
    
}