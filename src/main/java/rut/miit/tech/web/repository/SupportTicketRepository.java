package rut.miit.tech.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rut.miit.tech.web.domain.model.SupportTicket;

import java.util.Optional;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    Optional<SupportTicket> findById(Long Id);
}
