package rut.miit.tech.web.domain.dto.params;

import lombok.Data;

@Data
public class PaginationParams {
    private int page = 0;
    private int pageSize = 10;
}
