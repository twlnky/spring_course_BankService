package rut.miit.tech.web.service.operation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rut.miit.tech.web.domain.model.Card;
import rut.miit.tech.web.domain.model.Operation;
import rut.miit.tech.web.repository.MessageRepository;
import rut.miit.tech.web.repository.OperationRepository;
import rut.miit.tech.web.service.util.FilterUnit;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.QueryBuilder;
import rut.miit.tech.web.service.util.SortUnit;

import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor
public class OperationServiceImp implements OperationService {
    private final OperationRepository operationRepository;
    private final QueryBuilder queryBuilder;

    @Override
    public Operation getById(Long id) {
        return operationRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public PageResult<List<Operation>> getAll(int page, int pageSize, List<FilterUnit> filters, SortUnit sort) {
        return PageResult.of(queryBuilder.getAll(page, pageSize, filters, sort, Operation.class),
                queryBuilder.getPageCount(pageSize, filters, Operation.class));
    }

    @Override
    public Operation create(Operation card) {
        return operationRepository.save(card);
    }

    @Override
    public Operation update(Operation card) {
        return operationRepository.save(card);
    }

    @Override
    public void delete(Long id) {
        operationRepository.deleteById(id);
    }
}
