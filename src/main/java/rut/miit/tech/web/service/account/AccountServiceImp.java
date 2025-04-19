package rut.miit.tech.web.service.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rut.miit.tech.web.domain.model.Account;
import rut.miit.tech.web.repository.AccountRepository;
import rut.miit.tech.web.service.util.FilterUnit;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.QueryBuilder;
import rut.miit.tech.web.service.util.SortUnit;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImp implements AccountService{
    private final AccountRepository accountRepository;
    private final QueryBuilder queryBuilder;

    @Override
    public Account getById(Long id) {
        return accountRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public PageResult<List<Account>> getAll(int page, int pageSize, List<FilterUnit> filters, SortUnit sort) {
        return PageResult.of(queryBuilder.getAll(page, pageSize, filters, sort, Account.class),
                queryBuilder.getPageCount(pageSize, filters, Account.class));
    }

    @Override
    public Account create(Account card) {
        return accountRepository.save(card);
    }

    @Override
    public Account update(Account card) {
        return accountRepository.save(card);
    }

    @Override
    public void delete(Long id) {
        accountRepository.deleteById(id);
    }
}
