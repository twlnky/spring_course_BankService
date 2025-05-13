# Полная документация банковской базы данных

## 1. Сущности и таблицы

### 1.1 Банк (Bank)
| Поле | Тип | Описание |
|------|-----|----------|
| code | CHAR(5) PRIMARY KEY | Уникальный код банка |
| name | VARCHAR(100) | Название банка |
| registration_date | DATE | Дата регистрации |
| email | VARCHAR(50) | Контактный email |
| legal_address | VARCHAR(200) | Юридический адрес |
| phone | VARCHAR(20) | Контактный телефон |

### 1.2 Сотрудник (Employee)
| Поле | Тип | Описание |
|------|-----|----------|
| id | SERIAL PRIMARY KEY | Уникальный ID |
| full_name | VARCHAR(100) | Полное имя |
| login | VARCHAR(30) UNIQUE | Логин для входа |
| password | VARCHAR(128) | Хеш пароля |
| bank_code | CHAR(5) REFERENCES Bank(code) | Банк работодатель |
| position | VARCHAR(50) | Должность |
| hire_date | DATE | Дата приема на работу |

### 1.3 Клиент (Client)
| Поле | Тип | Описание |
|------|-----|----------|
| id | SERIAL PRIMARY KEY | Уникальный ID |
| full_name | VARCHAR(100) | Полное имя |
| passport | VARCHAR(30) UNIQUE | Паспортные данные |
| snils | CHAR(11) UNIQUE | Номер СНИЛС |
| email | VARCHAR(50) | Контактный email |
| login | VARCHAR(30) UNIQUE | Логин для входа |
| password | VARCHAR(128) | Хеш пароля |
| phone | VARCHAR(20) | Телефон |
| birth_date | DATE | Дата рождения |

### 1.4 Счет (Account)
| Поле | Тип | Описание |
|------|-----|----------|
| id | SERIAL PRIMARY KEY | Уникальный ID |
| account_number | VARCHAR(20) UNIQUE | Номер счета |
| type | VARCHAR(30) | Тип счета |
| open_date | DATE | Дата открытия |
| client_id | INTEGER REFERENCES Client(id) | Владелец счета |
| bank_code | CHAR(5) REFERENCES Bank(code) | Банк счета |

### 1.5 Карта (Card)
| Поле | Тип | Описание |
|------|-----|----------|
| id | SERIAL PRIMARY KEY | Уникальный ID |
| type | VARCHAR(20) | Тип карты |
| issue_date | DATE | Дата выпуска |
| expiration_date | DATE | Срок действия |
| account_id | INTEGER REFERENCES Account(id) | Привязанный счет |
| bank_code | CHAR(5) REFERENCES Bank(code) | Банк-эмитент |
| binding_status | VARCHAR(15) | Статус привязки |

### 1.6 Банкомат (ATM)
| Поле | Тип | Описание |
|------|-----|----------|
| code | VARCHAR(10) PRIMARY KEY | Уникальный код |
| address | VARCHAR(200) | Адрес установки |
| installation_date | DATE | Дата установки |
| last_service_date | DATE | Дата обслуживания |
| status | VARCHAR(20) | Текущий статус |
| bank_code | CHAR(5) REFERENCES Bank(code) | Обслуживающий банк |

### 1.7 Операция (Operation)
| Поле | Тип | Описание |
|------|-----|----------|
| id | SERIAL PRIMARY KEY | Уникальный ID |
| operation_date | DATE | Дата операции |
| operation_time | TIME | Время операции |
| type | VARCHAR(30) | Тип операции |
| amount | DECIMAL(15,2) | Сумма |
| fee | DECIMAL(15,2) | Комиссия |
| atm_code | VARCHAR(10) REFERENCES ATM(code) | Банкомат |
| card_id | INTEGER REFERENCES Card(id) | Карта |

