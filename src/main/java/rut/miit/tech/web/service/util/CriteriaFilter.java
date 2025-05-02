package rut.miit.tech.web.service.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@FunctionalInterface
public interface CriteriaFilter<R> {
    Predicate[] mapToPredicates(CriteriaBuilder cb, Root<R> root);
}
