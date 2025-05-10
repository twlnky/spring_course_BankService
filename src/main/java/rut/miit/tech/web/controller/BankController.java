package rut.miit.tech.web.controller;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rut.miit.tech.web.domain.model.Atm;
import rut.miit.tech.web.domain.model.Bank;
import rut.miit.tech.web.domain.model.Client;
import rut.miit.tech.web.domain.model.Employee;
import rut.miit.tech.web.service.atm.AtmService;
import rut.miit.tech.web.service.bank.BankService;
import rut.miit.tech.web.service.client.ClientService;
import rut.miit.tech.web.service.employee.EmployeeService;
import rut.miit.tech.web.service.util.Order;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.SortUnit;

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

        Bank bank = bankService.getById(code);
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
        model.addAttribute("bank", bank);
        return "clients/index";
    }


    @GetMapping("/{code}/atms")
    public String getBankAtms(
            @PathVariable String code,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "q", defaultValue = "") String q,
            Model model) {
        Bank bank = bankService.getById(code);
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
        model.addAttribute("bank", bank);

        return "atms/index";
    }



    @GetMapping("/{code}/employees")
    public String getBankEmployee(
            @PathVariable String code,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {
        Bank bank = bankService.getById(code);
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
        model.addAttribute("bank", bank);

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
            @Validated @ModelAttribute("bank") Bank bank,
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

    // 1) Форма создания нового сотрудника
    @GetMapping("/{code}/employees/create")
    public String showCreateEmployeeForm(
            @PathVariable("code") String code,
            Model model) {
        model.addAttribute("bankCode", code);
        model.addAttribute("bank", bankService.getById(code));
        model.addAttribute("employee", new Employee());
        return "employees/create";   // шаблон: src/main/resources/templates/employees/create.html
    }

    // 2) Создание
    @PostMapping("/{code}/employees/create")
    public String doCreateEmployee(
            @PathVariable("code") String code,
            @ModelAttribute Employee employee) {
        Bank bank = bankService.getById(code);
        employee.setBank(bank);
        employeeService.create(employee);
        return "redirect:/admin/index/" + code + "/employees";
    }

    // 3) Форма редактирования
    @GetMapping("/{code}/employees/{id}/edit")
    public String showEditEmployeeForm(
            @PathVariable("code") String code,
            @PathVariable("id") Long id,
            Model model) {
        model.addAttribute("bankCode", code);
        model.addAttribute("banks", bankService.getById(code));
        model.addAttribute("employee", employeeService.getById(id));
        return "employees/update";   // шаблон: employees/update.html
    }

    // 4) Обновление
    @PostMapping("/{code}/employees/{id}/edit")
    public String doEditEmployee(
            @PathVariable("code") String code,
            @PathVariable("id") Long id,
            @ModelAttribute Employee employee) {
        employee.setId(id);
        employee.setBank(bankService.getById(code));
        employeeService.update(employee);
        return "redirect:/admin/index/" + code + "/employees";
    }

    // 5) Удаление
    @PostMapping("/{code}/employees/{id}/delete")
    public String doDeleteEmployee(
            @PathVariable("code") String code,
            @PathVariable("id") Long id) {
        employeeService.delete(id);
        return "redirect:/admin/index/" + code + "/employees";
    }

    // 6) Просмотр одного сотрудника (опционально; если нужен)
    @GetMapping("/{code}/employees/{id}")
    public String viewEmployee(
            @PathVariable("code") String code,
            @PathVariable("id") Long id,
            Model model) {
        model.addAttribute("bankCode", code);
        model.addAttribute("bank", bankService.getById(code));
        model.addAttribute("employee", employeeService.getById(id));
        return "employees/view";     // шаблон: employees/view.html
    }

    @GetMapping("/{bankCode}/atms/create")
    public String createAtmForm(@PathVariable String bankCode, Model model) {
        model.addAttribute("atm", new Atm());
        model.addAttribute("bankCode", bankCode);
        return "atms/create";
    }

    @PostMapping("/{bankCode}/atms/create")
    public String createAtm(@PathVariable String bankCode, @ModelAttribute Atm atm) {
        Bank bank = bankService.getById(bankCode);
        atm.setBank(bank);
        atm.setInstallationDate(new Timestamp(System.currentTimeMillis()));
        atmService.create(atm);
        return "redirect:/admin/index/" + bankCode + "/atms";
    }

    @GetMapping("/{bankCode}/atms/update/{id}")
    public String updateAtmForm(@PathVariable String bankCode, @PathVariable String id, Model model) {
        Atm atm = atmService.getByCode(id);
        model.addAttribute("atm", atm);
        model.addAttribute("bankCode", bankCode);
        return "atms/update";
    }

    @PostMapping("/{bankCode}/atms/update/{id}")
    public String updateAtm(@PathVariable String bankCode, @PathVariable String id,
                            @ModelAttribute("atm") Atm updatedAtm) {
        Atm atm = atmService.getByCode(id);
        atm.setStatus(updatedAtm.getStatus());
        atm.setLastServiceDate(new Timestamp(System.currentTimeMillis()));
        atmService.update(atm);
        return "redirect:/admin/index/" + bankCode + "/atms";
    }

    @PostMapping("/{bankCode}/atms/delete/{id}")
    public String deleteAtm(@PathVariable String bankCode, @PathVariable String id) {
        atmService.delete(id);
        return "redirect:/admin/index/" + bankCode + "/atms";
    }

    @GetMapping("/{bankCode}/atms/{id}")
    public String getAtm(@PathVariable String bankCode, @PathVariable String id, Model model) {
        Atm atm = atmService.getByCode(id);
        model.addAttribute("atm", atm);
        model.addAttribute("bankCode", bankCode);
        return "atms/atm";
    }

}