### 1.8 Тикет поддержки (SupportTicket)
| Поле | Тип | Описание |
|------|-----|----------|
| id | SERIAL PRIMARY KEY | Уникальный ID |
| creation_date | TIMESTAMP | Дата создания |
| status | VARCHAR(20) | Статус |
| description | TEXT | Описание проблемы |
| client_id | INTEGER REFERENCES Client(id) | Автор тикета |
| employee_id | INTEGER REFERENCES Employee(id) | Ответственный |
| close_date | TIMESTAMP | Дата закрытия |

### 1.9 Сообщение (Message)
| Поле | Тип | Описание |
|------|-----|----------|
| id | SERIAL PRIMARY KEY | Уникальный ID |
| send_time | TIMESTAMP | Время отправки |
| sender_type | VARCHAR(10) | Тип отправителя |
| text | TEXT | Текст сообщения |
| status | VARCHAR(20) | Статус |
| ticket_id | INTEGER REFERENCES SupportTicket(id) | Тикет |

## 2. Связи между таблицами

```plantuml
    BANK ||--o{ EMPLOYEE : "нанимает"
    BANK ||--o{ ATM : "обслуживает"
    CLIENT ||--o{ ACCOUNT : "владеет"
    ACCOUNT ||--o{ CARD : "привязан"
    ATM ||--o{ OPERATION : "выполняет"
    CARD ||--o{ OPERATION : "используется"


```
# CRUD-запросы

