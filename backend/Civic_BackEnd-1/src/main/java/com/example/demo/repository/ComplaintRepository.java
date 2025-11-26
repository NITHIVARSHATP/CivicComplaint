package com.example.demo.repository;

import com.example.demo.models.Complaint;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findBySubmittedByUser(User user);
    List<Complaint> findByAssignedToOfficial(User official);
}