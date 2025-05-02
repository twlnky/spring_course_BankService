package rut.miit.tech.web.controller;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rut.miit.tech.web.domain.model.Bank;
import rut.miit.tech.web.service.bank.BankService;
import rut.miit.tech.web.service.util.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final BankService bankService;


    @GetMapping
    public String getAdminPage(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "search", defaultValue = "") String bankName,
            Model model) {


        List<FilterUnit> filters = new ArrayList<>();
        if (!bankName.isEmpty()) {
            FilterUnit nameFilter = new FilterUnit();
            nameFilter.setField("name");
            nameFilter.setOperation(FilterOperations.LIKE);
            nameFilter.setValue("%" + bankName + "%");
            filters.add(nameFilter);
        }


        SortUnit sort = new SortUnit("id", Order.ASC);
        PageResult<List<Bank>> pageResult = bankService.getAll(page, 10, (cb,root) -> new Predicate[]{
                cb.like(cb.lower(root.get("name")), "%" + bankName + "%"),
        }, sort);

        model.addAttribute("banks", pageResult.getQueryResult());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount", pageResult.getPageCount());
        model.addAttribute("bankName", bankName);
        model.addAttribute("search", bankName);

        return "admin/index";
    }

}
