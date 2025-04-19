package rut.miit.tech.web.config;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//MiddleWare
// () -> () -> () -> endPoint
// () <- () <- () <- endPoint
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @SneakyThrows
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(Customizer.withDefaults())
                .formLogin(form ->
                        form.loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login/process")
                )
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(
                        requests ->
                                requests.requestMatchers("/auth/**")
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated()
                )
                .build();
    }

}
