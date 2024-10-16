USE `clients-db`;

-- Table for Client entity
CREATE TABLE IF NOT EXISTS clients (
    id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    client_id VARCHAR(36),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email_address VARCHAR(255),
    street_address VARCHAR (50),
    city VARCHAR (50),
    province VARCHAR (50),
    country VARCHAR (50),
    postal_code VARCHAR (9),
    phone_number VARCHAR(255)
);