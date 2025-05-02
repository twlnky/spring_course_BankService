package rut.miit.tech.web.service.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FilterUnit {
    private String field;
    private FilterOperations operation;
    private Object value;
}
