package rut.miit.tech.web.service.util;

import lombok.Data;
import lombok.Getter;

@Data
public class FilterUnit {
    private String field;
    private FilterOperations operation;
    private Object value;
}