**РА**  
```postgresql
Create
--Добавить клиента и аккаунт              
{  W1 = (152, 'Иванов Иван', 'PASS123', 'SNILS-001', 'ivan@mail.com', 'ivanov', 'pass123', '+79990000000', '1990-01-01', 'B-001')
Client ← Client ∪ W1
Account ← Account ∪ { (1001, 'ACC-001', 'savings', CURRENT_DATE) x  π id, bank_code(W1)}  
}

Read
--Клиенты банка "B-001"           
π full_name (σ bank_code='B-001'(Client))

Сотрудники с >5 тикетов             
π id, full_name (
    σ count > 5 (
        γ employee_id; COUNT(id)→count (
            SupportTicket ⋈ Employee
        )
    )
)

--Активные карты с балансом >100K                 
π Card.type, Account.balance (
    σ binding_status='active' ∧ balance>100000 (
        Card ⋈ Account ⋈ Client
    )
)

--Банки без клиентов                                  
π code(Bank) − π bank_code(Client)

--Средняя комиссия по банкам                             
γ bank_code; AVG(fee)→avg_fee(Operation ⋈ ATM)

Update 
--Обновить статус банкомата и дату обслуживания              
old_atm ← σ_{code='ATM-045'}(ATM)
new_atm ← π_{
    code,
    address,
    installation_date,
    CURRENT_DATE → last_service_date,  
    'out_of_service' → status,        
    bank_code
}(old_atm)
ATM ← (ATM − old_atm) ∪ new_atm;

Delete 
Удаление клиента                  
Client ← Client - σ id=152(Client)
Удаление аккаунтов
Account ← Account - σ client_id=152(Account
Удаление карт)
Card ← Card - σ client_id=152(Card)
```
**РА**
```postgresql
-- Добавление клиента   
HOLD W(Client):
    Client.id = 152
    & Client.full_name = 'Иванов Иван'
    & Client.password = 'PASS123'
    & Client.snils = 'SNILS-001'
    & Client.email = 'ivan@mail.com'
    & Client.login = 'ivanov'
    & Client.passport = 'pass123'
    & Client.phone = '+79990000000'
    & Client.birth_date = '1990-01-01'
    & Client.bank_code = 'B-001'
    & Account:
        Account.id = 1001
        & Account.number = 'ACC-001'
        & Account.type = 'savings'
        & Account.open_date = CURRENT_DATE
        & Account.client_id = W.id
        & Account.bank_code = W.bank_code
PUT W

-- Получить клиентов банка "B-001"      
{Client.full_name | Client(Client) & Client.bank_code = 'B-001'}

-- Сотрудники с >5 тикетов              
{Employee.id, Employee.full_name | 
    Employee(Employee) 
    & ∃≥6 SupportTicket(SupportTicket.employee_id = Employee.id)}
    
--  Активные карты с балансом >100 000              
{Card.type, Account.balance | 
    Card(Card) 
    & Account(Account) 
    & Card.account_id = Account.id 
    & Card.binding_status = 'active' 
    & Account.balance > 100000}
    
-- Банки без клиентов                           
{Bank.code | Bank(Bank) & ¬∃Client(Client.bank_code = Bank.code)}

-- Средняя комиссия по банкам               
RANGE Operation O, ATM A
{ Bank.code, AVG(O.fee) | 
    O(O) 
    & A(A) 
    & O.atm_code = A.code 
    & A.bank_code = Bank.code 
    GROUP BY Bank.code }
    
-- Обновить статус банкомата                    
HOLD W(ATM): ATM.code = 'ATM-045'
W.status = 'out_of_service'
W.last_service_date = CURRENT_DATE
UPDATE W

-- Обновление связанных операций
HOLD W(Operation): Operation.atm_code = 'ATM-045'
W.status = 'paused'
UPDATE W       

-- Удаление клиента
HOLD W(Client): Client.id = 152
DELETE W

-- Удаление аккаунтов
HOLD W(Account): Account.client_id = 152
DELETE W

-- Удаление карт
HOLD W(Card): Card.client_id = 152
DELETE W
```
**SQL** 
```postgresql
-- Добавление клиента   
INSERT INTO Client (id, full_name, password, snils, email, login, passport, phone, birth_date, bank_code)
VALUES (152, 'Иванов Иван', 'PASS123', 'SNILS-001', 'ivan@mail.com', 'ivanov', 'pass123', '+79990000000', '1990-01-01', 'B-001');
-- Добавление аккаунта      
INSERT INTO Account (id, number, type, open_date, client_id, bank_code)
VALUES (1001, 'ACC-001', 'savings', CURRENT_DATE, 152, 'B-001');
-- Получить клиентов банка "B-001" с сортировкой
SELECT full_name
FROM Client
WHERE bank_code = 'B-001'
ORDER BY full_name;
-- Сотрудники с >5 тикетов              
SELECT e.id, e.full_name
FROM Employee e
WHERE (
          SELECT COUNT(*)
          FROM SupportTicket
          WHERE employee_id = e.id
      ) >= 6;
--  Активные карты с балансом >100 000              
SELECT c.type, a.balance
FROM Card c
         JOIN Account a ON c.account_id = a.id
WHERE c.binding_status = 'active'
  AND a.balance > 100000;
-- Банки без клиентов                           
SELECT code
FROM Bank
WHERE NOT EXISTS (
    SELECT 1
    FROM Client
    WHERE Client.bank_code = Bank.code
);
-- Средняя комиссия по банкам               
SELECT b.code, AVG(o.fee) AS avg_fee
FROM Operation o
         JOIN ATM a ON o.atm_code = a.code
         JOIN Bank b ON a.bank_code = b.code
GROUP BY b.code;
-- Обновить статус банкомата                    
UPDATE ATM
SET
    status = 'out_of_service',
    last_service_date = CURRENT_DATE
WHERE code = 'ATM-045';
-- Обновление связанных операций
UPDATE Operation
SET status = 'paused'
WHERE atm_code = 'ATM-045';          
-- Удаление карт
DELETE FROM Card WHERE client_id = 152;

-- Удаление аккаунтов
DELETE FROM Account WHERE client_id = 152;

-- Удаление клиента
DELETE FROM Client WHERE id = 152;


--Доп 
-- Клиенты и их аккаунты (старый синтаксис JOIN)
SELECT c.full_name, a.account_number
FROM Client c, Account a
WHERE c.id = a.client_id
  AND c.bank_code = 'B-001'
ORDER BY c.full_name;

-- Клиенты и их аккаунты (старый синтаксис JOIN)
SELECT c.full_name, a.account_number
FROM Client c, Account a
WHERE c.id = a.client_id
  AND c.bank_code = 'B-001'
ORDER BY c.full_name;

-- Клиенты с кредитными картами
SELECT full_name
FROM Client
WHERE id IN (
    SELECT client_id
    FROM Card
    WHERE type = 'credit'
)
ORDER BY full_name;

-- Количество карт каждого типа в банках
SELECT b.code, c.type, COUNT(*) AS total_cards
FROM Bank b
         JOIN Card c ON b.code = c.bank_code
GROUP BY b.code, c.type
ORDER BY b.code, total
```


