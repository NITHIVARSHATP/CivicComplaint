package com.example.demo.models;


import com.example.demo.enums.UserRoles;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRoles role;

   

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
	
    private Department department;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private OfficialProfile officialProfile;

    @OneToMany(mappedBy = "submittedByUser")
    private Set<Complaint> submittedComplaints;
    
    @OneToMany(mappedBy = "assignedToOfficial")
    private Set<Complaint> assignedComplaints;
    

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
    
 // inside User class (add imports if needed)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "image_id")
   
    private UserImage image;

    // add getter/setter
    public UserImage getImage() { return image; }
    public void setImage(UserImage image) {
        this.image = image;
        if (image != null) image.setUser(this); // keep bidirectional in sync
    }

    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public UserRoles getRole() {
		return role;
	}

	public void setRole(UserRoles role) {
		this.role = role;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public OfficialProfile getOfficialProfile() {
		return officialProfile;
	}

	public void setOfficialProfile(OfficialProfile officialProfile) {
		this.officialProfile = officialProfile;
	}

	public Set<Complaint> getSubmittedComplaints() {
		return submittedComplaints;
	}

	public void setSubmittedComplaints(Set<Complaint> submittedComplaints) {
		this.submittedComplaints = submittedComplaints;
	}

	public Set<Complaint> getAssignedComplaints() {
		return assignedComplaints;
	}

	public void setAssignedComplaints(Set<Complaint> assignedComplaints) {
		this.assignedComplaints = assignedComplaints;
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
