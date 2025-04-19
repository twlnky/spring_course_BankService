package rut.miit.tech.web.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "passport_data", nullable = false, length = 100, unique = true)
    private String passportData;

    @Column(nullable = false, length = 14, unique = true)
    private String snils;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 50, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(name = "birth_date", nullable = false)
    private Date birthDate;

    @ManyToOne
    @JoinColumn(name = "bank_code", nullable = false)
    private Bank bank;
}
