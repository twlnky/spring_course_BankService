package rut.miit.tech.web.repository;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rut.miit.tech.web.domain.model.Bank;
import rut.miit.tech.web.domain.model.Client;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Locale;

@Profile("dev")
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final BankRepository bankRepository;

    @Transactional
    @Override
    public void run(String... args) throws Exception {


        try {
            Faker faker = new Faker(new Locale("ru"));

            bankRepository.deleteAll();
            clientRepository.deleteAll();

            Bank bank = new Bank();
            bank.setRegistrationDate(Date.from(Instant.now()));
            bank.setLegalAddress(faker.address().fullAddress());
            bank.setName(faker.company().name()+" Bank");
            bank.setEmail(faker.internet().emailAddress());
            bank.setPhone(faker.phoneNumber().phoneNumber());
            bank = bankRepository.save(bank);

            Client client = new Client();
            client.setLogin(faker.name().username());
            client.setPassportData(faker.numerify("#### ######"));
            client.setPassword(passwordEncoder.encode(faker.internet().password()));
            client.setPhone(faker.phoneNumber().phoneNumber());
            client.setEmail(faker.internet().emailAddress());
            client.setEnable(true);
            client.setFullName(faker.name().fullName());
            client.setBirthDate(Timestamp.from(Instant.now()));
            client.setSnils(faker.numerify("###-###-### ##"));
            client.setBank(bank);
            client = clientRepository.save(client);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
