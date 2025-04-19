package rut.miit.tech.web.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "atm")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Atm {
    @Id
    @Column(name = "code", length = 20)
    private String code;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(name = "installation_date", nullable = false)
    private Date installationDate;

    @Column(name = "last_service_date")
    private Date lastServiceDate;

    @Column(nullable = false, length = 50)
    private String status;

    @ManyToOne
    @JoinColumn(name = "bank_code", nullable = false)
    private Bank bank;
}
