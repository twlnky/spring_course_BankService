package rut.miit.tech.web.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "support_ticket")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupportTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "closed_date")
    private Date closedDate;

    @Override
    public String toString() {
        return "SupportTicket{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", closedDate=" + closedDate +
                '}';
    }
}
