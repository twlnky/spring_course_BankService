package rut.miit.tech.web.repository;

import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import rut.miit.tech.web.domain.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByLogin(String login);
    boolean existsByLogin(String login);

    boolean existsByEmail(String email);

}
