package com.example.freeway.db.repository;

import com.example.freeway.db.entity.FreeVisitApplication;
import com.example.freeway.db.enums.FreeVisitStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FreeVisitApplicationRepository extends JpaRepository<FreeVisitApplication, Long>, JpaSpecificationExecutor<FreeVisitApplication> {
    Page<FreeVisitApplication> findAllByStatusAndIdNotIn(FreeVisitStatus freeVisitStatus, List<Long> approvedApplicationIds, Pageable pageable);

    List<FreeVisitApplication> findAllByStudentId(Long id);
}
