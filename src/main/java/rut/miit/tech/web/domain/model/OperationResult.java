package rut.miit.tech.web.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OperationResult {
    private BigDecimal balance;
}
