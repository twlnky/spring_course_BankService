package rut.miit.tech.web.service.operation;


import rut.miit.tech.web.domain.model.Operation;
import rut.miit.tech.web.domain.model.OperationResult;
import rut.miit.tech.web.service.util.FilterUnit;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.SortUnit;

import java.util.List;

public interface OperationService {
    Operation getById(Long id);
    PageResult<List<Operation>> getAll(int page, int pageSize, List<FilterUnit> filters, SortUnit sort);
    Operation create(Operation card);
    OperationResult doOperation(Operation operation);
    Operation update(Operation card);
    void delete(Long id);
}
