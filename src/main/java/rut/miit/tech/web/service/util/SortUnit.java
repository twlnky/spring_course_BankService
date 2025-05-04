package rut.miit.tech.web.service.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortUnit {
    private String field = "id";
    private Order order = Order.ASC;

}
