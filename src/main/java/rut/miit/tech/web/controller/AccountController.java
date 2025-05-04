package rut.miit.tech.web.controller;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import rut.miit.tech.web.domain.model.Account;
import rut.miit.tech.web.domain.model.ClientUserDetails;
import rut.miit.tech.web.service.account.AccountService;
import rut.miit.tech.web.service.util.Order;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.SortUnit;

import java.util.List;

@Controller
@RequestMapping("/clients/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;


    @GetMapping
    public String viewAllAccounts(
            @AuthenticationPrincipal ClientUserDetails userDetails,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "q", defaultValue = "") String q,
            Model model) {

        PageResult<List<Account>> accountPage = accountService.getAll(
                page, 10,
                (cb, root) -> {
                    Predicate byClient = cb.equal(
                            root.get("client").get("id"),
                            userDetails.getClient().getId()
                    );
                    if (q.isBlank()) return new Predicate[]{byClient};

                    String pat = "%" + q.toLowerCase() + "%";
                    Predicate byNumber = cb.like(cb.lower(root.get("accountNumber")), pat);
                    Predicate byType   = cb.like(cb.lower(root.get("type")), pat);
                    return new Predicate[]{cb.and(byClient, cb.or(byNumber, byType))};
                },
                new SortUnit("id", Order.ASC)
        );

        model.addAttribute("accounts",   accountPage.getQueryResult());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount",   accountPage.getPageCount());
        model.addAttribute("q",           q);
        return "clients/accounts";
    }


    @GetMapping("/{id}")
    public String viewAccount(
            @PathVariable Long id,
            @AuthenticationPrincipal ClientUserDetails userDetails,
            Model model) {

        Account account = accountService.getById(id);
        if (account == null
                || !account.getClient().getId().equals(userDetails.getClient().getId())) {
            return "redirect:/access_denied";
        }

        model.addAttribute("account", account);
        return "clients/account";
    }

    /** Удаление счета */
    @PostMapping("/{id}/delete")
    public String deleteAccount(
            @PathVariable Long id,
            @AuthenticationPrincipal ClientUserDetails userDetails) {
        Account account = accountService.getById(id);
        if (account == null
                || !account.getClient().getId().equals(userDetails.getClient().getId())) {
            return "redirect:/access_denied";
        }
        accountService.delete(id);
        return "redirect:/clients/account";
    }
}
