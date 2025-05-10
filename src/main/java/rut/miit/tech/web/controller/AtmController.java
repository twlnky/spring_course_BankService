package rut.miit.tech.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import rut.miit.tech.web.domain.model.Atm;
import rut.miit.tech.web.domain.model.Bank;
import rut.miit.tech.web.domain.model.ClientUserDetails;
import rut.miit.tech.web.domain.model.Employee;
import rut.miit.tech.web.service.atm.AtmService;
import rut.miit.tech.web.service.bank.BankService;
import rut.miit.tech.web.service.card.CardService;

import java.sql.Timestamp;

@Controller
@RequestMapping("/atms")
@RequiredArgsConstructor
public class AtmController {

    private final CardService cardService;
    private final BankService bankService;
    private final AtmService atmService;

    @GetMapping("/atm")
    public String atm(@AuthenticationPrincipal ClientUserDetails clientUserDetails,
                      Model model) {
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



}
