DROP TABLE IF EXISTS air_ticket;

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


CREATE INDEX idx_air_ticket_user_departure_date
    ON air_ticket (user_id, departure_date);

INSERT INTO air_ticket (airline_id, user_id, reservation_code, airline, flight_number, departure_airport, arrival_airport, departure_city, arrival_city, departure_date, departure_time, boarding_time, arrival_time, terminal, gate, seat, baggage, baggage_weight, passenger_name, qr_img, is_used, seat_class, created_at, updated_at) VALUES ('1', '169', 'RSV1001', '대한항공', 'AS0639', 'ICN', 'NRT', '서울(인천)', '도쿄', '2025-08-22(토)', '22:30', '22:30', '0:30', '5', 'C3', '25B', '무료수하물 1개', '15KG', '홍길동', 'https://trippy-deploy.s3.ap-northeast-2.amazonaws.com/AIR_TICKET/default_qr.png', 0, 'FIRST', '2025-08-04 04:32:43.646208', '2025-08-04 04:32:43.646216');
INSERT INTO air_ticket (airline_id, user_id, reservation_code, airline, flight_number, departure_airport, arrival_airport, departure_city, arrival_city, departure_date, departure_time, boarding_time, arrival_time, terminal, gate, seat, baggage, baggage_weight, passenger_name, qr_img, is_used, seat_class, created_at, updated_at) VALUES ('2', '169', 'RSV1002', '대한항공', 'DZ0564', 'PUS', 'FUK', '부산', '후쿠오카', '2025-08-28(일)', '19:45', '19:45', '21:45', '1', 'C3', '28B', '수하물 2개', '20KG', '홍길동', 'https://trippy-deploy.s3.ap-northeast-2.amazonaws.com/AIR_TICKET/default_qr.png', 0, 'FIRST', '2025-08-04 04:32:43.646244', '2025-08-04 04:32:43.646248');
INSERT INTO air_ticket (airline_id, user_id, reservation_code, airline, flight_number, departure_airport, arrival_airport, departure_city, arrival_city, departure_date, departure_time, boarding_time, arrival_time, terminal, gate, seat, baggage, baggage_weight, passenger_name, qr_img, is_used, seat_class, created_at, updated_at) VALUES ('3', '169', 'RSV1003', '제주항공', 'RS0918', 'FUK', 'KIX', '후쿠오카', '오사카', '2025-08-04(월)', '17:30', '17:30', '19:30', '1', 'C3', '14B', '수하물 2개', '30KG', '홍길동', 'https://trippy-deploy.s3.ap-northeast-2.amazonaws.com/AIR_TICKET/default_qr.png', 0, 'BIZ', '2025-08-04 04:32:43.646295', '2025-08-04 04:32:43.646298');
INSERT INTO air_ticket (airline_id, user_id, reservation_code, airline, flight_number, departure_airport, arrival_airport, departure_city, arrival_city, departure_date, departure_time, boarding_time, arrival_time, terminal, gate, seat, baggage, baggage_weight, passenger_name, qr_img, is_used, seat_class, created_at, updated_at) VALUES ('4', '169', 'RSV1004', '대한항공', 'AI0336', 'FUK', 'ICN', '후쿠오카', '서울(인천)', '2025-08-09(화)', '10:15', '10:15', '12:15', '6', 'C3', '25C', '무료수하물 1개', '15KG', '홍길동', 'https://trippy-deploy.s3.ap-northeast-2.amazonaws.com/AIR_TICKET/default_qr.png', 0, 'FIRST', '2025-08-04 04:32:43.646328', '2025-08-04 04:32:43.646332');
INSERT INTO air_ticket (airline_id, user_id, reservation_code, airline, flight_number, departure_airport, arrival_airport, departure_city, arrival_city, departure_date, departure_time, boarding_time, arrival_time, terminal, gate, seat, baggage, baggage_weight, passenger_name, qr_img, is_used, seat_class, created_at, updated_at) VALUES ('5', '169', 'RSV1005', '제주항공', 'OZ0865', 'FUK', 'KIX', '후쿠오카', '오사카', '2025-08-08(수)', '18:45', '18:45', '20:45', '2', 'A1', '2C', '무료수하물 1개', '23KG', '홍길동', 'https://trippy-deploy.s3.ap-northeast-2.amazonaws.com/AIR_TICKET/default_qr.png', 0, 'ECO', '2025-08-04 04:32:43.646371', '2025-08-04 04:32:43.646383');

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