package com.example.demo.dto;

import com.example.demo.enums.ComplaientStatus;
import lombok.Data;

@Data
public class ComplaintHistoryRequestDto {
    private Long complaintId;
    private Long changedByUserId;
    private ComplaientStatus previousStatus;
    private ComplaientStatus newStatus;
    private String comment;
	public Long getComplaintId() {
		return complaintId;
	}
	public void setComplaintId(Long complaintId) {
		this.complaintId = complaintId;
	}
	public Long getChangedByUserId() {
		return changedByUserId;
	}
	public void setChangedByUserId(Long changedByUserId) {
		this.changedByUserId = changedByUserId;
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
    
}