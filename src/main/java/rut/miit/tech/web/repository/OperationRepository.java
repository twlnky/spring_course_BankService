package rut.miit.tech.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rut.miit.tech.web.domain.model.Operation;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    List<Operation> findAllById(Long id);
    @Query("SELECT op FROM Account a JOIN Card c ON c.account.id = a.id JOIN Operation op ON c.id=op.card.id WHERE a.id=:id")
    List<Operation> findAllByAccount(@Param("id") Long accountId);



}
