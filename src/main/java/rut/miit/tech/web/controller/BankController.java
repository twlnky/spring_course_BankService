package rut.miit.tech.web.controller;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rut.miit.tech.web.domain.model.Atm;
import rut.miit.tech.web.domain.model.Bank;
import rut.miit.tech.web.domain.model.Client;
import rut.miit.tech.web.domain.model.Employee;
import rut.miit.tech.web.service.atm.AtmService;
import rut.miit.tech.web.service.bank.BankService;
import rut.miit.tech.web.service.client.ClientService;
import rut.miit.tech.web.service.employee.EmployeeService;
import rut.miit.tech.web.service.util.*;

import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/admin/index")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;
    private final ClientService clientService;
    private final EmployeeService employeeService;
    private final AtmService atmService;


    @GetMapping
    public String getAllBanks(
            @RequestParam(value = "page", defaultValue = "0") int currentPage,
            Model model) {

        List<Bank> banks = bankService.getAll();
        int pageCount = (int) Math.ceil((double) banks.size() / 10);

        model.addAttribute("banks", banks);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageCount", pageCount);
        return "admin/index";
    }


    @GetMapping("/view/{code}")
    public String viewBank(
            @PathVariable("code") String code,
            Model model) {

        Bank bank = bankService.getById(code);
        model.addAttribute("bank", bank);
        return "admin/info";
    }


    @GetMapping("/{code}/clients")
    public String getBankClients(
            @PathVariable String code,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "q", defaultValue = "") String q,
            Model model) {


        PageResult<List<Client>> clientPage = clientService.getAll(
                page, 10,
                (cb, root) -> {
                    Predicate byBank = cb.equal(root.get("bank").get("code"), code);
                    if (q.isBlank()) {
                        return new Predicate[]{ byBank };
                    }
                    String pattern = "%" + q.toLowerCase() + "%";
                    Predicate byName  = cb.like(cb.lower(root.get("fullName")), pattern);
                    Predicate byLogin = cb.like(cb.lower(root.get("login")),    pattern);
                    return new Predicate[]{ cb.and(byBank, cb.or(byName, byLogin)) };
                },
                new SortUnit("id", Order.ASC)
        );

        model.addAttribute("clients", clientPage.getQueryResult());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount", clientPage.getPageCount());
        model.addAttribute("search", q);
        model.addAttribute("bankCode", code);
        return "clients/index";
    }


    @GetMapping("/{code}/atms")
    public String getBankAtms(
            @PathVariable String code,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "q", defaultValue = "") String q,
            Model model) {

        PageResult<List<Atm>> atmPage = atmService.getAll(
                page, 10,
                (cb, root) -> {
                    Predicate byBank = cb.equal(root.get("bank").get("code"), code);
                    if (q.isBlank()) {
                        return new Predicate[]{ byBank };
                    }
                    String pattern = "%" + q.toLowerCase() + "%";
                    Predicate byName  = cb.like(cb.lower(root.get("fullName")), pattern);
                    Predicate byLogin = cb.like(cb.lower(root.get("login")),    pattern);
                    return new Predicate[]{ cb.and(byBank, cb.or(byName, byLogin)) };
                },
                new SortUnit("id", Order.ASC)
        );

        model.addAttribute("atms",        atmPage.getQueryResult());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount",   atmPage.getPageCount());
        model.addAttribute("bankCode",    code);
        return "atms/index";
    }



    @GetMapping("/{code}/employees")
    public String getBankEmployee(
            @PathVariable String code,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        PageResult<List<Employee>> employeePage = employeeService.getAll(
                page, 10,
                (cb, root) -> new Predicate[]{
                        cb.equal(root.get("bank").<String>get("code"), code)
                },
                new SortUnit("id", Order.ASC)
        );

        model.addAttribute("employees",        employeePage.getQueryResult());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount",   employeePage.getPageCount());
        model.addAttribute("bankCode",    code);
        return "employees/index";
    }


    @GetMapping("/update/{code}")
    public String updateBankForm(
            @PathVariable("code") String code,
            Model model) {

        Bank bank = bankService.getById(code);
        model.addAttribute("bank", bank);
        return "admin/update";
    }


    @PostMapping("/update/{code}")
    public String updateBank(@PathVariable String code,
                             @ModelAttribute("bank") Bank updatedBank) {
        Bank existing = bankService.getById(code);
        updatedBank.setRegistrationDate(existing.getRegistrationDate());
        updatedBank.setCode(code);
        bankService.update(updatedBank);
        return "redirect:/admin/index";
    }



    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("bank", new Bank());
        return "admin/create";
    }


    @PostMapping("/create")
    public String processCreateForm(
            @Valid @ModelAttribute("bank") Bank bank,
            BindingResult result) {

        if (result.hasErrors()) {
            return "admin/create";
        }
        bank.setRegistrationDate(new Timestamp(System.currentTimeMillis()));
        bankService.create(bank);
        return "redirect:/admin/index";
    }


    @PostMapping("/delete/{code}")
    public String deleteBank(@PathVariable String code) {
        bankService.deleteByCode(code);
        return "redirect:/admin/index";
    }


}
