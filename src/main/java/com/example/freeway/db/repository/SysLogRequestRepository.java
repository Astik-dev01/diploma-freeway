package com.example.freeway.db.repository;


import com.example.freeway.db.entity.SysLogRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysLogRequestRepository extends JpaRepository<SysLogRequest, Long> {
}
