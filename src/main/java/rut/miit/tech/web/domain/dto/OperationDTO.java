package rut.miit.tech.web.domain.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import rut.miit.tech.web.domain.model.OperationType;

import java.math.BigDecimal;

@Data
public class OperationDTO {
    @NotNull(message = "id карты обязателен")
    private Long cardId;

    @NotNull(message = "сумма обязательна")
    private BigDecimal amount = new BigDecimal(0);

    @NotNull(message = "тип операции обязателен")
    private OperationType type;
}
