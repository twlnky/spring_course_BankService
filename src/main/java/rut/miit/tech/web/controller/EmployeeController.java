package rut.miit.tech.web.controller;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import rut.miit.tech.web.domain.dto.params.PaginationParams;
import rut.miit.tech.web.domain.model.EmployeeUserDetails;
import rut.miit.tech.web.domain.model.SupportTicket;
import rut.miit.tech.web.service.supportTicket.SupportTicketService;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.SortUnit;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {

    private final SupportTicketService supportTicketService;

    @GetMapping
    public String employee(@ModelAttribute PaginationParams paginationParams,
                           @AuthenticationPrincipal EmployeeUserDetails userDetails, Model model) {

        PageResult<List<SupportTicket>> result = supportTicketService.getAll(
                paginationParams.getPage(),
                paginationParams.getPageSize(),
                (cb, root) -> new Predicate[]{

                        cb.or(
                                cb.equal(root.get("employee").get("id"), userDetails.getEmployee().getId()),
                                cb.isNull(root.get("employee"))
                        )
                },
                new SortUnit()
        );

        List<SupportTicket> supportTickets = result.getQueryResult();

        model.addAttribute("supportTickets", supportTickets);
        model.addAttribute("paginationParams", paginationParams);
        model.addAttribute("employee", userDetails.getEmployee().getLogin());

        return "employees/homescreen";
    }

}
