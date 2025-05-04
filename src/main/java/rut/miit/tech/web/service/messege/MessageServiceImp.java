package rut.miit.tech.web.service.messege;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rut.miit.tech.web.domain.exception.ResourceNotFountException;
import rut.miit.tech.web.domain.model.Client;
import rut.miit.tech.web.domain.model.Employee;
import rut.miit.tech.web.domain.model.Message;
import rut.miit.tech.web.repository.EmployeeRepository;
import rut.miit.tech.web.repository.MessageRepository;
import rut.miit.tech.web.service.util.*;

import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor
public class MessageServiceImp implements MessageService{
    private final MessageRepository messageRepository;
    private final QueryBuilder queryBuilder;

    @Override
    public Message getById(Long id) {
        return messageRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public PageResult<List<Message>> getAll(int page, int pageSize, List<FilterUnit> filters, SortUnit sort) {
        return PageResult.of(queryBuilder.getAll(page, pageSize, filters, sort, Message.class),
                queryBuilder.getPageCount(pageSize, filters, Message.class));
    }

    @Override
    public PageResult<List<Message>> getAll(int page, int pageSize, CriteriaFilter<Message> filter, SortUnit sort) {
        return PageResult.of(queryBuilder.getAll(page, pageSize, filter, sort, Message.class),
                queryBuilder.getPageCount(pageSize, filter, Message.class));
    }

    @Override
    public List<Message> getAll(CriteriaFilter<Message> filter, SortUnit sort) {
        return queryBuilder.getAll(filter, sort, Message.class);
    }



    @Override
    public Message create(Message card) {
        return messageRepository.save(card);
    }

    @Override
    public Message update(Message card) {
        return messageRepository.save(card);
    }

    @Override
    public void delete(Long id) {
        messageRepository.deleteById(id);
    }
}
