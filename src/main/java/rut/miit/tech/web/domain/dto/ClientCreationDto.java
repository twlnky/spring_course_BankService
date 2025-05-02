package rut.miit.tech.web.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ClientCreationDto {

    private String fullName;
    @Pattern(regexp = "[0-9]{4} [0-9]{6}", message = "Passport data should be valid formatted")
    private String passportData;

    @Pattern(regexp = "[0-9]{3}-[0-9]{3}-[0-9]{3} [0-9]{2}", message ="Snils data should be valid formatted" )
    private String snils;

    @Email(message = "email should be valid formatted")
    private String email;

    @Length(min = 1, max = 50, message = "Login length should be exact 50 symbols")
    private String login;

    @Length(min = 8, max = 255, message = "Incorrect passport length")
    private String password;
    private String passwordConfirm;

    @Pattern(regexp = "\\+\\d{1,3}-\\d{1,4}-\\d{7,10}", message = "Phone number should be valid formatted")
    private String phone;

    private String birthDate;

    private String bank;
}
