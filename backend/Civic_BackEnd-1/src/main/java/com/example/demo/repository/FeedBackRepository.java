package com.example.demo.repository;



import com.example.demo.models.FeedBack;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
public interface FeedBackRepository extends JpaRepository<FeedBack, Long> {
	
    Optional<FeedBack> findByComplaintId(Long complaintId);
    
    // Check if feedback already exists for a complaint
    boolean existsByComplaintId(Long complaintId);
}
