package rut.miit.tech.web.service.messege;

import rut.miit.tech.web.domain.model.Message;
import rut.miit.tech.web.service.util.CriteriaFilter;
import rut.miit.tech.web.service.util.FilterUnit;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.SortUnit;

import java.util.List;

public interface MessageService {
    Message getById(Long id);
    PageResult<List<Message>> getAll(int page, int pageSize, List<FilterUnit> filters, SortUnit sort);
    PageResult<List<Message>> getAll(int page, int pageSize, CriteriaFilter<Message> filter, SortUnit sort);
    List<Message> getAll(CriteriaFilter<Message> filter, SortUnit sort);

    Message create(Message card);
    Message update(Message card);
    void delete(Long id);
}
