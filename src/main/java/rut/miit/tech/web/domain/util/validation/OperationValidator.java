package rut.miit.tech.web.domain.util.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import rut.miit.tech.web.domain.dto.OperationDTO;
import rut.miit.tech.web.domain.model.Account;
import rut.miit.tech.web.domain.model.OperationType;
import rut.miit.tech.web.service.account.AccountService;
import rut.miit.tech.web.service.card.CardService;

@Component
@RequiredArgsConstructor
public class OperationValidator implements Validator {
    private final CardService cardService;
    private final AccountService accountService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(OperationDTO.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        OperationDTO dto = (OperationDTO) target;
        if(dto.getAmount().doubleValue() <= 0){
            errors.rejectValue("amount", "error.amount.negative","Amount must be positive");
            return;
        }
        if(dto.getType() == OperationType.DEPOSIT){
            return;
        }
        Account account = accountService.getByCardId(dto.getCardId());
        double opAmount = dto.getAmount().doubleValue();
        double diff = account.getBalance().doubleValue() - opAmount;
        if(diff < 0){
            errors.rejectValue("amount", "error.amount.negative","Amount could not be more than account balance");
        }
    }
}
