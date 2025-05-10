package rut.miit.tech.web.service.operation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rut.miit.tech.web.domain.model.Account;
import rut.miit.tech.web.domain.model.Operation;
import rut.miit.tech.web.domain.model.OperationResult;
import rut.miit.tech.web.repository.OperationRepository;
import rut.miit.tech.web.service.account.AccountService;
import rut.miit.tech.web.service.util.FilterUnit;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.QueryBuilder;
import rut.miit.tech.web.service.util.SortUnit;

import java.math.BigDecimal;
import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor
public class OperationServiceImp implements OperationService {
    private final OperationRepository operationRepository;
    private final QueryBuilder queryBuilder;
    private final AccountService accountService;

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
    public OperationResult doOperation(Operation operation) {
        Account account = accountService.getByCardId(operation.getCard().getId());
        BigDecimal balance = account.getBalance();
        BigDecimal newBalance = BigDecimal.valueOf(balance.doubleValue());
        switch (operation.getType()) {
            case DEPOSIT -> {
                BigDecimal additionAmount = operation
                        .getAmount()
                        .multiply(BigDecimal.valueOf(1.0 - operation.getCommission().doubleValue()/100));
                newBalance = newBalance.add(additionAmount);
                account.setBalance(newBalance);
                accountService.update(account);
            }
            case WITHDRAW -> {
                BigDecimal subtractSum = operation
                        .getAmount()
                        .multiply(BigDecimal.valueOf(1.0 - operation.getCommission().doubleValue()/100));
                newBalance = newBalance.subtract(subtractSum);
                account.setBalance(newBalance);
                accountService.update(account);
            }
            case CHECK_BALANCE -> {
                operation.setAmount(newBalance);
            }
        }
        operationRepository.save(operation);
        return new OperationResult(newBalance);
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
