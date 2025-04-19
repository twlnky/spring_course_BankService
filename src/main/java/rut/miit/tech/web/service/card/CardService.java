package rut.miit.tech.web.service.card;

import rut.miit.tech.web.domain.model.Card;
import rut.miit.tech.web.service.util.FilterUnit;
import rut.miit.tech.web.service.util.PageResult;
import rut.miit.tech.web.service.util.SortUnit;

import java.util.List;

public interface CardService {

    Card getById(Long id);
    PageResult<List<Card>> getAll(int page, int pageSize, List<FilterUnit> filters, SortUnit sort);
    Card create(Card card);
    Card update(Card card);
    void delete(Long id);

}
