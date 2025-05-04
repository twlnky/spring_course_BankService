package rut.miit.tech.web.controller;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import rut.miit.tech.web.domain.dto.SupportTicketDTO;
import rut.miit.tech.web.domain.model.*;
import rut.miit.tech.web.service.messege.MessageService;
import rut.miit.tech.web.service.supportTicket.SupportTicketService;
import rut.miit.tech.web.service.util.Order;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.SortUnit;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequestMapping("/tickets")
@Controller
@RequiredArgsConstructor
public class SupportTicketController {
    private final SupportTicketService supportTicketService;
    private final MessageService messageService;

    @GetMapping("/{id}/chat")
    public String getChatPage(@PathVariable Long id,
                              Model model,
                              @AuthenticationPrincipal UserDetails userDetails) {

        PageResult<List<Message>> result =
                messageService.getAll(0,10,(cb, root) -> new Predicate[]{
                  cb.equal(root.get("ticket").get("id"), id)
                }, new SortUnit("sentDatetime", Order.DESC));
        List<Message> messages = new ArrayList<>(result.getQueryResult());


        model.addAttribute("id",id);
        model.addAttribute("lastMessageId", messages.getLast().getId());
        model.addAttribute("lastAcceptedId", messages.getFirst().getId());
        model.addAttribute("messages", messages.reversed());
        model.addAttribute("pageCount", result.getPageCount());
        if(userDetails instanceof EmployeeUserDetails) {
            model.addAttribute("isClient",false);
            model.addAttribute("opposite","Client");
        }else if(userDetails instanceof ClientUserDetails) {
            model.addAttribute("opposite", "Tech support");
            model.addAttribute("isClient",true);
        }


        return "ticket/chat";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("ticketDto", new SupportTicketDTO());
        return "ticket/create";
    }

    @PostMapping("/create")
    public String createTicket(@AuthenticationPrincipal UserDetails userDetails, SupportTicketDTO ticketDto) {
        Client client = ((ClientUserDetails) userDetails).getClient();


        SupportTicket supportTicket = new SupportTicket();
        supportTicket.setDescription(ticketDto.getDescription());
        supportTicket.setStatus("true"); // желательно заменить на enum в будущем
        supportTicket.setClient(client);
        supportTicket.setCreatedDate(new Date());


        SupportTicket savedTicket = supportTicketService.create(supportTicket);


        Message message = new Message();
        message.setTicket(savedTicket);
        message.setText(ticketDto.getDescription());
        message.setSenderClient(client);
        message.setStatus("true");
        supportTicket.setCreatedDate(Date.from(Instant.ofEpochSecond(System.currentTimeMillis())));


        messageService.create(message);

        return "redirect:/clients/tickets";
    }
}
