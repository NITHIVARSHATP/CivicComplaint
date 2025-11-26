package com.example.demo.repository;

import com.example.demo.models.ComplaintHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintHistoryRepository extends JpaRepository<ComplaintHistory, Long> {

    // Find a history record by the email of the user who changed it (nested property)
    Optional<ComplaintHistory> findByChangedByUserEmail(String email);

    // Existence check by the email of the user who changed it
    boolean existsByChangedByUserEmail(String email);

    // All history entries for a complaint
    List<ComplaintHistory> findByComplaintId(Long complaintId);

    // All history entries for a complaint ordered by change time (most useful)
    List<ComplaintHistory> findByComplaintIdOrderByChangedAtDesc(Long complaintId);
}
