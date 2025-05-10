package rut.miit.tech.web.service.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rut.miit.tech.web.domain.exception.ResourceNotFountException;
import rut.miit.tech.web.domain.model.Account;
import rut.miit.tech.web.repository.AccountRepository;
import rut.miit.tech.web.repository.CardRepository;
import rut.miit.tech.web.repository.OperationRepository;
import rut.miit.tech.web.service.util.*;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImp implements AccountService{
    private final AccountRepository accountRepository;
    private final QueryBuilder queryBuilder;
    private final CardRepository cardRepository;
    private final OperationRepository operationRepository;

    @Override
    public Account getById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new ResourceNotFountException("account with id = %d not found".formatted(id)));
    }

    @Override
    public Account getByCardId(Long cardId) {
        return accountRepository.findByCardId(cardId).orElseThrow(() -> new ResourceNotFountException("account by card_id = %d not found".formatted(cardId)));
    }

    @Override
    public PageResult<List<Account>> getAll(int page, int pageSize, List<FilterUnit> filters, SortUnit sort) {
        return PageResult.of(queryBuilder.getAll(page, pageSize, filters, sort, Account.class),
                queryBuilder.getPageCount(pageSize, filters, Account.class));
    }

    @Override
    public PageResult<List<Account>> getAll(int page, int pageSize, CriteriaFilter<Account> filter, SortUnit sort) {
        return PageResult.of(queryBuilder.getAll(page, pageSize, filter, sort, Account.class),
                queryBuilder.getPageCount(pageSize, filter, Account.class));
    }

    @Override
    public Account create(Account card) {
        return accountRepository.save(card);
    }

    @Override
    public void updateBlock(Long id, boolean block) {
        Integer cardCountUpdated = cardRepository.updateAllIsBlockedById(id,block);
        Integer accountCountUpdated = accountRepository.updateIsBlockedById(id,block);
        log.info("Updated block status cards - {}, updated block status account - {}",cardCountUpdated,accountCountUpdated);
    }

    @Override
    public Account update(Account card) {
        return accountRepository.save(card);
    }

    @Override
    public void delete(Long id) {
        cardRepository.deleteAllByAccountId(id);
        accountRepository.deleteById(id);
    }

}
