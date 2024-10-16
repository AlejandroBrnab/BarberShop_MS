-- Sample data for barbers table
INSERT INTO barbers (sin, first_name, last_name, email_address, salary, street_address, city, province, country, postal_code, phone_number, date_of_birth, is_available)VALUES('185-48-4455', 'Juan', 'Doe', 'john@example.com', 50000.00, '123 Main St', 'Anytown', 'Quebec', 'Canada', 'A1B2C3', '768-456-9749', '1990-01-01', true)

-- Sample data for barber_specialties table
INSERT INTO barber_specialties (barber_id, specialty) VALUES(1, 'Haircut'), (1, 'Shave')