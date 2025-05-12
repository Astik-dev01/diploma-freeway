package com.example.freeway.db.repository.specification;

import com.example.freeway.db.entity.SysRole;
import com.example.freeway.model.role.filter.RoleFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class RoleSpecification implements Specification<SysRole> {

    private final RoleFilter filter;


    @Override
    public Predicate toPredicate(Root<SysRole> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (!filter.getDeleted()) {
            predicates.add(cb.isFalse(root.get("deleted")));
        } else {
            predicates.add(cb.isTrue(root.get("deleted")));
        }

//        if (Boolean.TRUE.equals(filter.getRegistration())) {
//            predicates.add(root.get("alias").in(List.of("FARMER", "SUPPLIER")));
//        }


        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
