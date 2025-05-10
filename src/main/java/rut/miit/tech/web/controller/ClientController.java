package rut.miit.tech.web.controller;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rut.miit.tech.web.domain.model.*;
import rut.miit.tech.web.service.account.AccountService;
import rut.miit.tech.web.service.card.CardService;
import rut.miit.tech.web.service.client.ClientService;
import rut.miit.tech.web.service.supportTicket.SupportTicketService;
import rut.miit.tech.web.service.util.Order;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.SortUnit;

import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final AccountService accountService;
    private final SupportTicketService supportTicketService;
    private final CardService cardService;

    @GetMapping
    public String clientDashboard(@AuthenticationPrincipal ClientUserDetails userDetails, Model model) {

        Client client = clientService.getById(userDetails.getClient().getId());
        model.addAttribute("client", client);


        PageResult<List<Account>> accountPage = accountService.getAll(
                0,
                1,
                (cb, root) -> new Predicate[]{
                        cb.equal(root.get("client").get("id"), client.getId())
                },
                new SortUnit("id", Order.ASC)
        );


        List<Account> accounts = accountPage.getQueryResult();
        model.addAttribute("accounts", accounts);


        if (accounts.isEmpty()) {
            model.addAttribute("noAccountMessage", "У вас нет открытых счетов.");
        }


        PageResult<List<Card>> cardPage = cardService.getAll(
                0,
                10,
                (cb, root) -> new Predicate[]{
                        cb.equal(root.get("client").get("id"), client.getId())
                },
                new SortUnit("id", Order.ASC)
        );

        List<Card> cards = cardPage.getQueryResult();
        model.addAttribute("cards", cards);

        return "clients/homescreen";
    }


    @GetMapping("/chat")
    public String redirectToChat(@AuthenticationPrincipal ClientUserDetails userDetails) {
        Long clientId = userDetails.getClient().getId();


        PageResult<List<SupportTicket>> ticketsPage = supportTicketService.getAll(
                0,
                1,
                (cb, root) -> new Predicate[]{
                        cb.equal(root.get("client").get("id"), clientId),
                        cb.equal(root.get("status"), "OPEN")
                },
                new SortUnit("createdDate", Order.ASC)
        );

        SupportTicket open = ticketsPage.getQueryResult().stream().findFirst().orElse(null);

        if (open == null) {
            return "redirect:/tickets/create";
        }

        return "redirect:/tickets/" + open.getId() + "/chat";
    }

    @GetMapping("/tickets")
    public String listTickets(@AuthenticationPrincipal ClientUserDetails userDetails, Model model) {
        Long clientId = userDetails.getClient().getId();
        PageResult<List<SupportTicket>> pr = supportTicketService.getAll(
                0, 20,
                (cb, root) -> new Predicate[]{
                        cb.equal(root.get("client").get("id"), clientId)
                },
                new SortUnit("createdDate", Order.DESC)
        );
        model.addAttribute("tickets", pr.getQueryResult());
        model.addAttribute("pageCount", pr.getPageCount());
        return "clients/tickets";
    }

    @GetMapping("/update")
    public String showUpdateForm(@AuthenticationPrincipal ClientUserDetails userDetails, Model model) {
        Client client = clientService.getById(userDetails.getClient().getId());
        model.addAttribute("client", client);
        return "clients/update";
    }

    @PostMapping("/update")
    public String updateProfile(@AuthenticationPrincipal ClientUserDetails userDetails,
                                @RequestParam String fullName,
                                @RequestParam String email,
                                @RequestParam String phone) {

        Client client = clientService.getById(userDetails.getClient().getId());
        client.setFullName(fullName);
        client.setEmail(email);
        client.setPhone(phone);

        clientService.update(client);

        return "redirect:/clients";
    }
}
