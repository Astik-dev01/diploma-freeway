package com.example.freeway.db.repository.specification;

import com.example.freeway.db.entity.FreeVisitApplication;
import com.example.freeway.model.freeVisitApplication.FreeVisitApplicationFilterRequestDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class FreeVisitApplicationSpecification implements Specification<FreeVisitApplication> {

    private final FreeVisitApplicationFilterRequestDto filter;

    public FreeVisitApplicationSpecification(FreeVisitApplicationFilterRequestDto filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<FreeVisitApplication> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getStudentId() != null) {
            predicates.add(cb.equal(root.get("student").get("id"), filter.getStudentId()));
        }

        if (filter.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), filter.getStatus()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
