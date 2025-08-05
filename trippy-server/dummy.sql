-- 여기에 더미 확정된거 쌓아주세요

-- user

-- airTicket

-- voucher

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