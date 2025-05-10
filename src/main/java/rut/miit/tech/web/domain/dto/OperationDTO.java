package rut.miit.tech.web.domain.dto;

import lombok.Data;
import rut.miit.tech.web.domain.model.OperationType;

import java.math.BigDecimal;

@Data
public class OperationDTO {
    private Long cardId;
    private BigDecimal amount;
    private OperationType type;
}
