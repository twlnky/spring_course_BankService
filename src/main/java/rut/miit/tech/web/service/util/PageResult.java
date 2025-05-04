package rut.miit.tech.web.service.util;


import lombok.Data;

import java.util.function.Function;

@Data
public class PageResult<T> {
    private final T queryResult;
    private final Long pageCount;

    public <R> PageResult<R> map(Function<T,R> mapper) {
        return of(mapper.apply(queryResult), pageCount);
    }

    public static <T> PageResult<T> of(T queryResult, Long pageCount) {
        return new PageResult<T>(queryResult, pageCount);
    }
}
