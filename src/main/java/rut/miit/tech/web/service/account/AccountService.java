package rut.miit.tech.web.service.account;

import rut.miit.tech.web.domain.model.Account;
import rut.miit.tech.web.service.util.FilterUnit;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.SortUnit;
import java.util.List;

public interface AccountService {
    Account getById(Long id);
    PageResult<List<Account>> getAll(int page, int pageSize, List<FilterUnit> filters, SortUnit sort);
    Account create(Account card);
    Account update(Account card);
    void delete(Long id);
}
