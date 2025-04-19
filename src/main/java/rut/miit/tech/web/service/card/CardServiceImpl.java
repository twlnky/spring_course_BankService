package rut.miit.tech.web.service.card;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rut.miit.tech.web.domain.exception.ResourceNotFountException;
import rut.miit.tech.web.domain.model.Card;
import rut.miit.tech.web.repository.CardRepository;
import rut.miit.tech.web.service.util.FilterUnit;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.QueryBuilder;
import rut.miit.tech.web.service.util.SortUnit;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final QueryBuilder queryBuilder;

    //region CRUD
    @Override
    public PageResult<List<Card>> getAll(int page, int pageSize, List<FilterUnit> filters, SortUnit sort) {

        return PageResult.of(queryBuilder.getAll(page, pageSize, filters, sort, Card.class),
                queryBuilder.getPageCount(pageSize, filters, Card.class));
    }

    @Override
    public Card create(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public Card getById(Long id) {
        return cardRepository.findById(id).orElseThrow(ResourceNotFountException::new);
    }

    @Override
    public Card update(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public void delete(Long id) {
        cardRepository.deleteById(id);
    }
    //endregion
}
