package com.example.freeway.db.repository.specification;

import com.example.freeway.db.entity.SysUser;
import com.example.freeway.model.user.filter.UserFilterDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SysUserSpecification implements Specification<SysUser> {

    private final UserFilterDto filter;


    @Override
    public Predicate toPredicate(Root<SysUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter != null) {
            if (!filter.getIsBanned()) {
                predicates.add(cb.isFalse(root.get("isBanned")));
            } else {
                predicates.add(cb.isTrue(root.get("isBanned")));
            }


            // Фильтр по ФИО
            if (filter.getFullName() != null && !filter.getFullName().isEmpty()) {
                String[] nameParts = filter.getFullName().toLowerCase().split(" ");
                for (String part : nameParts) {
                    predicates.add(cb.or(
                            cb.like(cb.lower(root.get("secondName")), part + "%"),
                            cb.like(cb.lower(root.get("name")), part + "%"),
                            cb.like(cb.lower(root.get("patronymic")), part + "%")
                    ));
                }
            }

            // Фильтр по роли
            if (filter.getRolesIds() != null && !filter.getRolesIds().isEmpty()) {
                predicates.add(root.get("roles").get("id").in(filter.getRolesIds()));
            }

            // Фильтр удален?
            if (!filter.getDeleted()) {
                predicates.add(cb.isFalse(root.get("deleted")));
            } else {
                predicates.add(cb.isTrue(root.get("deleted")));
            }
        }
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
