package rut.miit.tech.web.controller;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import rut.miit.tech.web.domain.dto.MessageDTO;
import rut.miit.tech.web.domain.dto.params.PaginationParams;
import rut.miit.tech.web.domain.model.ClientUserDetails;
import rut.miit.tech.web.domain.model.EmployeeUserDetails;
import rut.miit.tech.web.domain.model.Message;
import rut.miit.tech.web.domain.model.SupportTicket;
import rut.miit.tech.web.domain.util.convertor.DtoConverter;
import rut.miit.tech.web.service.messege.MessageService;
import rut.miit.tech.web.service.util.Order;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.SortUnit;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final DtoConverter dtoConverter;
    //TODO
    // 1. GetLastMessagesWithPagination id = 200  id < 200
    // 3. PostMessage
    // 4. GetPollingMessages  id = 204 id > 204


    // - 3
    // - 2
    // - 1
    // LastMessage id=200
    // + 1
    // + 2
    // +

    @GetMapping("/{chat_id}/history")
    public List<MessageDTO> getHistoryMessages(@PathVariable("chat_id") Long chatId,
                                               @RequestParam("id") Long id,
                                               @ModelAttribute PaginationParams pagination){
        PageResult<List<Message>> pageResult = messageService.getAll(pagination.getPage(), pagination.getPageSize(),
                (cb, root) -> new Predicate[]{
                cb.lessThan(root.get("id"),id),
                cb.equal(root.get("ticket").get("id"),chatId)
        }, new SortUnit("sentDatetime", Order.DESC));
        return pageResult.map((models) -> dtoConverter.toDto(models, MessageDTO.class)).getQueryResult().reversed();

    }

    @GetMapping("/{chat_id}/last")
    public List<MessageDTO> getLastMessagesAfterId(@PathVariable("chat_id") Long chatId,
            @RequestParam("id") Long id){

        return dtoConverter.toDto(messageService.getAll((cb, root) -> new Predicate[]{
                cb.greaterThan(root.get("id"),id),
                cb.equal(root.get("ticket").get("id"),chatId)
        }, new SortUnit("sentDatetime", Order.DESC)),MessageDTO.class);
    }

    @PostMapping("")
    public MessageDTO postMessage(@RequestBody MessageDTO messageDTO,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        Message message = new Message();
        message.setText(messageDTO.getText());
        SupportTicket supportTicket = new SupportTicket();
        supportTicket.setId(messageDTO.getTicketId());
        message.setTicket(supportTicket);
        message.setStatus("");
        if(userDetails instanceof ClientUserDetails client){
            message.setSenderClient(client.getClient());
        }else if(userDetails instanceof EmployeeUserDetails employee){
            message.setSenderEmployee(employee.getEmployee());
        }
        message = messageService.create(message);
        return dtoConverter.toDto(message, MessageDTO.class);
    }

    @PutMapping("")
    public MessageDTO putMessage(@RequestBody List<MessageDTO> messageDTO){
        return null;
    }

}
