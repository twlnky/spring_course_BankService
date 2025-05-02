package rut.miit.tech.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rut.miit.tech.web.domain.model.Atm;

import java.util.Optional;

public interface AtmRepository extends JpaRepository<Atm, String> {
    Optional<Atm> findByCode(String accountCode);

}
