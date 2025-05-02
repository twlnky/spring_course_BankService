package rut.miit.tech.web.domain.util.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import rut.miit.tech.web.domain.dto.ClientCreationDto;
import rut.miit.tech.web.repository.ClientRepository;
import rut.miit.tech.web.repository.EmployeeRepository;

@Component
@RequiredArgsConstructor
public class ClientRegistrationValidator implements Validator {
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(ClientCreationDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //TODO logic of unique check login -> clients and empoyees


        ClientCreationDto dto = (ClientCreationDto) target;

        if(!dto.getPassword().equals(dto.getPasswordConfirm())){
            errors.rejectValue("passwordConfirm", "","Passwords do not match");
        }

        if(clientRepository.existsByLogin(dto.getLogin()) || employeeRepository.existsByLogin(dto.getLogin()))
        {
            errors.rejectValue("login", null, "Login already exists");
        }

        if(clientRepository.existsByEmail(dto.getEmail()))
        {
            errors.rejectValue("email", null, "Email already exists");
        }

    }
}
