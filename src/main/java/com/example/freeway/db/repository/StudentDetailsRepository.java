package com.example.freeway.db.repository;

import com.example.freeway.db.entity.StudentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentDetailsRepository extends JpaRepository<StudentDetails, Long> , JpaSpecificationExecutor<StudentDetails> {
    Optional<StudentDetails> findByUserId(Long userId);

}
