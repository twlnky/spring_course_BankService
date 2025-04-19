package rut.miit.tech.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rut.miit.tech.web.domain.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findAllByEmail(String email);

    Optional<Client> findByLogin(String login);
}
