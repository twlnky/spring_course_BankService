package rut.miit.tech.web.service.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rut.miit.tech.web.domain.model.Bank;
import rut.miit.tech.web.domain.model.Card;
import rut.miit.tech.web.repository.BankRepository;
import rut.miit.tech.web.repository.CardRepository;
import rut.miit.tech.web.service.util.FilterUnit;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.QueryBuilder;
import rut.miit.tech.web.service.util.SortUnit;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {
    private final BankRepository bankRepository;
    private final QueryBuilder queryBuilder;
    @Override
    public Bank getById(Long id) {
        return bankRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public PageResult<List<Bank>> getAll(int page, int pageSize, List<FilterUnit> filters, SortUnit sort) {
        return PageResult.of(queryBuilder.getAll(page, pageSize, filters, sort, Bank.class),
                queryBuilder.getPageCount(pageSize, filters, Bank.class));
    }

    @Override
    public Bank create(Bank card) {
        return bankRepository.save(card);
    }

    @Override
    public Bank update(Bank card) {
        return bankRepository.save(card);
    }

    @Override
    public void delete(Long id) {
        bankRepository.deleteById(id);
    }
}
