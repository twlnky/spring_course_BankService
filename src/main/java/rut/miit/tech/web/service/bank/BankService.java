package rut.miit.tech.web.service.bank;

import rut.miit.tech.web.domain.model.Bank;
import rut.miit.tech.web.service.util.FilterUnit;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.SortUnit;

import java.util.List;

public interface BankService {
    Bank getById(Long id);
    PageResult<List<Bank>> getAll(int page, int pageSize, List<FilterUnit> filters, SortUnit sort);
    Bank create(Bank card);
    Bank update(Bank card);
    void delete(Long id);
}
