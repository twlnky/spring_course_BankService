package rut.miit.tech.web.controller;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import rut.miit.tech.web.domain.dto.params.PaginationParams;
import rut.miit.tech.web.domain.model.Bank;
import rut.miit.tech.web.domain.model.Employee;
import rut.miit.tech.web.domain.model.EmployeeUserDetails;
import rut.miit.tech.web.domain.model.SupportTicket;
import rut.miit.tech.web.service.bank.BankService;
import rut.miit.tech.web.service.employee.EmployeeService;
import rut.miit.tech.web.service.supportTicket.SupportTicketService;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.SortUnit;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final SupportTicketService supportTicketService;
    private final BankService bankService;


    @GetMapping
    public String home(@ModelAttribute PaginationParams params,
                       @AuthenticationPrincipal EmployeeUserDetails userDetails,
                       Model model) {
        var page = supportTicketService.getAll(
                params.getPage(), params.getPageSize(),
                (cb, root) -> new Predicate[]{
                        cb.or(
                                cb.equal(root.get("employee").get("id"), userDetails.getEmployee().getId()),
                                cb.isNull(root.get("employee"))
                        )
                },
                new SortUnit()
        );
        model.addAttribute("supportTickets", page.getQueryResult());
        model.addAttribute("paginationParams", params);
        model.addAttribute("employeeName", userDetails.getEmployee().getFullName());
        return "employees/homescreen";
    }


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new Employee());
        List<Bank> banks = (List<Bank>) bankService.getAll(0, 10, List.of(), new SortUnit());
        model.addAttribute("banks", banks);
        return "employees/create";
    }
    @PostMapping("/create")
    public String doCreate(@ModelAttribute Employee employee) {
        employeeService.create(employee);
        return "redirect:/employees";
    }


    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("employee", employeeService.getById(id));
        return "employees/update";
    }
    @PostMapping("/{id}/edit")
    public String doEdit(@PathVariable Long id,
                         @ModelAttribute Employee updated) {
        updated.setId(id);
        employeeService.update(updated);
        return "redirect:/employees/" + id;
    }


    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("employee", employeeService.getById(id));
        return "employees/view";
    }


    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        employeeService.delete(id);
        return "redirect:/employees";
    }
}

