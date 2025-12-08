package com.example.demo.models;



import com.example.demo.enums.ComplaintPriority;



import com.example.demo.enums.ComplaientStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.Instant;
import java.util.Set;

@Data
@Entity
@Table(name = "complaints")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

   

	@Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaientStatus status = ComplaientStatus.SUBMITTED;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaintPriority priority = ComplaintPriority.MEDIUM;

    private String category;
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by_user_id", nullable = false)
    private User submittedByUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_official_id")
    private User assignedToOfficial;
/*
    // This is the magic mapping for PostGIS
    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false)
    private Point location;
*/
    @Column(nullable = true)
    private Double latitude;
    @Column(nullable = true)
    private Double longitude;

    @OneToOne(mappedBy = "complaint", cascade = CascadeType.ALL, orphanRemoval = true)
    private FeedBack feedback;
    
    @OneToMany(mappedBy = "complaint", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("changedAt ASC")
    private Set<ComplaintHistory> historyLogs;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
    
    
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

	public User getSubmittedByUser() {
		return submittedByUser;
	}

	public void setSubmittedByUser(User submittedByUser) {
		this.submittedByUser = submittedByUser;
	}

	public User getAssignedToOfficial() {
		return assignedToOfficial;
	}

	public void setAssignedToOfficial(User assignedToOfficial) {
		this.assignedToOfficial = assignedToOfficial;
	}

	

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public FeedBack getFeedback() {
		return feedback;
	}

	public void setFeedback(FeedBack feedback) {
		this.feedback = feedback;
	}

	public Set<ComplaintHistory> getHistoryLogs() {
		return historyLogs;
	}

	public void setHistoryLogs(Set<ComplaintHistory> historyLogs) {
		this.historyLogs = historyLogs;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
}
