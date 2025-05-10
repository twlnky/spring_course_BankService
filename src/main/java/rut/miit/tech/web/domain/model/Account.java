package rut.miit.tech.web.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false, length = 30, unique = true)
    private String accountNumber;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(name = "is_blocked", nullable = false)
    private boolean isBlocked;

    @Column(name = "open_date", nullable = false)
    private Date openDate;

    @Column(name="balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "bank_code", nullable = false)
    private Bank bank;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
}
