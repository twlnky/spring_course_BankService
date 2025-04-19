package rut.miit.tech.web.service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rut.miit.tech.web.domain.model.Card;
import rut.miit.tech.web.domain.model.Client;
import rut.miit.tech.web.repository.ClientRepository;
import rut.miit.tech.web.service.util.FilterUnit;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.QueryBuilder;
import rut.miit.tech.web.service.util.SortUnit;

import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;;
    private final QueryBuilder queryBuilder;
    @Override
    public PageResult<List<Card>> getAll(int page, int pageSize, List<FilterUnit> filters, SortUnit sort) {

        return PageResult.of(queryBuilder.getAll(page, pageSize, filters, sort, Card.class),
                queryBuilder.getPageCount(pageSize, filters, Card.class));
    }

    //region CRUD
    @Override
    public Client getById(Long id) {
        return clientRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public Client create(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client update(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public void delete(Long id) {
        clientRepository.deleteById(id);
    }
    //endregion

}
