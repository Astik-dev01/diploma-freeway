package com.example.freeway.db.repository;


import com.example.freeway.db.entity.SysLogAuthorization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysLogAuthorizationRepository extends JpaRepository<SysLogAuthorization, Long> {
}
