package rut.miit.tech.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import rut.miit.tech.web.domain.model.SupportTicket;

import java.util.List;
import java.util.Optional;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    Optional<SupportTicket> findById(Long Id);
    @Query("SELECT st FROM Bank b JOIN Employee e ON e.bank.code =b.code JOIN SupportTicket st ON st.employee.id=e.id WHERE b.code=:code")
    List<SupportTicket> findAllByBankCode(@Param("code") String bankCode);
}
