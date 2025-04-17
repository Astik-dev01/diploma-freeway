package com.example.freeway.db.repository;

import com.example.freeway.db.entity.FreeVisitAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreeVisitAttachmentRepository extends JpaRepository<FreeVisitAttachment, Long> {
}
