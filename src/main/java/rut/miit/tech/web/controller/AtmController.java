package rut.miit.tech.web.controller;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rut.miit.tech.web.domain.dto.OperationDTO;
import rut.miit.tech.web.domain.model.*;
import rut.miit.tech.web.domain.util.validation.OperationValidator;
import rut.miit.tech.web.service.atm.AtmService;
import rut.miit.tech.web.service.bank.BankService;
import rut.miit.tech.web.service.card.CardService;
import rut.miit.tech.web.service.util.Order;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.SortUnit;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/atms")
@RequiredArgsConstructor
public class AtmController {

    private final CardService cardService;
    private final BankService bankService;
    private final AtmService atmService;
    private final OperationValidator operationValidator;

    @GetMapping("/atm")
    public String atm(Model model) {
        model.addAttribute("hasResult",false);

        return "atms/atm";
    }

    @GetMapping("atms/create")
    public String createAtm(Model model) {
        model.addAttribute("hasResult",false);
        return "atms/create";
    }

    @PostMapping("atms/create")
    public String createAtm(@PathVariable String code, Atm atm) {
        Bank bank = bankService.getById(code);
        atm.setBank(bank);
        atm.setInstallationDate(new Timestamp(System.currentTimeMillis()));
        atmService.create(atm);
        return "atms/create";
    }

    @GetMapping("/atm/update/{id}")
    public String updateAtm(@PathVariable("code") String code, Model model) {
        Atm atm = atmService.getByCode(code);
        model.addAttribute("atm",atm);
        return "atms/update";
    }

    @PostMapping("/atm/update/{id}")
    public String updateAtm(@PathVariable String code,  @ModelAttribute("atm") Atm updatedAtm) {
        Atm atm = atmService.getByCode(code);
        atm.setStatus(updatedAtm.getStatus());
        atm.setLastServiceDate(new Timestamp(System.currentTimeMillis()));
        atmService.update(atm);
        return "atms/update";

    }

    @PostMapping("/atms/delete/{id}")
    public String deleteAtm(@PathVariable String code, Model model) {
        atmService.delete(code);
        return "atms/delete";
    }

    @GetMapping("/atms/{id}")
    public String getAtm(@PathVariable String id, Model model) {
        Atm atm = atmService.getByCode(id);
        model.addAttribute("atm",atm);
        return "atms/atm";
    }

    @GetMapping("/operations")
    public String showOperationsForm(
            @AuthenticationPrincipal ClientUserDetails clientUserDetails,
            Model model
    ) {
        model.addAttribute("operationDto", new OperationDTO());
        model.addAttribute("hasResult", false);
        Long clientId = clientUserDetails.getClient().getId();
        PageResult<List<Card>> page = cardService.getAll(
                0,
                100,
                (cb, root) -> new Predicate[]{
                        cb.equal(root.get("client").get("id"), clientId)
                },
                new SortUnit("id", Order.ASC)
        );
        List<Card> cards = page.getQueryResult();
        model.addAttribute("cards", cards);
        return "atms/operations";
    }


    @PostMapping("/operations")
    public String processOperation(
            @Validated @ModelAttribute("operationDto") OperationDTO dto,
            BindingResult bindingResult,
            @AuthenticationPrincipal ClientUserDetails clientUserDetails,
            Model model
    ) {
        operationValidator.validate(dto, bindingResult);
        Long clientId = clientUserDetails.getClient().getId();
        PageResult<List<Card>> page = cardService.getAll(
                0,
                100,
                (cb, root) -> new Predicate[]{
                        cb.equal(root.get("client").get("id"), clientId)
                },
                new SortUnit("id", Order.ASC)
        );
        List<Card> cards = page.getQueryResult();
        model.addAttribute("cards", cards);
        if (bindingResult.hasErrors()) {
            model.addAttribute("hasResult", false);
            return "atms/operations";
        }

        String result;
        try {
            result = atmService.doOperation(dto);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = "Ошибка: " + ex.getMessage();
        }

        model.addAttribute("hasResult", true);
        model.addAttribute("resultMessage", result);
        return "atms/operations";
    }



}
