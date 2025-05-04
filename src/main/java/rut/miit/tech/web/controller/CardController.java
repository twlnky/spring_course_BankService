package rut.miit.tech.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import rut.miit.tech.web.domain.model.*;
import rut.miit.tech.web.service.account.AccountService;
import rut.miit.tech.web.service.bank.BankService;
import rut.miit.tech.web.service.card.CardService;

import java.util.Date;


@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    private final AccountService accountService;
    private final BankService bankService;

    @GetMapping("/account/{id}/cards/create")
    public String showCardForm(@PathVariable Long id, Model model) {
        model.addAttribute("accountId", id);
        return "clients/card_create";
    }

    @PostMapping("/account/{id}/cards/create")
    public String createCard(@PathVariable Long id,
                             @RequestParam String type) {

        Account account = accountService.getById(id);
        Client client = account.getClient();
        Bank bank = account.getBank();

        Card card = new Card();
        card.setType(type);
        card.setIssueDate(new Date());
        card.setExpirationDate(Date.from(
                new Date().toInstant().plus(4 * 365, java.time.temporal.ChronoUnit.DAYS)
        ));
        card.setBindingDate(new Date());
        card.setBindingStatus("ACTIVE");

        card.setAccount(account);
        card.setClient(client);
        card.setBank(bank);

        cardService.create(card);

        return "redirect:/clients";
    }

    @GetMapping("/card/{id}/edit")
    public String showEditCardForm(@PathVariable Long id, Model model) {
        Card card = cardService.getById(id);
        if (card == null) {
            return "redirect:/clients";
        }
        model.addAttribute("card", card);
        return "clients/card_edit";
    }

    @PostMapping("/card/{id}/edit")
    public String updateCard(@PathVariable Long id,
                             @RequestParam String type) {

        Card card = cardService.getById(id);
        if (card == null) {
            return "redirect:/clients";
        }
        card.setType(type);

        cardService.update(card);

        return "redirect:/clients";
    }

    @PostMapping("/card/{id}/delete")
    public String deleteCard(@PathVariable Long id) {
        Card card = cardService.getById(id);
        if (card != null) {
            cardService.delete(id);
        }
        return "redirect:/clients";
    }


    @PostMapping("/account/create")
    public String createAccount(@RequestParam String type, @RequestParam Long bankId, @AuthenticationPrincipal ClientUserDetails userDetails) {
        Bank bank = bankService.getById(String.valueOf(bankId));
        Client client = userDetails.getClient();

        Account account = new Account();
        account.setType(type);
        account.setOpenDate(new Date());
        account.setBank(bank);
        account.setClient(client);

        accountService.create(account);

        return "redirect:/clients";
    }

    @PostMapping("/clients/card/{id}/delete")    public String deleteAccount(@PathVariable Long id) {
        Account account = accountService.getById(id);
        if (account != null) {
            accountService.delete(id);
        }
        return "redirect:/clients";
    }
    @GetMapping("/card/{id}")
    public String viewCard(@PathVariable("id") Long id, Model model) {

        model.addAttribute("card", cardService.getById(id));
        return "clients/card_view";
    }
}

