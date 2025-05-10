package rut.miit.tech.web.service.atm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rut.miit.tech.web.domain.model.Atm;
import rut.miit.tech.web.repository.AtmRepository;
import rut.miit.tech.web.service.util.*;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AtmServiceImpl implements AtmService {
    private final AtmRepository atmRepository;
    private final QueryBuilder queryBuilder;


    @Override
    public Atm getByCode(String code) {
        return atmRepository.findById(code).orElseThrow(RuntimeException::new);
    }

    @Override
    public PageResult<List<Atm>> getAll(int page, int pageSize, List<FilterUnit> filters, SortUnit sort) {
        return PageResult.of(queryBuilder.getAll(page, pageSize, filters, sort, Atm.class),
                queryBuilder.getPageCount(pageSize, filters, Atm.class));
    }

    @Override
    public PageResult<List<Atm>> getAll(int page, int pageSize, CriteriaFilter<Atm> filter, SortUnit sort) {
        return PageResult.of(queryBuilder.getAll(page, pageSize, filter, sort, Atm.class),
                queryBuilder.getPageCount(pageSize, filter, Atm.class));
    }

    @Override
    public Atm create(Atm card) {
        return atmRepository.save(card);
    }

    @Override
    public Atm update(Atm card) {
        return atmRepository.save(card);
    }

    @Override
    public void delete(String code) {
        atmRepository.deleteById(code);
    }
}
