package rut.miit.tech.web.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "card")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(name = "issue_date", nullable = false)
    private Date issueDate;

    @Column(name = "is_blocked", nullable = false)
    private boolean isBlocked;

    @Column(name = "expiration_date", nullable = false)
    private Date expirationDate;

    @ManyToOne
    @JoinColumn(name = "bank_code", nullable = false)
    private Bank bank;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "card")
    private List<Operation> operations;

    @Column(name = "binding_date", nullable = false)
    private Date bindingDate;

    @Column(name = "binding_status", nullable = false, length = 20)
    private String bindingStatus;
}
