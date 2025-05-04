package rut.miit.tech.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rut.miit.tech.web.domain.model.Employee;
import rut.miit.tech.web.domain.model.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllById(Long id);
}
