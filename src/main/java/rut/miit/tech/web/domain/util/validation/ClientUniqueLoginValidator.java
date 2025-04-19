package rut.miit.tech.web.domain.util.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import rut.miit.tech.web.repository.ClientRepository;
import rut.miit.tech.web.repository.EmployeeRepository;

public class ClientUniqueLoginValidator implements Validator {
    private ClientRepository clientRepository;
    private EmployeeRepository employeeRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(ClientCreationDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //TODO logic of unique check login -> clients and empoyees
        ClientCreationDto dto = (ClientCreationDto) target;
        if(clientRepository.existsByLogin(dto.getLogin()))
        {
            errors.rejectValue("login", null, "Login already exists");
        }

    }
}
