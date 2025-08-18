-- Inserts a default admin user with a pre-hashed password ('password')
-- and sets the role to ROLE_ADMIN.
-- All boolean columns are implicitly set to their default value (TRUE).
INSERT INTO app_users (email, password, contact_number, role)
VALUES (
    'admin@example.com',
    '$2a$10$ZrOlDHXKj2FNGd/dm/o7Se7IVRZpbY4g397HQE0K0dbJChtLYeCgK',
    '0000000000',
    'ROLE_ADMIN'
);