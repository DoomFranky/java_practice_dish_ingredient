CREATE DATABASE "mini_dish_db";

CREATE USER "mini_dish_db_manager" WITH PASSWORD '123456';

GRANT CREATE ON DATABASE "mini_dish_db" TO "mini_dish_db_manager";

\c mini_dish_db

GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO "mini_dish_db_manager";

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO "mini_dish_db_manager";
