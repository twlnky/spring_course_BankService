package rut.miit.tech.web.repository;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rut.miit.tech.web.domain.model.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Random;

@Profile("dev")
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final BankRepository bankRepository;
    private final AccountRepository accountRepository;
    private final AtmRepository atmRepository;
    private final CardRepository cardRepository;
    private final EmployeeRepository employeeRepository;
    private final MessageRepository messageRepository;
    private final OperationRepository operationRepository;
    private final SupportTicketRepository supportTicketRepository;
    @Value("${test.records.count}")
    private Integer testRecords = 0;

    @Transactional
    @Override
    public void run(String... args) throws Exception {


        try {
            Faker faker = new Faker(new Locale("en"));




            bankRepository.deleteAll();
            clientRepository.deleteAll();
            for (int i = 0; i < testRecords; i++) {
                Bank bank = new Bank();
                bank.setRegistrationDate(Timestamp.from(Instant.now()));
                bank.setLegalAddress(faker.address().fullAddress());
                bank.setName(faker.company().name() + " Bank");
                bank.setEmail(faker.internet().emailAddress());
                bank.setCode(faker.internet().password(1, 19));
                bank.setPhone(faker.phoneNumber().phoneNumber());
                bank = bankRepository.save(bank);

                Client client = new Client();
                client.setLogin(faker.name().username());
                client.setPassportData(faker.numerify("#### ######"));
                client.setPassword(passwordEncoder.encode(faker.internet().password()));
                String clientPhone = faker.phoneNumber().phoneNumber();
                if (clientPhone.length() > 20) {
                    clientPhone = clientPhone.substring(0, 20);
                }
                client.setPhone(clientPhone);
                client.setEmail(faker.internet().emailAddress());
                client.setEnable(randStatusBoolean());
                client.setFullName(faker.funnyName().name());
                client.setBirthDate(new Date(System.currentTimeMillis()));
                client.setSnils(faker.numerify("###-###-### ##"));
                client.setBank(bank);
                client = clientRepository.save(client);

                Atm atm = new Atm();
                atm.setBank(bank);
                atm.setCode(faker.internet().password(1,19));
                atm.setAddress(faker.address().fullAddress());
                atm.setStatus(randStatusBoolean());
                atm.setInstallationDate(Timestamp.from(Instant.now()));
                atm.setInstallationDate(Timestamp.from(Instant.now()));
                atm = atmRepository.save(atm);

                Account account = new Account();
                account.setBank(bank);
                account.setClient(client);
                account.setAccountNumber(faker.numerify("#### ######"));
                account.setType("account");
                account.setOpenDate(Timestamp.from(Instant.now()));
                account = accountRepository.save(account);
                account = accountRepository.save(account);

                Card card = new Card();
                card.setBank(bank);
                card.setAccount(account);
                card.setClient(client);
                card.setType(faker.business().creditCardType());
                card.setIssueDate(Timestamp.from(Instant.now()));
                card.setBindingDate(Timestamp.from(Instant.now()));
                card.setBindingStatus(randStatusString());
                card.setExpirationDate(Timestamp.from(Instant.now()));
                card = cardRepository.save(card);

                Employee employee = new Employee();
                employee.setBank(bank);
                employee.setPassword(passwordEncoder.encode(faker.internet().password()));
                employee.setLogin(faker.name().username());
                employee.setFullName(faker.name().fullName());
                employee.setPosition(faker.job().position());
                employee.setHireDate(Timestamp.from(Instant.now()));
                employee = employeeRepository.save(employee);

                SupportTicket supportTicket = new SupportTicket();
                supportTicket.setClient(client);
                supportTicket.setCreatedDate(Timestamp.from(Instant.now()));
                supportTicket.setDescription(faker.lorem().sentence());
                String massageText = supportTicket.getDescription();
                supportTicket.setStatus(randStatusString());
                supportTicket.setEmployee(employee);
                supportTicket.setClosedDate(faker.date().past(12, java.util.concurrent.TimeUnit.DAYS));
                supportTicket = supportTicketRepository.save(supportTicket);




                Message message = new Message();
                message.setStatus(randStatusString());
                message.setText(massageText);
                message.setTicket(supportTicket);
                message.setSenderEmployee(employee);
                message.setSentDatetime(Timestamp.from(Instant.now()));
                message = messageRepository.save(message);

                Operation operation = new Operation();
                operation.setType(faker.business().creditCardType());
                operation.setOperationDate(Timestamp.from(Instant.now()));
                operation.setOperationTime(Time.valueOf(LocalTime.of(12, 23)));
                operation.setAmount(BigDecimal.valueOf(Double.parseDouble(String.valueOf(faker.number().randomDouble(2, 100, 1000)))));
                operation.setCommission(BigDecimal.valueOf(Double.parseDouble(String.valueOf(faker.number().randomDouble(2, 1, 10)))));
                operation.setAtm(atm);
                operation = operationRepository.save(operation);




            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String randStatusString(){
        Random random = new Random();
        boolean bindingStatus = random.nextBoolean();
        return bindingStatus ? "true" : "false";
    }
    public boolean randStatusBoolean(){
        Random random = new Random();
        return random.nextBoolean();
    }
}
