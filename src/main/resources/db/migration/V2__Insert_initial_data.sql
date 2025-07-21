-- src/main/resources/db/migration/V2__Insert_initial_data.sql

-- Insert initial user data into app_users
-- Make sure to replace the password with a valid bcrypt hash for 'admin'
-- You can generate a bcrypt hash using Spring Security's BCryptPasswordEncoder
-- Use a placeholder or generate a new, valid bcrypt hash.
-- Example of a valid bcrypt hash (for 'password'): $2a$10$e.g.abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890

INSERT INTO app_users (id, username, password, email, contact_number, enabled, created_date, last_updated_date, role) VALUES
(1, 'admin', '$2a$10$mrPJkAxrnOFrDS7P62st4uKhGmCms6qyzLL9L6ACD.3WtfEbsSC.C', 'admin@example.com', '1234567890', TRUE, NOW(), NOW(), 'ROLE_ADMIN');