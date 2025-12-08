-- SQL скрипт для создания администратора
-- Email: weewee@admin.com
-- Пароль: Admin21W
-- BCrypt hash для пароля "Admin21W"

INSERT INTO users (id, email, password, first_name, last_name, phone, role, created_at, updated_at)
VALUES (
    gen_random_uuid(),
    'weewee@admin.com',
    '$2a$10$0ZPmS.PtWmBmsNADDlvqgOpN6vNpNSOcOOkEw7.3ae8KRaO6hOKSa', -- BCrypt hash для "Admin21W"
    'Admin',
    'Weewee',
    '+79999999999',
    'ADMIN',
    NOW(),
    NOW()
)
ON CONFLICT (email) DO UPDATE
SET 
    role = 'ADMIN',
    password = '$2a$10$0ZPmS.PtWmBmsNADDlvqgOpN6vNpNSOcOOkEw7.3ae8KRaO6hOKSa',
    updated_at = NOW();

