-- 여기에 더미 확정된거 쌓아주세요

-- user
INSERT INTO users (name, password, phone, birth, gender, email, created_at, updated_at)
VALUES ('강예성', '123456', '010-1234-5678', '1995-08-03', 'M', 'yesung.kang@example.com', NOW(), NOW());
-- airTicket

-- voucher

delete
from account_member
where user_id = 1;
