package rut.miit.tech.web.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SupportTicketDTO {
    @NotBlank(message = "Опишите проблему")
    private String description;
}
