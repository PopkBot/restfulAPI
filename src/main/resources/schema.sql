DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users(
id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
name VARCHAR(100) NOT NULL,
email VARCHAR(255) NOT NULL,
CONSTRAINT pk_user PRIMARY KEY (id),
CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS tasks(
id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
name VARCHAR(100) NOT NULL,
description VARCHAR NOT NULL,
start_date TIMESTAMP NOT NULL,
end_date TIMESTAMP NOT NULL,
status VARCHAR(20) NOT NULL,
user_id BIGINT NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

