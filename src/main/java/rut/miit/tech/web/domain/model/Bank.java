package rut.miit.tech.web.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bank")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Bank {

    @Id
    @Column(name = "code", length = 20)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(name = "registration_date", nullable = false)
    private Timestamp registrationDate;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "legal_address", nullable = false, columnDefinition = "TEXT")
    private String legalAddress;

    @Column(nullable = false, length = 40)
    private String phone;

    @ToString.Exclude
    @OneToMany(mappedBy = "bank", cascade = CascadeType.ALL)
    private List<Employee> employees = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "bank", cascade = CascadeType.ALL)
    private List<Client> clients = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "bank", cascade = CascadeType.ALL)
    private List<Atm> atms = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "bank", cascade = CascadeType.ALL)
    private List<Account> accounts = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "bank", cascade = CascadeType.ALL)
    private List<Card> cards = new ArrayList<>();
}
