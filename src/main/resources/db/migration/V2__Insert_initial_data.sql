-- src/main/resources/db/migration/V2__Insert_initial_data.sql

-- Insert initial user data into app_users
-- Make sure to replace the password with a valid bcrypt hash for 'admin'
-- You can generate a bcrypt hash using Spring Security's BCryptPasswordEncoder
-- Use a placeholder or generate a new, valid bcrypt hash.
-- Example of a valid bcrypt hash (for 'password'): $2a$10$e.g.abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890
INSERT INTO app_users (id, username, password, email, contact_number, enabled, created_date, last_updated_date) VALUES
(1, 'admin', '$2a$10$C8K1D6X5U3S2E1T0.Q9L8J7H6G5F4D3C2B1A.R9T8Y7U6I5O4P3L2K1J0', 'admin@example.com', '1234567890', TRUE, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Insert roles for the admin user - CORRECTED COLUMN NAME
INSERT INTO user_roles (user_id, role_name) VALUES
(1, 'ROLE_ADMIN'),
(1, 'ROLE_USER');

-- Insert a placeholder profile for the admin user - ENSURING 'user_id' IS PRESENT
INSERT INTO profiles (user_id, full_name, date_of_birth, gender, religion, marital_status, is_active, created_date, last_updated_date) VALUES
(1, 'Admin User', '1990-01-01', 'Male', 'Christian', 'Single', TRUE, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Insert placeholder preferences for the admin user
INSERT INTO preferences (user_id, preferred_age_min, preferred_age_max, preferred_gender, preferred_religion, preferred_marital_status, preferred_city, preferred_state, preferred_country, created_date, last_updated_date) VALUES
(1, 25, 35, 'Female', 'Christian', 'Single', 'Any', 'Any', 'Any', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());