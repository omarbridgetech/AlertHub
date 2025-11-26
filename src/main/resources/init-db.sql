-- MST Alert Hub - User Microservice Database Initialization Script
-- This script creates sample roles for the system

-- Create database (if not exists)
CREATE DATABASE IF NOT EXISTS mst_user_db;
USE mst_user_db;

-- Insert sample roles
-- Note: The application will auto-create tables via JPA
-- This script should be run after the application starts at least once

-- Wait for tables to be created by Spring Boot, then run:
INSERT INTO role (role) VALUES 
    ('createAction'),
    ('updateAction'),
    ('deleteAction'),
    ('createMetric'),
    ('updateMetric'),
    ('deleteMetric'),
    ('read'),
    ('write'),
    ('admin'),
    ('viewer')
ON DUPLICATE KEY UPDATE role=role;

-- Sample user (password should be encrypted in production)
INSERT INTO users (username, email, phone, password) VALUES 
    ('admin', 'admin@mstalerthub.com', '1234567890', 'admin123')
ON DUPLICATE KEY UPDATE username=username;

-- Get the user and role IDs
SET @user_id = (SELECT id FROM users WHERE username = 'admin');
SET @admin_role_id = (SELECT id FROM role WHERE role = 'admin');
SET @read_role_id = (SELECT id FROM role WHERE role = 'read');
SET @write_role_id = (SELECT id FROM role WHERE role = 'write');

-- Assign admin roles to the admin user
INSERT IGNORE INTO user_role (user_id, role_id) VALUES 
    (@user_id, @admin_role_id),
    (@user_id, @read_role_id),
    (@user_id, @write_role_id);

SELECT 'Database initialized successfully!' as status;
