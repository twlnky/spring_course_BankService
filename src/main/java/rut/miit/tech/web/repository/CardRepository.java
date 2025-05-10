package rut.miit.tech.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rut.miit.tech.web.domain.model.Card;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllById(Long id);

    void deleteAllByAccountId(Long id);

    @Modifying
    @NativeQuery("UPDATE card SET is_blocked=:block WHERE id=:id")
    Integer updateAllIsBlockedById(@Param("id") Long id, @Param("block") boolean block);

}
