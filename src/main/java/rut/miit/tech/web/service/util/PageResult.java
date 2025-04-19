package rut.miit.tech.web.service.util;


import lombok.Data;

@Data
public class PageResult<T> {
    private final T queryResult;
    private final Long pageCount;

    public static <T> PageResult<T> of(T queryResult, Long pageCount) {
        return new PageResult<T>(queryResult, pageCount);
    }
}
