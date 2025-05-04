package rut.miit.tech.web.service.supportTicket;


import rut.miit.tech.web.domain.model.Client;
import rut.miit.tech.web.domain.model.SupportTicket;
import rut.miit.tech.web.service.util.CriteriaFilter;
import rut.miit.tech.web.service.util.FilterUnit;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.SortUnit;

import java.util.List;

public interface SupportTicketService {
    SupportTicket getById(Long id);
    PageResult<List<SupportTicket>> getAll(int page, int pageSize, List<FilterUnit> filters, SortUnit sort);
    PageResult<List<SupportTicket>> getAll(int page, int pageSize, CriteriaFilter<SupportTicket> filter, SortUnit sort);
    SupportTicket create(SupportTicket card);
    SupportTicket update(SupportTicket card);
    void delete(Long id);
}
