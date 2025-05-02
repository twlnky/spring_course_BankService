package rut.miit.tech.web.service.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SortUnit {
    private String field;
    private Order order;

}
