CREATE DATABASE bank_accounts;
\c bank_accounts;

CREATE TABLE admin
(
id INT PRIMARY KEY NOT NULL,
login CHAR(10)
);

CREATE TABLE users
(
id INT PRIMARY KEY NOT NULL,
first_name CHAR(10),
last_name CHAR(10),
iban CHAR(31)
);

CREATE TABLE restriction
(
id INT PRIMARY KEY NOT NULL,
admin_id INT references admin(id),
user_id INT references users(id),
delete BOOLEAN NOT NULL,
list BOOLEAN NOT NULL,
update BOOLEAN NOT NULL
);