## Database 
```postgresql

create TABLE bank (
    code VARCHAR(20) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    registration_date DATE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    legal_address TEXT NOT NULL,
    phone VARCHAR(50) NOT NULL
);

create TABLE employee (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    bank_code VARCHAR(20) REFERENCES bank(code) ON DELETE CASCADE,
    position VARCHAR(100) NOT NULL,
    hire_date DATE NOT NULL
);

create TABLE client (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    passport_data VARCHAR(100) UNIQUE NOT NULL,
    snils VARCHAR(14) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    birth_date DATE NOT NULL,
    bank_code VARCHAR(20) REFERENCES bank(code) ON DELETE CASCADE
);

create TABLE account (
    id SERIAL PRIMARY KEY,
    account_number VARCHAR(30) UNIQUE NOT NULL,
    type VARCHAR(50) NOT NULL,
    open_date DATE NOT NULL,
    bank_code VARCHAR(20) REFERENCES bank(code) ON DELETE CASCADE,
    client_id INT REFERENCES client(id) ON DELETE CASCADE
);



create TABLE atm (
    code VARCHAR(20) PRIMARY KEY,
    address TEXT NOT NULL,
    installation_date DATE NOT NULL,
    last_service_date DATE,
    status VARCHAR(50) NOT NULL,
    bank_code VARCHAR(20) REFERENCES bank(code) ON DELETE CASCADE
);

TABLE operation (
    id SERIAL PRIMARY KEY,
    operation_date DATE NOT NULL,
    operation_time TIME NOT NULL,
    type VARCHAR(50) NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    commission DECIMAL(15,2) NOT NULL,
    atm_code VARCHAR(20) REFERENCES atm(code) ON DELETE SET NULL
);

create TABLE card (
    id SERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    issue_date DATE NOT NULL,
    expiration_date DATE NOT NULL,
    bank_code VARCHAR(20) REFERENCES bank(code) ON DELETE CASCADE,
    client_id INT REFERENCES client(id) ON DELETE CASCADE,
    account_id INT REFERENCES account(id) ON DELETE CASCADE,
    binding_date DATE NOT NULL,
    binding_status VARCHAR(20) NOT NULL
);

create TABLE support_ticket (
    id SERIAL PRIMARY KEY,
    created_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    client_id INT REFERENCES client(id) ON DELETE CASCADE,
    employee_id INT REFERENCES employee(id) ON DELETE SET NULL,
    closed_date DATE
);


create TABLE message (
    id SERIAL PRIMARY KEY,
    sent_datetime TIMESTAMP NOT NULL,
    text TEXT NOT NULL,
    status VARCHAR(50) NOT NULL,
    ticket_id INT REFERENCES support_ticket(id) ON DELETE CASCADE,
    sender_client_id INT REFERENCES client(id) ON DELETE CASCADE,
    sender_employee_id INT REFERENCES employee(id) ON DELETE CASCADE,
    CHECK (
        (sender_client_id IS NOT NULL AND sender_employee_id IS NULL) OR
        (sender_client_id IS NULL AND sender_employee_id IS NOT NULL)
    )
);

```