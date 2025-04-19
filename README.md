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

```mermaid
erDiagram
    BANK ||--o{ EMPLOYEE : "нанимает"
    BANK ||--o{ ATM : "обслуживает"
    BANK ||--o{ ACCOUNT : "ведет"
    BANK ||--o{ CARD : "эмитирует"
    CLIENT ||--o{ ACCOUNT : "владеет"
    CLIENT ||--o{ CARD : "использует"
    CLIENT ||--o{ SUPPORT_TICKET : "создает"
    ACCOUNT ||--o{ CARD : "привязан"
    ATM ||--o{ OPERATION : "выполняет"
    CARD ||--o{ OPERATION : "используется"
    EMPLOYEE ||--o{ SUPPORT_TICKET : "обрабатывает"
    SUPPORT_TICKET ||--o{ MESSAGE : "содержит"



---

## Реляционная алгебра

### CRUD-операции
```relational-algebra
-- 1. Поиск клиентов и сотрудников банка
π full_name(σ bank_code='B-001'(Client)) 
∪ 
π full_name (σ bank_code='B-001'(Employee))

-- 2. Добавление нового банка
Bank ← Bank ∪ {
    ('B-100', 'New Bank', '2023-10-01', 
    'info@newbank.com', 'Moscow', '+79990000000')
}

``` 
# CRUD-запросы

## Create (C)
### Добавить нового клиента и его аккаунт
```sql
-- Реляционная алгебра
Client ← Client ∪ {
    (152, 'Иванов Иван', 'PASS123', 'SNILS-001', 'ivan@mail.com', 
    'ivanov', 'pass123', '+79990000000', '1990-01-01', 'B-001')
}

Account ← Account ∪ {
    (1001, 'ACC-001', 'savings', CURRENT_DATE, 'B-001', 152)
}

-- Реляционное исчисление
INSERT INTO Client (id, full_name, passport_data, snils, email, login, password, phone, birth_date, bank_code) 
VALUES (152, 'Иванов Иван', 'PASS123', 'SNILS-001', 'ivan@mail.com', 'ivanov', 'pass123', '+79990000000', '1990-01-01', 'B-001');

INSERT INTO Account (id, account_number, type, open_date, bank_code, client_id) 
VALUES (1001, 'ACC-001', 'savings', CURRENT_DATE, 'B-001', 152);