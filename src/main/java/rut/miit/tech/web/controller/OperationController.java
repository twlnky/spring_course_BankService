package rut.miit.tech.web.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import rut.miit.tech.web.domain.dto.OperationDTO;
import rut.miit.tech.web.domain.model.Operation;
import rut.miit.tech.web.domain.model.OperationResult;
import rut.miit.tech.web.domain.util.convertor.DtoConverter;
import rut.miit.tech.web.domain.util.validation.OperationValidator;
import rut.miit.tech.web.service.operation.OperationService;

@Controller
@RequestMapping("/operations")
@RequiredArgsConstructor
public class OperationController {
    private final OperationValidator validator;
    private final OperationService operationService;
    private final DtoConverter dtoConverter;

    @PostMapping
    public String doOperation(@Validated @ModelAttribute OperationDTO dto,
                              BindingResult errors,
                              Model model) {
        validator.validate(dto, errors);
        if(errors.hasErrors()) {
            return "atms/atm";//TODO
        }
        OperationResult result = operationService.doOperation(
                dtoConverter.toModel(dto, Operation.class)
        );
        model.addAttribute("hasResult", true);
        model.addAttribute("operationResult", result);
        return "atms/atm";
    }


}
