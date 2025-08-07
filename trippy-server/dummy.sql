-- 여기에 더미 확정된거 쌓아주세요

-- user

-- airTicket
INSERT INTO travel_log (
    user_id, title, travel_begin_date, travel_end_date,
    destination, is_generated, travel_img, created_at, updated_at
) VALUES
-- user_id = 101 (기존 유저)
(101, '서울 도보 여행', '2025-09-01 09:00:00', '2025-09-02 18:00:00', '서울 종로구', 0, 'seoul.jpg', NOW(), NOW()),
(101, '가족과 함께하는 속초 여행', '2025-10-03 08:00:00', '2025-10-05 20:00:00', '속초', 1, 'sokcho.jpg', NOW(), NOW()),

-- user_id = 102 (기존 유럽 여행자)
(102, '미국 서부 여행', '2025-04-01 12:00:00', '2025-04-15 18:00:00', 'LA, 샌프란시스코, 라스베가스', 1, 'usa_west.jpg', NOW(), NOW()),
(102, '남미 여행 계획', '2025-11-10 07:30:00', '2025-12-01 22:00:00', '페루, 볼리비아', 0, 'south_america.jpg', NOW(), NOW()),

-- user_id = 103 (부산 여행자)
(103, '경주 역사 여행', '2025-05-05 09:00:00', '2025-05-07 19:00:00', '경주', 0, 'gyeongju.jpg', NOW(), NOW()),
(103, '대구 일일 투어', '2025-08-20 10:00:00', '2025-08-20 21:00:00', '대구', 0, 'daegu.jpg', NOW(), NOW()),

-- 신규 유저 user_id = 106
(106, '홍콩 쇼핑 여행', '2025-03-10 11:00:00', '2025-03-13 23:00:00', '홍콩', 1, 'hongkong.jpg', NOW(), NOW()),
(106, '싱가포르 휴양 여행', '2025-06-20 09:00:00', '2025-06-25 21:00:00', '싱가포르', 1, 'singapore.jpg', NOW(), NOW()),

-- 신규 유저 user_id = 107
(107, '서울 근교 캠핑', '2025-07-18 14:00:00', '2025-07-20 12:00:00', '경기 양평', 0, 'camping.jpg', NOW(), NOW()),
(107, '여수 낭만 여행', '2025-08-22 10:00:00', '2025-08-24 18:00:00', '여수', 0, 'yeosu.jpg', NOW(), NOW());

INSERT INTO users (
    user_id, name, password, phone, birth, gender, email, created_at, updated_at
) VALUES (
             101, '홍낄낄', 'securePassword123', '010-1234-5678', '1990-01-01', '남성', 'hong@example.com', NOW(), NOW()
         );

-- voucher

SELECT *
FROM transaction
WHERE account_id = '0707-000060001-6511'
  AND user_id = 1;

-- account
INSERT INTO account (
    account_id, user_id, account_name, account_type, owner_id, balance, account_foreign, is_deleted, created_at, updated_at
) VALUES
      ('100-200-300001', 1001, '홍길동의 통장', '보통예금', 1001, 500000, 'kor', 'false', NOW(), NOW()),
      ('100-200-300002', 1002, '김영희 통장', '보통예금', 1002, 230000, 'kor', 'false', NOW(), NOW()),
      ('100-200-300003', 1003, '박철수 모임통장', '보통예금', 1003, 1200000, 'kor', 'false', NOW(), NOW()),
      ('200-300-400001', 1004, '이민수 외화계좌', '외화예금', 1004, 300000, 'foreign', 'false', NOW(), NOW()),
      ('100-200-300004', 1005, '최지우 생활비통장', '보통예금', 1005, 890000, 'kor', 'false', NOW(), NOW()),
      ('100-200-300005', 1001, '홍길동의 서브통장', '보통예금', 1001, 150000, 'kor', 'false', NOW(), NOW());

