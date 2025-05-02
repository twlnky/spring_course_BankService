package rut.miit.tech.web.service.client;

import rut.miit.tech.web.domain.model.Card;
import rut.miit.tech.web.domain.model.Client;
import rut.miit.tech.web.service.util.CriteriaFilter;
import rut.miit.tech.web.service.util.FilterUnit;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.SortUnit;

import java.util.List;

public interface ClientService {
    Client getById(Long id);
    PageResult<List<Client>> getAll(int page, int pageSize, List<FilterUnit> filters, SortUnit sort);
    PageResult<List<Client>> getAll(int page, int pageSize, CriteriaFilter<Client> filter, SortUnit sort);
    Client create(Client client);
    Client update(Client client);
    void delete(Long id);
}
