package rut.miit.tech.web.controller;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import rut.miit.tech.web.domain.dto.OperationDTO;
import rut.miit.tech.web.domain.model.Card;
import rut.miit.tech.web.domain.model.ClientUserDetails;
import rut.miit.tech.web.domain.model.Operation;
import rut.miit.tech.web.domain.model.OperationResult;
import rut.miit.tech.web.domain.util.convertor.DtoConverter;
import rut.miit.tech.web.domain.util.validation.OperationValidator;
import rut.miit.tech.web.service.card.CardService;
import rut.miit.tech.web.service.operation.OperationService;
import rut.miit.tech.web.service.util.Order;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.SortUnit;

import java.util.List;

@Controller
@RequestMapping("/operation")
@RequiredArgsConstructor
public class OperationController {
    private final OperationValidator validator;
    private final OperationService operationService;
    private final DtoConverter dtoConverter;
    private final CardService cardService;

    @GetMapping
    public String showForm(Model model,
                           @AuthenticationPrincipal ClientUserDetails user) {
        Long clientId = user.getClient().getId();
        PageResult<List<Card>> page = cardService.getAll(
                0,
                100,
                (cb, root) -> new Predicate[]{
                        cb.equal(root.get("client").get("id"), clientId)
                },
                new SortUnit("id", Order.ASC)
        );
        List<Card> cards = page.getQueryResult();
        model.addAttribute("cards", cards);
        model.addAttribute("operationDto", new OperationDTO());
        model.addAttribute("hasResult", false);
        return "atms/operations";
    }

    @PostMapping
    public String doOperation(@Validated @ModelAttribute OperationDTO dto,
                              BindingResult errors,
                              Model model,
                              @AuthenticationPrincipal ClientUserDetails user) {

        Long clientId = user.getClient().getId();
        PageResult<List<Card>> page = cardService.getAll(
                0,
                100,
                (cb, root) -> new Predicate[]{
                        cb.equal(root.get("client").get("id"), clientId)
                },
                new SortUnit("id", Order.ASC)
        );
        List<Card> cards = page.getQueryResult();
        model.addAttribute("cards", cards);

        validator.validate(dto, errors);
        if (errors.hasErrors()) {
            model.addAttribute("hasResult", false);
            return "atms/operations";
        }

        OperationResult result = operationService.doOperation(
                dtoConverter.toModel(dto, Operation.class)
        );

        model.addAttribute("hasResult", true);
        model.addAttribute("operationResult", result);
        return "atms/operations";
    }
}

