
DROP TABLE IF EXISTS barbers;
-- Table for Barber entity
CREATE TABLE IF NOT EXISTS barbers (
    id SERIAL,
    sin VARCHAR(36) UNIQUE,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email_address VARCHAR(50),
    salary DECIMAL(19, 2),
    street_address VARCHAR (50),
    city VARCHAR (50),
    province VARCHAR (50),
    country VARCHAR (50),
    postal_code VARCHAR (9),
    phone_number VARCHAR(50),
    date_of_birth DATE,
    is_Available BOOLEAN,
    PRIMARY KEY(id)
    );

DROP TABLE IF EXISTS barber_specialties;
-- Table for Specialty value object
CREATE TABLE IF NOT EXISTS barber_specialties (
   barber_id INTEGER,
   specialty VARCHAR(255)
);