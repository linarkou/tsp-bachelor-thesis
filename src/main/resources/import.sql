-- -- Table: users
-- CREATE TABLE users (
--   id       INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
--   username VARCHAR(255) NOT NULL,
--   password VARCHAR(255) NOT NULL
-- );
-- 
-- -- Table: roles
-- CREATE TABLE roles (
--   id   INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
--   name VARCHAR(100) NOT NULL
-- );
-- 
-- -- Table for mapping user and roles: user_roles
-- CREATE TABLE user_roles (
--   user_id INT NOT NULL,
--   role_id INT NOT NULL,
-- 
--   FOREIGN KEY (user_id) REFERENCES users (id),
--   FOREIGN KEY (role_id) REFERENCES roles (id),
-- 
--   UNIQUE (user_id, role_id)
-- );

-- Charset settings

ALTER TABLE Client CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;
ALTER TABLE Driver CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;
ALTER TABLE Orders CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;
ALTER TABLE Place CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;
ALTER TABLE roles CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;
ALTER TABLE Route CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;
ALTER TABLE Stock CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;
ALTER TABLE users CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;

-- Insert data

INSERT INTO users VALUES (0, 'Linar', '$2a$11$h.zkPZjvWfwn93X8hoouTegJjxOyIXXS1h.CylrSfBSqJH/GmtzW6', 'admin');

INSERT INTO roles VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles VALUES (2, 'ROLE_DRIVER');
INSERT INTO roles VALUES (3, 'ROLE_CLIENT');

INSERT INTO user_roles VALUES (0, 1);

INSERT INTO place (lat, lng, address) VALUES (54.742876, 55.960542, 'Россия, Республика Башкортостан, Уфа, улица Цюрупы, 149');
INSERT INTO stock (id, place_lat, place_lng) VALUES (1, 54.742876, 55.960542);

INSERT INTO driver (id, fullname, password, username, stock_id) VALUES (1, 'Абзалтдинов Линар', '$2a$11$74tnBEdYox.r4Fx1rSfYBugzW2cOvtgwJRX4U37tpFVfCLPpYEkx6', 'linar', 1);
INSERT INTO user_roles VALUES (1, 2);