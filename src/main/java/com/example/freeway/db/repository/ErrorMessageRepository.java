package com.example.freeway.db.repository;

import com.example.freeway.db.entity.ErrorMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorMessageRepository extends JpaRepository<ErrorMessage, String> {
}
