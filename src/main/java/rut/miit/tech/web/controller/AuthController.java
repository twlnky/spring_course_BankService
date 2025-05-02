package rut.miit.tech.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import rut.miit.tech.web.domain.dto.ClientCreationDto;
import rut.miit.tech.web.domain.model.Bank;
import rut.miit.tech.web.domain.model.Client;
import rut.miit.tech.web.domain.util.convertor.DtoConverter;
import rut.miit.tech.web.domain.util.validation.ClientRegistrationValidator;
import rut.miit.tech.web.service.bank.BankService;
import rut.miit.tech.web.service.client.ClientService;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final BankService bankService;
    private final ClientRegistrationValidator clientRegistrationValidator;
    private final ClientService clientService;
    private final DtoConverter dtoConverter;

    @GetMapping("/login")
    public String getLoginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(Model model) {
        model.addAttribute("client", new ClientCreationDto());
        model.addAttribute("banks",bankService.getAll());
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String postRegistration(@Validated @ModelAttribute("client") ClientCreationDto client,
                                   BindingResult errors, Model model, Map map) {

        clientRegistrationValidator.validate(client, errors);
        if(errors.hasErrors()) {
            model.addAttribute("banks",bankService.getAll());
            return "auth/registration";
        }
        Client mappedClientModel = dtoConverter.toModel(client, Client.class);
        Bank clientsBank = new Bank();
        clientsBank.setCode(client.getBank());
        mappedClientModel.setBank(clientsBank);
        mappedClientModel.setEnable(true);
        clientService.create(mappedClientModel);

        return "redirect:auth/login?confirm";
    }

}
