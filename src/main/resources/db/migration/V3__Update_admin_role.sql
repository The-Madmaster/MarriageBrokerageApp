-- src/main/resources/db/migration/V3__Update_admin_role.sql

UPDATE app_users
SET role = 'ROLE_ADMIN'
WHERE id = 1;