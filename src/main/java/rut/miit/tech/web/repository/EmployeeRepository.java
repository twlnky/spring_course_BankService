package rut.miit.tech.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rut.miit.tech.web.domain.model.Employee;
import java.util.List;
import java.util.Optional;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAllById(Long id);

    Optional<Employee> findByLogin(String login);

    boolean existsByLogin(String login);
}
