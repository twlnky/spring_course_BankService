package rut.miit.tech.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rut.miit.tech.web.domain.model.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findById(Long id);

}
