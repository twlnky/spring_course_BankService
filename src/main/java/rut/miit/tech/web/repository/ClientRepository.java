package rut.miit.tech.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rut.miit.tech.web.domain.model.Client;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {


    @Query("SELECT sum (a.balance) FROM Account a WHERE a.client.id = :id")
    Long getSummaryBalanceByClientId(@Param("id") Long clientId);

    Optional<Client> findByLogin(String login);
    boolean existsByLogin(String login);

    boolean existsByEmail(String email);

}
