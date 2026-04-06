-- ========================================
-- Minimal Test Data for Finance Backend
-- ========================================

-- Clean existing data
DELETE FROM transactions;
DELETE FROM users;

-- Create 3 test users with different roles
INSERT INTO users (id, username, email, password, full_name, role, active, created_at)
VALUES (1, 'admin', 'admin@test.com', 'admin123', 'Admin User', 'ADMIN', true, NOW());

INSERT INTO users (id, username, email, password, full_name, role, active, created_at)
VALUES (2, 'analyst', 'analyst@test.com', 'analyst123', 'Analyst User', 'ANALYST', true, NOW());

INSERT INTO users (id, username, email, password, full_name, role, active, created_at)
VALUES (3, 'viewer', 'viewer@test.com', 'viewer123', 'Viewer User', 'VIEWER', true, NOW());

-- Create test transactions for admin (user_id = 1)
INSERT INTO transactions (id, user_id, amount, type, category, transaction_date, description, deleted, created_at)
VALUES (1, 1, 5000.00, 'INCOME', 'Salary', '2024-01-15', 'Monthly Salary', false, NOW());

INSERT INTO transactions (id, user_id, amount, type, category, transaction_date, description, deleted, created_at)
VALUES (2, 1, 1500.00, 'EXPENSE', 'Rent', '2024-01-20', 'Monthly Rent', false, NOW());

INSERT INTO transactions (id, user_id, amount, type, category, transaction_date, description, deleted, created_at)
VALUES (3, 1, 200.00, 'EXPENSE', 'Groceries', '2024-01-22', 'Weekly Groceries', false, NOW());

INSERT INTO transactions (id, user_id, amount, type, category, transaction_date, description, deleted, created_at)
VALUES (4, 1, 5000.00, 'INCOME', 'Salary', '2024-02-15', 'Monthly Salary', false, NOW());

INSERT INTO transactions (id, user_id, amount, type, category, transaction_date, description, deleted, created_at)
VALUES (5, 1, 1500.00, 'EXPENSE', 'Rent', '2024-02-20', 'Monthly Rent', false, NOW());

-- Create transactions for analyst (user_id = 2)
INSERT INTO transactions (id, user_id, amount, type, category, transaction_date, description, deleted, created_at)
VALUES (6, 2, 4500.00, 'INCOME', 'Salary', '2024-01-15', 'Monthly Salary', false, NOW());

INSERT INTO transactions (id, user_id, amount, type, category, transaction_date, description, deleted, created_at)
VALUES (7, 2, 1200.00, 'EXPENSE', 'Rent', '2024-01-20', 'Monthly Rent', false, NOW());

INSERT INTO transactions (id, user_id, amount, type, category, transaction_date, description, deleted, created_at)
VALUES (8, 2, 4500.00, 'INCOME', 'Salary', '2024-02-15', 'Monthly Salary', false, NOW());

-- Create transactions for viewer (user_id = 3)
INSERT INTO transactions (id, user_id, amount, type, category, transaction_date, description, deleted, created_at)
VALUES (9, 3, 3000.00, 'INCOME', 'Salary', '2024-01-15', 'Monthly Salary', false, NOW());

INSERT INTO transactions (id, user_id, amount, type, category, transaction_date, description, deleted, created_at)
VALUES (10, 3, 800.00, 'EXPENSE', 'Rent', '2024-01-20', 'Monthly Rent', false, NOW());

-- Summary of test data
SELECT 'Test Data Loaded:' as Status;
SELECT COUNT(*) as Total_Users FROM users;
SELECT COUNT(*) as Total_Transactions FROM transactions;
SELECT role, COUNT(*) as User_Count FROM users GROUP BY role;