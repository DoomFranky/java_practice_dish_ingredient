CREATE DATABASE "dish_management_db";
CREATE USER "dish_manager_user" WITH PASSWORD '123456';
GRANT CREATE ON DATABASE "dish_management_db" TO "dish_manager_user";
\c dish_management_db
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO "dish_manager_user";
ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO "dish_manager_user";
