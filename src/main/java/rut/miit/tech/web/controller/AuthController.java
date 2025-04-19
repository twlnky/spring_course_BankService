package rut.miit.tech.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import rut.miit.tech.web.domain.model.Client;

@Controller
@RequestMapping("/auth")
public class AuthController {


    @GetMapping("/login")
    public String getLoginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(Model model) {
        model.addAttribute("client", new Client());
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String postRegistration(@Validated @ModelAttribute("client") Client client,
                                   BindingResult errors) {
        //TODO - 1 На форме добавить все поля клиента
        //       2 В методе получения страницы через модель внедрять список всех доступных банков
        //       3 Создать класс ClientCreationDto - except id, + passwordConfirm
        //       4 Validation
        //       5 Create Validator and check login unique
        //
        if(errors.hasErrors()) {
            return "auth/registration";
        }
        return "redirect:auth/login?confirm";
    }

}
