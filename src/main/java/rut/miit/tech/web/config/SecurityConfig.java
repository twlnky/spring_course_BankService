package rut.miit.tech.web.config;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    @SneakyThrows
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(form ->
                        form.loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login/process")
                                .defaultSuccessUrl("/redirection", true)
                )
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(
                        requests ->
                                requests.requestMatchers("/auth/**","/css/**","/js/**","/img/**","/access_denied")
                                        .permitAll()

                                        .requestMatchers("/admin/**")
                                            .hasAuthority("ADMIN")

                                        .requestMatchers("/employee/**")
                                            .hasAuthority("EMPLOYEE")

                                        .requestMatchers("/client/**")
                                            .hasAuthority("CLIENT")

                                        .anyRequest()
                                        .authenticated()
                ).exceptionHandling(handler ->
                        handler.accessDeniedPage("/access_denied"))
                .build();
    }

}
