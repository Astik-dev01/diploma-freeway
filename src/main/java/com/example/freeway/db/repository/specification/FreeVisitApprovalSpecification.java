package com.example.freeway.db.repository.specification;

import com.example.freeway.db.entity.FreeVisitApproval;
import com.example.freeway.model.freeVisitApproval.FreeVisitApprovalFilterRequestDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class FreeVisitApprovalSpecification implements Specification<FreeVisitApproval> {

    private final FreeVisitApprovalFilterRequestDto filter;

    public FreeVisitApprovalSpecification(FreeVisitApprovalFilterRequestDto filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<FreeVisitApproval> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getTeacherId() != null) {
            predicates.add(cb.equal(root.get("teacher").get("id"), filter.getTeacherId()));
        }

        if (filter.getApplicationId() != null) {
            predicates.add(cb.equal(root.get("application").get("id"), filter.getApplicationId()));
        }

        if (filter.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), filter.getStatus()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
