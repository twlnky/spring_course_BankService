package rut.miit.tech.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rut.miit.tech.web.domain.model.Bank;

import java.util.List;

public interface BankRepository extends JpaRepository<Bank, Long> {
   List<Bank> findByCode(String code);

}
