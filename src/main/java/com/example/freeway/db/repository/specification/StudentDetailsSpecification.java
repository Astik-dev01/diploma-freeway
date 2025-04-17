package com.example.freeway.db.repository.specification;

import com.example.freeway.db.entity.StudentDetails;
import com.example.freeway.model.studentDetails.filter.StudentDetailsFilterRequestDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class StudentDetailsSpecification implements Specification<StudentDetails> {

    private final StudentDetailsFilterRequestDto filter;

    @Override
    public Predicate toPredicate(Root<StudentDetails> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getStudentId() != null && !filter.getStudentId().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("studentId")), "%" + filter.getStudentId().toLowerCase() + "%"));
        }

        if (filter.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), filter.getStatus()));
        }

        if (filter.getFacultyId() != null) {
            predicates.add(cb.equal(root.get("faculty").get("id"), filter.getFacultyId()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
