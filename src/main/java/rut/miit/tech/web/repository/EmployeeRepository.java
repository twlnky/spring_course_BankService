package rut.miit.tech.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rut.miit.tech.web.domain.model.Employee;
import java.util.List;
import java.util.Optional;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT DISTINCT e FROM SupportTicket st JOIN Message m ON m.ticket.id = st.id JOIN Employee e ON m.senderEmployee.id=e.id WHERE st.id=:id")
    List<Employee> findDistinctMessagedByTicketId(@Param("id") Long ticketId);

    List<Employee> findAllById(Long id);

    Optional<Employee> findByLogin(String login);

    boolean existsByLogin(String login);
}
