package rut.miit.tech.web.service.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rut.miit.tech.web.domain.model.Card;
import rut.miit.tech.web.service.card.CardSort;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class QueryBuilder {
    private final EntityManager entityManager;


    public <T> List<T> getAll(CriteriaFilter<T> filter, SortUnit sort, Class<T> rootType) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(rootType);
        Root<T> root = query.from(rootType);
        //Фильтрация
        query.where(filter.mapToPredicates(criteriaBuilder, root));
        switch (sort.getOrder()){
            case ASC -> query.orderBy(criteriaBuilder.asc(root.get(sort.getField())));
            case DESC -> query.orderBy(criteriaBuilder.desc(root.get(sort.getField())));
        }
        return  entityManager.createQuery(query).getResultList();
    }

    public <T> List<T> getAll(int page, int pageSize, CriteriaFilter<T> filter, SortUnit sort, Class<T> rootType) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(rootType);
        Root<T> root = query.from(rootType);
        //Фильтрация
        query.where(filter.mapToPredicates(criteriaBuilder, root));
        switch (sort.getOrder()){
            case ASC -> query.orderBy(criteriaBuilder.asc(root.get(sort.getField())));
            case DESC -> query.orderBy(criteriaBuilder.desc(root.get(sort.getField())));

        }
        //Пагинация
        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(page * pageSize);
        typedQuery.setMaxResults(pageSize);
        return typedQuery.getResultList();
    }

    public <T> Long getPageCount(int pageSize, CriteriaFilter<T> filter, Class<T> rootType) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<T> root = query.from(rootType);
        //SELECT count(*) FROM cards
        query.select(criteriaBuilder.count(root));
        //WHERE predicate1 AND predicated2;
        query.where(filter.mapToPredicates(criteriaBuilder, root));
        //Пагинация
        TypedQuery<Long> typedQuery = entityManager.createQuery(query);
        Long count = typedQuery.getSingleResult();
        return count / pageSize + (count % pageSize == 0 ? 0 : 1);

    }

    public <T> List<T> getAll(int page, int pageSize, List<FilterUnit> filters, SortUnit sort, Class<T> rootType) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(rootType);
        Root<T> root = query.from(rootType);
        //Фильтрация
        List<Predicate> predicates = new ArrayList<>();
        for (FilterUnit filter : filters) {
            Predicate p = this.buildPredicate(filter, root, criteriaBuilder);
            predicates.add(p);
        }
        if(!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }
        switch (sort.getOrder()){
            case ASC -> query.orderBy(criteriaBuilder.asc(root.get(sort.getField())));
            case DESC -> query.orderBy(criteriaBuilder.desc(root.get(sort.getField())));

        }
        //Пагинация
        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(page * pageSize);
        typedQuery.setMaxResults(pageSize);
        return typedQuery.getResultList();
    }

    public <T> Long getPageCount(int pageSize, List<FilterUnit> filters, Class<T> rootType) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<T> root = query.from(rootType);
        //SELECT count(*) FROM cards
        query.select(criteriaBuilder.count(root));
        //WHERE predicate1 AND predicated2;
        List<Predicate> predicates = new ArrayList<>();
        for (FilterUnit filter : filters) {
            Predicate p = this.buildPredicate(filter, root, criteriaBuilder);
            predicates.add(p);
        }
        if(!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }
        //Пагинация
        TypedQuery<Long> typedQuery = entityManager.createQuery(query);
        Long count = typedQuery.getSingleResult();
        return count / pageSize + (count % pageSize == 0 ? 0 : 1);

    }

    public <T> Predicate buildPredicate(FilterUnit filter, Root<T> root, CriteriaBuilder cb) {
        return switch (filter.getOperation()){
            case EQUAL -> cb.equal(root.get(filter.getField()), filter.getValue());
            case LESS -> cb.lessThan(root.get(filter.getField()), (Comparable)filter.getValue());
            case LESS_OR_EQUAL -> cb.lessThanOrEqualTo(root.get(filter.getField()), (Comparable)filter.getValue());
            case GREATER -> cb.greaterThan(root.get(filter.getField()), (Comparable)filter.getValue());
            case GREATER_OR_EQUAL -> cb.greaterThanOrEqualTo(root.get(filter.getField()), (Comparable)filter.getValue());
            case LIKE -> cb.like(root.get(filter.getField()), (String)filter.getValue());
        };
    }
}