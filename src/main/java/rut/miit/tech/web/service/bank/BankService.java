package rut.miit.tech.web.service.bank;

import rut.miit.tech.web.domain.model.Bank;
import rut.miit.tech.web.service.util.CriteriaFilter;
import rut.miit.tech.web.service.util.FilterUnit;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.SortUnit;

import java.util.List;

public interface BankService {
    List<Bank> getAll();
    Bank getById(String code);
    PageResult<List<Bank>> getAll(int page, int pageSize, List<FilterUnit> filters, SortUnit sort);
    PageResult<List<Bank>> getAll(int page, int pageSize, CriteriaFilter<Bank> filters, SortUnit sort);
    Bank create(Bank card);
    Bank update(Bank card);
    void deleteByCode(String code);
}
