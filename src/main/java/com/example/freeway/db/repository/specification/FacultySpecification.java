package com.example.freeway.db.repository.specification;

import com.example.freeway.db.entity.Faculty;
import com.example.freeway.model.faculty.FacultyFilterRequestDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class FacultySpecification implements Specification<Faculty> {

    private final FacultyFilterRequestDto filter;

    public FacultySpecification(FacultyFilterRequestDto filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<Faculty> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getName() != null && !filter.getName().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
        }

        if (filter.getDeleted() != null) {
            predicates.add(cb.equal(root.get("deleted"), filter.getDeleted()));
        } else {
            predicates.add(cb.isFalse(root.get("deleted")));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
