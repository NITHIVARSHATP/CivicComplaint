package com.example.demo.repository;



import com.example.demo.models.OfficialProfile;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
public interface OfficialProfileRepository extends JpaRepository<OfficialProfile, Long> {
	
Optional<OfficialProfile> findByUserId(Long userId);
    
    // Check if employee ID already exists
    boolean existsByEmployeeId(String employeeId);
}