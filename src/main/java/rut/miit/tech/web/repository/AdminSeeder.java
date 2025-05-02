package rut.miit.tech.web.repository;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminSeeder {
    private final PasswordEncoder passwordEncoder;
    @Value("${admin.password}")
    private String password;
    private Logger logger = LoggerFactory.getLogger(AdminSeeder.class);

    @Bean
    public UserDetails adminDetails(){
        String password = this.password == null ? new Faker().internet().password() : this.password;
        String username = "admin";
        logger.info("Generate admins password " + password);
        String encodedPassword = passwordEncoder.encode(password);
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority("ADMIN"));
            }

            @Override
            public String getPassword() {
                return encodedPassword;
            }

            @Override
            public String getUsername() {
                return username;
            }
        };
    }

}
