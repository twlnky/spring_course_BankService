package rut.miit.tech.web.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.security.Timestamp;

public class BankCreationDto {

    private String name;

    private String legal_address;

    @Pattern(regexp = "\\+\\d{1,3}-\\d{1,4}-\\d{7,10}", message = "Phone number should be valid formatted")
    private String phone;

    @Email
    private String email;

    @Length(min = 6, max = 6, message = "Incorrect registration_date length")
    private Timestamp registration_date;

}
