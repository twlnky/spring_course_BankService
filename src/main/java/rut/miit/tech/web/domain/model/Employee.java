package rut.miit.tech.web.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false, length = 50, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "bank_code", nullable = false)
    private Bank bank;

    @Column(nullable = false, length = 100)
    private String position;

    @Column(name = "hire_date", nullable = false)
    private Date hireDate;
}
