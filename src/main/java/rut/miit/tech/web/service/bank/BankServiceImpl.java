package rut.miit.tech.web.service.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rut.miit.tech.web.domain.model.Bank;
import rut.miit.tech.web.repository.BankRepository;
import rut.miit.tech.web.service.util.*;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {
    private final BankRepository bankRepository;
    private final QueryBuilder queryBuilder;

    @Override
    public List<Bank> getAll() {
        return bankRepository.findAll();
    }

    @Override
    public Bank getById(String code) {
        List<Bank> banks = bankRepository.findByCode(code);
        if (banks.isEmpty()) {
            throw new RuntimeException("Bank with code " + code + " not found");
        }
        return banks.get(0);
    }


    @Override
    public PageResult<List<Bank>> getAll(int page, int pageSize, List<FilterUnit> filters, SortUnit sort) {
        return PageResult.of(queryBuilder.getAll(page, pageSize, filters, sort, Bank.class),
                queryBuilder.getPageCount(pageSize, filters, Bank.class));
    }

    @Override
    public PageResult<List<Bank>> getAll(int page, int pageSize, CriteriaFilter<Bank> filters, SortUnit sort) {
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
    public void deleteByCode(String code) {
        bankRepository.deleteById(code);
    }


}
