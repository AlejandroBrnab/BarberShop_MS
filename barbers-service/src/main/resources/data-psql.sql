-- Sample data for barbers table
INSERT INTO barbers (sin, first_name, last_name, email_address, salary, street_address, city, province, country, postal_code, phone_number, date_of_birth, is_available)VALUES
('185-48-4455', 'John', 'Doe', 'john@example.com', 50000.00, '123 Main St', 'Anytown', 'Quebec', 'Canada', 'A1B2C3', '768-456-9749', '1990-01-01', true),
('575-03-7490', 'Jane', 'Smith', 'jane@example.com', 60000.00, '456 Elm St', 'Othertown', 'Quebec', 'Canada', 'X1Y2Z3', '729-560-2582', '1985-05-15', true),
('738-25-1672', 'Michael', 'Williams', 'michael@example.com', 55000.00, '789 Pine St', 'Anothertown', 'Quebec', 'Canada', 'P5Q6R7', '987-654-3210', '1988-08-08', true),
('372-38-4335', 'Emily', 'Brown', 'emily@example.com', 48000.00, '987 Cedar St', 'Yetanothertown', 'Quebec', 'Canada', 'L8M9N0', '555-555-5555', '1995-03-25', true),
('782-03-6569', 'Christopher', 'Taylor', 'chris@example.com', 52000.00, '111 Oak St', 'Metropolis', 'Quebec', 'Canada', 'M1N2O3', '123-456-7890', '1992-04-12', true),
('660-43-2363', 'Samantha', 'Clark', 'samantha@example.com', 49000.00, '222 Maple St', 'Smallville', 'Quebec', 'Canada', 'S4T5U6', '681-783-6134', '1993-09-20', true),
('731-03-3948', 'Michael', 'Johnson', 'michael.j@example.com', 48000.00, '555 Pine St', 'Cityville', 'Quebec', 'Canada', 'P5Q6R7', '317-379-9169', '1990-06-15', true),
('542-21-6474', 'Jessica', 'Brown', 'jessica.b@example.com', 52000.00, '666 Elm St', 'Townburg', 'Quebec', 'Canada', 'L8M9N0', '854-723-4659', '1987-02-28', true),
('886-19-9440', 'Alex', 'Martinez', 'alex.m@example.com', 53000.00, '111 Pine St', 'Smalltown', 'Quebec', 'Canada', 'S1T2U3', '915-707-2103', '1985-11-25', true),
('246-81-3579', 'Michelle', 'Lopez', 'michelle.l@example.com', 50000.00, '222 Elm St', 'Hometown', 'Quebec', 'Canada', 'H4I5J6', '197-823-8977', '1991-07-18', true);

-- Sample data for barber_specialties table
INSERT INTO barber_specialties (barber_id, specialty) VALUES
(1, 'Haircut'),
(1, 'Shave'),
(2, 'Coloring'),
(2, 'Haircut'),
(2, 'Highlights'),
(3, 'Beard Trim'),
(3, 'Facial'),
(4, 'Perm'),
(4, 'Facial'),
(5, 'Waxing'),
(5, 'Facial'),
(6, 'Hair Styling'),
(6, 'Haircut'),
(7, 'Trimming'),
(8, 'Coloring'),
(8, 'Highlights'),
(8, 'Haircut'),
(9, 'Shaving'),
(9, 'Beard Styling'),
(10, 'Manicure'),
(10, 'Hair Coloring'),
(10, 'Hair Extensions');