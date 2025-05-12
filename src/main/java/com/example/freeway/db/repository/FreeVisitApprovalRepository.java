package com.example.freeway.db.repository;

import com.example.freeway.db.entity.FreeVisitApproval;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface FreeVisitApprovalRepository extends JpaRepository<FreeVisitApproval, Long>, JpaSpecificationExecutor<FreeVisitApproval> {
    boolean existsByApplicationIdAndTeacherId(Long applicationId, Long teacherId);

    List<FreeVisitApproval> findAllByApplicationId(Long applicationId);

    Page<FreeVisitApproval> findAllByTeacherId(Long teacherId, Pageable pageable);

    Optional<FreeVisitApproval> findByApplicationIdAndTeacherId(Long applicationId, Long id);
}
