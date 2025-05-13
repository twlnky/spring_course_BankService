package rut.miit.tech.web.service.atm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rut.miit.tech.web.domain.dto.OperationDTO;
import rut.miit.tech.web.domain.model.Account;
import rut.miit.tech.web.domain.model.Atm;
import rut.miit.tech.web.repository.AtmRepository;
import rut.miit.tech.web.service.account.AccountService;
import rut.miit.tech.web.service.util.*;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AtmServiceImpl implements AtmService {
    private final AtmRepository atmRepository;
    private final QueryBuilder queryBuilder;
    private final AccountService accountService;



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

    @Override
    public String doOperation(OperationDTO dto) throws IllegalAccessException {
        Account account = accountService.getByCardId(dto.getCardId());
        BigDecimal.valueOf(dto.getAmount().doubleValue());
        BigDecimal newBalance;
        String result = "Операция завершена";
        switch (dto.getType()) {
            case DEPOSIT -> {
                newBalance = account.getBalance().add(BigDecimal.valueOf(dto.getAmount().doubleValue()));
                account.setBalance(newBalance);
                result += ", баланс после пополнения " + newBalance;
            }
            case WITHDRAW -> {
                if (account.getBalance().compareTo(BigDecimal.valueOf(dto.getAmount().doubleValue())) < 0) throw new IllegalAccessException("Недостаточно средств");
                newBalance = account.getBalance().subtract(BigDecimal.valueOf(dto.getAmount().doubleValue()));
                account.setBalance(newBalance);
                result += ", баланс после снятия " + newBalance;
            }
            case CHECK_BALANCE -> {
                newBalance = account.getBalance();
                account.setBalance(newBalance);
                result += ", ваш баланс " + newBalance;
            }
            default -> throw new IllegalAccessException("Неизвестный тип операции");
        }
        accountService.update(account);
        return result;
    }
}
