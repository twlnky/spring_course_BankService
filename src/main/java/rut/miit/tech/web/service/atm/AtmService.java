package rut.miit.tech.web.service.atm;

import rut.miit.tech.web.domain.dto.OperationDTO;
import rut.miit.tech.web.domain.model.Atm;
import rut.miit.tech.web.service.util.CriteriaFilter;
import rut.miit.tech.web.service.util.FilterUnit;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.SortUnit;

import java.util.List;

public interface AtmService {
    Atm getByCode(String code);
    PageResult<List<Atm>> getAll(int page, int pageSize, List<FilterUnit> filters, SortUnit sort);
    PageResult<List<Atm>> getAll(int page, int pageSize, CriteriaFilter<Atm> filter, SortUnit sort);
    Atm create(Atm card);
    Atm update(Atm card);
    void delete(String code);
    String doOperation(OperationDTO dto) throws IllegalAccessException;
}