-- foreign_account_balance
INSERT INTO foreign_account_balance (
    account_id, currency_code, balance, created_at, updated_at
) VALUES
-- USD
('200-300-400001', 'USD', 300000, '2025-08-05 16:24:27', '2025-08-05 16:24:27'),
-- EUR
('200-300-400001', 'EUR', 250000, '2025-08-05 16:24:27', '2025-08-05 16:24:27'),
-- JPY
('200-300-400001', 'JPY', 5000000, '2025-08-05 16:24:27', '2025-08-05 16:24:27'),
-- CNY
('200-300-400001', 'CNY', 1000000, '2025-08-05 16:24:27', '2025-08-05 16:24:27'),
-- GBP
('200-300-400001', 'GBP', 180000, '2025-08-05 16:24:27', '2025-08-05 16:24:27');


-- ??
# 항공권 테이블 삭제
DROP TABLE IF EXISTS air_ticket;

# 항공권 테이블 수정
CREATE TABLE air_ticket (
                            airline_id         BIGINT       NOT NULL,  -- PK
                            user_id            BIGINT       NOT NULL,
                            reservation_code   VARCHAR(50),
                            airline            VARCHAR(100),
                            flight_number      VARCHAR(50),
                            departure_airport  VARCHAR(20),
                            arrival_airport    VARCHAR(20),
                            departure_city     VARCHAR(50),
                            arrival_city       VARCHAR(50),
                            departure_date     VARCHAR(30),
                            departure_time     VARCHAR(10),
                            boarding_time      VARCHAR(10),
                            arrival_time       VARCHAR(10),
                            terminal           VARCHAR(10),
                            gate               VARCHAR(10),
                            seat               VARCHAR(10),
                            baggage            VARCHAR(100),
                            baggage_weight     VARCHAR(10),
                            passenger_name     VARCHAR(100),
                            qr_img             TEXT,
                            is_used            BOOLEAN      DEFAULT FALSE,
                            seat_class         ENUM('ECO','BIZ','FIRST'),
                            created_at         DATETIME     DEFAULT CURRENT_TIMESTAMP,
                            updated_at         DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                            PRIMARY KEY (airline_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

# 인덱스 생성
CREATE INDEX idx_air_ticket_user_departure_date
    ON air_ticket (user_id, departure_date);

# 항공권 더미 데이터
INSERT INTO air_ticket (airline_id, user_id, reservation_code, airline, flight_number, departure_airport, arrival_airport, departure_city, arrival_city, departure_date, departure_time, boarding_time, arrival_time, terminal, gate, seat, baggage, baggage_weight, passenger_name, qr_img, is_used, seat_class, created_at, updated_at) VALUES ('1', '169', 'RSV1001', '대한항공', 'AS0639', 'ICN', 'NRT', '서울(인천)', '도쿄', '2025-08-22(토)', '22:30', '22:30', '0:30', '5', 'C3', '25B', '무료수하물 1개', '15KG', '홍길동', 'https://trippy-deploy.s3.ap-northeast-2.amazonaws.com/AIR_TICKET/default_qr.png', 0, 'FIRST', '2025-08-04 04:32:43.646208', '2025-08-04 04:32:43.646216');
INSERT INTO air_ticket (airline_id, user_id, reservation_code, airline, flight_number, departure_airport, arrival_airport, departure_city, arrival_city, departure_date, departure_time, boarding_time, arrival_time, terminal, gate, seat, baggage, baggage_weight, passenger_name, qr_img, is_used, seat_class, created_at, updated_at) VALUES ('2', '169', 'RSV1002', '대한항공', 'DZ0564', 'PUS', 'FUK', '부산', '후쿠오카', '2025-08-28(일)', '19:45', '19:45', '21:45', '1', 'C3', '28B', '수하물 2개', '20KG', '홍길동', 'https://trippy-deploy.s3.ap-northeast-2.amazonaws.com/AIR_TICKET/default_qr.png', 0, 'FIRST', '2025-08-04 04:32:43.646244', '2025-08-04 04:32:43.646248');
INSERT INTO air_ticket (airline_id, user_id, reservation_code, airline, flight_number, departure_airport, arrival_airport, departure_city, arrival_city, departure_date, departure_time, boarding_time, arrival_time, terminal, gate, seat, baggage, baggage_weight, passenger_name, qr_img, is_used, seat_class, created_at, updated_at) VALUES ('3', '169', 'RSV1003', '제주항공', 'RS0918', 'FUK', 'KIX', '후쿠오카', '오사카', '2025-08-04(월)', '17:30', '17:30', '19:30', '1', 'C3', '14B', '수하물 2개', '30KG', '홍길동', 'https://trippy-deploy.s3.ap-northeast-2.amazonaws.com/AIR_TICKET/default_qr.png', 0, 'BIZ', '2025-08-04 04:32:43.646295', '2025-08-04 04:32:43.646298');
INSERT INTO air_ticket (airline_id, user_id, reservation_code, airline, flight_number, departure_airport, arrival_airport, departure_city, arrival_city, departure_date, departure_time, boarding_time, arrival_time, terminal, gate, seat, baggage, baggage_weight, passenger_name, qr_img, is_used, seat_class, created_at, updated_at) VALUES ('4', '169', 'RSV1004', '대한항공', 'AI0336', 'FUK', 'ICN', '후쿠오카', '서울(인천)', '2025-08-09(화)', '10:15', '10:15', '12:15', '6', 'C3', '25C', '무료수하물 1개', '15KG', '홍길동', 'https://trippy-deploy.s3.ap-northeast-2.amazonaws.com/AIR_TICKET/default_qr.png', 0, 'FIRST', '2025-08-04 04:32:43.646328', '2025-08-04 04:32:43.646332');
INSERT INTO air_ticket (airline_id, user_id, reservation_code, airline, flight_number, departure_airport, arrival_airport, departure_city, arrival_city, departure_date, departure_time, boarding_time, arrival_time, terminal, gate, seat, baggage, baggage_weight, passenger_name, qr_img, is_used, seat_class, created_at, updated_at) VALUES ('5', '169', 'RSV1005', '제주항공', 'OZ0865', 'FUK', 'KIX', '후쿠오카', '오사카', '2025-08-08(수)', '18:45', '18:45', '20:45', '2', 'A1', '2C', '무료수하물 1개', '23KG', '홍길동', 'https://trippy-deploy.s3.ap-northeast-2.amazonaws.com/AIR_TICKET/default_qr.png', 0, 'ECO', '2025-08-04 04:32:43.646371', '2025-08-04 04:32:43.646383');

# 유저 더미데이터
INSERT INTO users (
    user_id,
    name,
    password,
    phone,
    birth,
    gender,
    email,
    created_at,
    updated_at
) VALUES (
             169,
             '홍길동',
             '33333',  -- 실제 운영에선 bcrypt나 SHA 등으로 암호화된 비밀번호 사용
             '010-1234-5678',
             '1990-01-01',
             'M',                -- enum 또는 문자열, GenderTypeHandler 기준
             'honggildong@example.com',
             NOW(),
             NOW()
         );


drop table id_card;
select * from id_card;
CREATE TABLE id_card
(
    user_id      BIGINT NOT NULL,
    id_card_num  VARCHAR(100),
    id_card_date VARCHAR(20),
    name         VARCHAR(100),
    address      VARCHAR(100),
    img_url      VARCHAR(200),
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

INSERT INTO id_card (user_id, id_card_num, id_card_date, name, address, img_url)
VALUES
    (1, '110101-1234567', '2015-03-10', '김민수', '서울특별시 강남구 역삼동 123-4', "a.png"),
    (2, '990101-2345678', '2012-07-22', '이서연', '부산광역시 해운대구 우동 56-7', "b.png"),
    (3, '050505-3456789', '2020-01-05', '박지후', '경기도 성남시 분당구 수내동 88-2', "c.png");


INSERT INTO passport (
    passport_number, user_id, name_kr, name_en,
    birth_date, gender, country_code, expire_date
) VALUES
      ('M12345678', 1, '김철수', 'CHULSU KIM', '1990-05-12', 'M', 'KOR', '2033-05-12 00:00:00'),
      ('M87654321', 2, '이영희', 'YOUNGHEE LEE', '1995-11-03', 'F', 'KOR', '2030-11-03 00:00:00'),
      ('M11223344', 3, '박지민', 'JIMIN PARK', '1988-02-25', 'M', 'KOR', '2029-02-25 00:00:00');

# account_member 테이블 수정 (trave_id 컬럼 추가)
DROP TABLE IF EXISTS account_member;

CREATE TABLE account_member (
                                account_id      VARCHAR(50) NOT NULL,
                                user_id         BIGINT      NOT NULL,
                                travel_id       BIGINT      NOT NULL, -- 여행 ID 연결
                                role            VARCHAR(50),
                                main_account_id VARCHAR(50),
                                created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
                                updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                PRIMARY KEY (account_id, user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

INSERT INTO account_member (account_id, user_id, travel_id, role) VALUES
                                                                      ('ACC001', 101, 1, 'owner'),
                                                                      ('ACC001', 102, 1, 'member');
INSERT INTO account_member (account_id, user_id, travel_id, role) VALUES
                                                                      ('ACC002', 102, 2, 'owner'),
                                                                      ('ACC002', 103, 2, 'member');
INSERT INTO account_member (account_id, user_id, travel_id, role) VALUES
    ('ACC003', 103, 3, 'owner');
INSERT INTO account_member (account_id, user_id, travel_id, role) VALUES
                                                                      ('ACC004', 104, 4, 'owner'),
                                                                      ('ACC004', 105, 4, 'member');
INSERT INTO account_member (account_id, user_id, travel_id, role) VALUES
                                                                      ('ACC005', 105, 5, 'owner'),
                                                                      ('ACC005', 106, 5, 'member');

INSERT INTO account_member (account_id, user_id, travel_id, role) VALUES
    ('ACC006', 101, 6, 'owner');
INSERT INTO account_member (account_id, user_id, travel_id, role) VALUES
                                                                      ('ACC007', 101, 7, 'owner'),
                                                                      ('ACC007', 107, 7, 'member');
INSERT INTO account_member (account_id, user_id, travel_id, role) VALUES
                                                                      ('ACC008', 102, 8, 'owner'),
                                                                      ('ACC008', 108, 8, 'member');
INSERT INTO account_member (account_id, user_id, travel_id, role) VALUES
                                                                      ('ACC009', 102, 9, 'owner'),
                                                                      ('ACC009', 109, 9, 'member');
INSERT INTO account_member (account_id, user_id, travel_id, role) VALUES
                                                                      ('ACC010', 103, 10, 'owner'),
                                                                      ('ACC010', 101, 10, 'member');
INSERT INTO account_member (account_id, user_id, travel_id, role) VALUES
    ('ACC011', 103, 11, 'owner');
INSERT INTO account_member (account_id, user_id, travel_id, role) VALUES
                                                                      ('ACC012', 106, 12, 'owner'),
                                                                      ('ACC012', 102, 12, 'member');
INSERT INTO account_member (account_id, user_id, travel_id, role) VALUES
    ('ACC013', 106, 13, 'owner');
INSERT INTO account_member (account_id, user_id, travel_id, role) VALUES
                                                                      ('ACC014', 107, 14, 'owner'),
                                                                      ('ACC014', 103, 14, 'member');
INSERT INTO account_member (account_id, user_id, travel_id, role) VALUES
    ('ACC015', 107, 15, 'owner');


