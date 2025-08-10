use trippy_db;
-- account_member
CREATE TABLE account_member
(
    account_id      VARCHAR(50) NOT NULL,
    user_id         BIGINT      NOT NULL,
    role            VARCHAR(50),
    main_account_id VARCHAR(50),
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (account_id, user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


-- account
CREATE TABLE account
(
    account_id      VARCHAR(50)  NOT NULL,
    user_id         BIGINT       NOT NULL,
    account_name    VARCHAR(100) NOT NULL,
    account_type    VARCHAR(50),
    owner_id        BIGINT,
    balance         BIGINT   DEFAULT 0,
    account_foreign VARCHAR(20),
    is_deleted      VARCHAR(20),
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (account_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

Drop table if exists foreign_account_balance;

-- foreign_account_balance
-- account_id 형식 수정
CREATE TABLE foreign_account_balance
(
    balance_id    BIGINT NOT NULL AUTO_INCREMENT,
    account_id    VARCHAR(50) NOT NULL,
    currency_code VARCHAR(10),
    balance       BIGINT   DEFAULT 0,
    created_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (balance_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- card
CREATE TABLE card
(
    card_id       BIGINT      NOT NULL AUTO_INCREMENT,
    user_id       BIGINT      NOT NULL,
    account_id    VARCHAR(50) NOT NULL,
    card_number   VARCHAR(50),
    card_name     VARCHAR(100),
    card_nickname VARCHAR(100),
    field         INT,
    is_main_card  TINYINT(1) DEFAULT 0,
    card_img      VARCHAR(255),
    created_at    DATETIME   DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (card_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- exchangelog Mapper
CREATE TABLE exchange_log
(
    exchange_id           BIGINT      NOT NULL AUTO_INCREMENT,
    account_id            VARCHAR(50) NOT NULL,
    amount_krw            BIGINT,
    amount_foreign        BIGINT,
    applied_exchange_rate BIGINT,
    nation                VARCHAR(100),
    currency_code         VARCHAR(10),
    exchange_type         VARCHAR(50),
    created_at            DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at            DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (exchange_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- exchange rate mapper
DROP table if exists exchange_rate;

CREATE TABLE exchange_rate
(
    exchange_rate_id   BIGINT NOT NULL AUTO_INCREMENT,
    currency_code      VARCHAR(10),
    currency_name      VARCHAR(30),
    base_exchange_rate DOUBLE,
    rate_buy           DOUBLE,
    rate_sell          DOUBLE,
    exchange_rate_date DATETIME,
    created_at         DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at         DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (exchange_rate_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- id card
CREATE TABLE id_card
(
    user_id      BIGINT NOT NULL,
    id_card_num  VARCHAR(100),
    id_card_date VARCHAR(20),
    name         VARCHAR(100),
    address      VARCHAR(100),
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- passport
CREATE TABLE passport
(
    passport_number VARCHAR(30) NOT NULL,
    user_id         BIGINT      NOT NULL,
    name_kr         VARCHAR(100),
    name_en         VARCHAR(100),
    birth_date      DATE,
    gender          VARCHAR(10),
    country_code    VARCHAR(10),
    expire_date     DATETIME,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (passport_number)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


-- notification
CREATE TABLE notification
(
    noti_id    BIGINT NOT NULL AUTO_INCREMENT,
    user_id    BIGINT NOT NULL,
    title      VARCHAR(200),
    content    TEXT,
    noti_type  VARCHAR(50),
    amount     BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (noti_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


-- transaction
CREATE TABLE transaction
(
    transaction_id   BIGINT      NOT NULL AUTO_INCREMENT,
    account_id       VARCHAR(50) NOT NULL,
    user_id          BIGINT      NOT NULL,
    transaction_type VARCHAR(50),
    amount           BIGINT,
    title            VARCHAR(200),
    category         VARCHAR(50),
    latitude         VARCHAR(30),
    longitude        VARCHAR(30),
    balance_after    BIGINT,
    status           VARCHAR(50),
    currency_code    VARCHAR(10),
    created_at       DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (transaction_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- travellog
CREATE TABLE travel_log
(
    travel_id         BIGINT NOT NULL AUTO_INCREMENT,
    user_id           BIGINT NOT NULL,
    title             VARCHAR(200),
    travel_begin_date DATETIME,
    travel_end_date   DATETIME,
    destination       VARCHAR(200),
    is_generated      TINYINT(1) DEFAULT 0,
    travel_img        VARCHAR(255),
    created_at        DATETIME   DEFAULT CURRENT_TIMESTAMP,
    updated_at        DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (travel_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- travel report
CREATE TABLE travel_report
(
    settlement_id   BIGINT      NOT NULL AUTO_INCREMENT,
    account_id      VARCHAR(50) NOT NULL,
    user_id         BIGINT      NOT NULL,
    travel_id       BIGINT      NOT NULL,
    total_expense   INT      DEFAULT 0,
    total_food      INT      DEFAULT 0,
    total_activity  INT      DEFAULT 0,
    total_acc       INT      DEFAULT 0,
    total_transport INT      DEFAULT 0,
    total_shop      INT      DEFAULT 0,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (settlement_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- user
CREATE TABLE users
(
    user_id    BIGINT NOT NULL AUTO_INCREMENT,
    name       VARCHAR(100),
    password   VARCHAR(255),
    phone      VARCHAR(20),
    birth      DATE,
    gender     VARCHAR(10),
    email      VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- accommodation
CREATE TABLE accommodation
(
    reservation_code       VARCHAR(50) NOT NULL,
    user_id                BIGINT      NOT NULL,
    accommodation_name     VARCHAR(200),
    room_name              VARCHAR(200),
    reservation_start_date DATE,
    reservation_end_date   DATE,
    check_in               DATETIME,
    check_out              DATETIME,
    created_at             DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at             DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (reservation_code)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


-- airTicekt
CREATE TABLE air_ticket
(
    reservation_code  VARCHAR(50) NOT NULL,
    user_id           BIGINT      NOT NULL,
    airline_id        BIGINT,
    airline           VARCHAR(100),
    flight_number     VARCHAR(50),
    departure_airport VARCHAR(100),
    arrival_airport   VARCHAR(100),
    departure_city    VARCHAR(100),
    arrival_city      VARCHAR(100),
    departure_date    VARCHAR(20),
    departure_time    VARCHAR(20),
    boarding_time     VARCHAR(20),
    arrival_time      VARCHAR(20),
    terminal          VARCHAR(20),
    gate              VARCHAR(20),
    seat              VARCHAR(20),
    baggage           VARCHAR(100),
    passenger_name    VARCHAR(100),
    qr_img            VARCHAR(255),
    is_used           TINYINT(1) DEFAULT 0,
    ticket_img        VARCHAR(255),
    seat_class        VARCHAR(50),
    created_at        DATETIME   DEFAULT CURRENT_TIMESTAMP,
    updated_at        DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (reservation_code)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- sightSeeing
CREATE TABLE sightseeing
(
    voucher_id   BIGINT NOT NULL AUTO_INCREMENT,
    user_id      BIGINT NOT NULL,
    voucher_img  VARCHAR(255),
    name         VARCHAR(200),
    viewing_date DATETIME,
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (voucher_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

--
alter table account
    change account_foreign account_currency VARCHAR(20)


select count(*) from account
where user_id = 1 and account_type = 'group' and is_deleted = 'N';