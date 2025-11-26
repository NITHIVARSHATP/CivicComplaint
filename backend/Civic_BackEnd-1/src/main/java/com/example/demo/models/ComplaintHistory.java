package com.example.demo.models;



import com.example.demo.enums.ComplaientStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;

@Data
@Entity
@Table(name = "complaint_history")
public class ComplaintHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complaint_id", nullable = false)
    private Complaint complaint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by_user_id")
    private User changedByUser;

    @Enumerated(EnumType.STRING)
    private ComplaientStatus previousStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaientStatus newStatus;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @CreationTimestamp
    private Instant changedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Complaint getComplaint() {
		return complaint;
	}

	public void setComplaint(Complaint complaint) {
		this.complaint = complaint;
	}

	public User getChangedByUser() {
		return changedByUser;
	}

	public void setChangedByUser(User changedByUser) {
		this.changedByUser = changedByUser;
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
