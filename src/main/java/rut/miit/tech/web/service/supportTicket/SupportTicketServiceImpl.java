package rut.miit.tech.web.service.supportTicket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rut.miit.tech.web.domain.model.Operation;
import rut.miit.tech.web.domain.model.SupportTicket;
import rut.miit.tech.web.repository.OperationRepository;
import rut.miit.tech.web.repository.SupportTicketRepository;
import rut.miit.tech.web.service.util.FilterUnit;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.QueryBuilder;
import rut.miit.tech.web.service.util.SortUnit;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SupportTicketServiceImpl implements SupportTicketService {
    private final SupportTicketRepository supportTicketRepository;
    private final QueryBuilder queryBuilder;

    @Override
    public SupportTicket getById(Long id) {
        return supportTicketRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public PageResult<List<SupportTicket>> getAll(int page, int pageSize, List<FilterUnit> filters, SortUnit sort) {
        return PageResult.of(queryBuilder.getAll(page, pageSize, filters, sort, SupportTicket.class),
                queryBuilder.getPageCount(pageSize, filters, SupportTicket.class));
    }

    @Override
    public SupportTicket create(SupportTicket card) {
        return supportTicketRepository.save(card);
    }

    @Override
    public SupportTicket update(SupportTicket card) {
        return supportTicketRepository.save(card);
    }

    @Override
    public void delete(Long id) {
        supportTicketRepository.deleteById(id);
    }
}
