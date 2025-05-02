package rut.miit.tech.web.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rut.miit.tech.web.domain.model.Client;
import rut.miit.tech.web.domain.model.ClientUserDetails;
import rut.miit.tech.web.domain.model.Employee;
import rut.miit.tech.web.domain.model.EmployeeUserDetails;
import rut.miit.tech.web.repository.ClientRepository;
import rut.miit.tech.web.repository.EmployeeRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceDetailsImpl implements UserDetailsService {
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final UserDetails adminDetails;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.equals(adminDetails.getUsername())){
            return adminDetails;
        }
        Optional<Client> client = clientRepository.findByLogin(username);
        Optional<Employee> employee = employeeRepository.findByLogin(username);
        return client.isPresent() ? new ClientUserDetails(client.get())
                : new EmployeeUserDetails(employee.orElseThrow(() -> new UsernameNotFoundException(username)));
    }
}
