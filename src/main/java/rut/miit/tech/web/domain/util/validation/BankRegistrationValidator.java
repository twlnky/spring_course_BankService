package rut.miit.tech.web.domain.util.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import rut.miit.tech.web.domain.dto.BankCreationDto;
import rut.miit.tech.web.repository.BankRepository;

@Component
@RequiredArgsConstructor
public class BankRegistrationValidator implements Validator {
    private final BankRepository bankRepository;
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(BankCreationDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
