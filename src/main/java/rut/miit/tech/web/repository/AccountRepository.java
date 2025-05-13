package rut.miit.tech.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.ModelAttribute;
import rut.miit.tech.web.domain.model.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findById(Long id);
    @Query("SELECT a FROM Card c JOIN Account a ON c.account.id=a.id WHERE c.id=:cardId")
    Optional<Account> findByCardId(@Param("cardId") Long cardId);

    @Modifying
    @NativeQuery("UPDATE account SET is_blocked=:block WHERE id=:id")
    Integer updateIsBlockedById(@Param("id") Long id, @Param("block") boolean block);
}
