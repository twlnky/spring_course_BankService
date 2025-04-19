package rut.miit.tech.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rut.miit.tech.web.domain.model.Atm;
import rut.miit.tech.web.domain.model.Bank;

import java.util.List;

public interface AtmRepository extends JpaRepository<Atm, Long> {
    List<Atm> findByAtmCode(String accountCode);

}
