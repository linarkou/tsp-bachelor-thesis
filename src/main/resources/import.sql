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

INSERT INTO users VALUES (-1, 'Linar', '$2a$11$h.zkPZjvWfwn93X8hoouTegJjxOyIXXS1h.CylrSfBSqJH/GmtzW6', 'admin');

INSERT INTO roles VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles VALUES (2, 'ROLE_DRIVER');
INSERT INTO roles VALUES (3, 'ROLE_CLIENT');

INSERT INTO user_roles VALUES (-1, 1);

-- INSERT INTO place (lat, lng, address) VALUES (54.742876, 55.960542, 'Россия, Республика Башкортостан, Уфа, улица Цюрупы, 149');
-- INSERT INTO stock (id, place_lat, place_lng) VALUES (1, 54.742876, 55.960542);
-- 
-- INSERT INTO driver (id, fullname, password, username, stock_id) VALUES (0, 'Абзалтдинов Линар', '$2a$11$74tnBEdYox.r4Fx1rSfYBugzW2cOvtgwJRX4U37tpFVfCLPpYEkx6', 'linar', 1);
-- INSERT INTO user_roles VALUES (0, 2);
-- 
-- INSERT INTO tsp.place (lat, lng, address) VALUES (54.705939, 55.997229, 'Россия, Республика Башкортостан, Уфа, улица Степана Кувыкина, 25/1');
-- INSERT INTO tsp.place (lat, lng, address) VALUES (54.721281, 55.945486, 'Россия, Республика Башкортостан, Уфа, улица Ленина, 10');
-- INSERT INTO tsp.place (lat, lng, address) VALUES (54.72503, 55.942459, 'Россия, Республика Башкортостан, Уфа, улица Карла Маркса, 12');
-- INSERT INTO tsp.place (lat, lng, address) VALUES (54.732137, 55.948774, 'Россия, Республика Башкортостан, Уфа, улица Ленина, 33/1');
-- INSERT INTO tsp.place (lat, lng, address) VALUES (54.734918, 55.931975, 'Россия, Республика Башкортостан, Уфа, улица Гафури, 101');
-- INSERT INTO tsp.place (lat, lng, address) VALUES (54.737107, 55.964979, 'Россия, Республика Башкортостан, Уфа, улица Пархоменко, 101');
-- INSERT INTO tsp.place (lat, lng, address) VALUES (54.769212, 56.034545, 'Россия, Республика Башкортостан, Уфа, бульвар Тюлькина, 3');
-- INSERT INTO tsp.place (lat, lng, address) VALUES (54.783892, 56.039252, 'Россия, Республика Башкортостан, Уфа, Российская улица, 25');
-- INSERT INTO stock (id, place_lat, place_lng) VALUES (1, 54.783892, 56.039252);
-- INSERT INTO tsp.place (lat, lng, address) VALUES (54.792468, 56.046322, 'Россия, Республика Башкортостан, Уфа, улица Шота Руставели, 25/1');
-- 
-- INSERT INTO tsp.orders (lat, lng, address) VALUES (1,54.705939, 55.997229);
-- INSERT INTO tsp.place (lat, lng, address) VALUES (2,54.721281, 55.945486);
-- INSERT INTO tsp.place (lat, lng, address) VALUES (3,54.72503, 55.942459);
-- INSERT INTO tsp.place (lat, lng, address) VALUES (4,54.732137, 55.948774);
-- INSERT INTO tsp.place (lat, lng, address) VALUES (5,54.734918, 55.931975);
-- INSERT INTO tsp.place (lat, lng, address) VALUES (6,54.737107, 55.964979);
-- INSERT INTO tsp.place (lat, lng, address) VALUES (7,54.769212, 56.034545);
-- INSERT INTO tsp.place (lat, lng, address) VALUES (8,54.783892, 56.039252);
-- INSERT INTO tsp.place (lat, lng, address) VALUES (9,54.792468, 56.046322);