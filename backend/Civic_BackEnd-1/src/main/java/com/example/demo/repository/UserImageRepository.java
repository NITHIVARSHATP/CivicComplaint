package com.example.demo.repository;



import com.example.demo.models.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    // custom queries if needed
}

