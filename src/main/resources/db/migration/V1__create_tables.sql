CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL CHECK (LENGTH(password) >= 8),
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       is_active BOOLEAN NOT NULL DEFAULT true,
                       roles text[] NOT NULL
);