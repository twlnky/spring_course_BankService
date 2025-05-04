package rut.miit.tech.web.domain.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MessageDTO {
    private Long id;
    private Long ticketId;
    private String text;
    private Long senderEmployeeId;
    private Long senderClientId;
    private Timestamp sentDatetime;
}
