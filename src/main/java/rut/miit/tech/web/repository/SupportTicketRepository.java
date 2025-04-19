package rut.miit.tech.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rut.miit.tech.web.domain.model.SupportTicket;

import java.util.List;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findByUserId(Long userId);
}
