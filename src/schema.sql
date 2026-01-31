-- Hotel Management System Database Schema

CREATE DATABASE IF NOT EXISTS hotel;
USE hotel;

-- Customer Table
CREATE TABLE IF NOT EXISTS CUSTOMER (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    p_number VARCHAR(15) NOT NULL,
    id_proof VARCHAR(50) NOT NULL
);

-- Rooms Table
CREATE TABLE IF NOT EXISTS ROOMS (
    room_id INT PRIMARY KEY,
    room_type VARCHAR(50) NOT NULL,
    price_per_day DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'available'
);

-- Booking Table
CREATE TABLE IF NOT EXISTS BOOKING (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    room_id INT NOT NULL,
    check_in DATETIME NOT NULL,
    check_out DATETIME,
    total_amount DECIMAL(10,2) DEFAULT 0,
    status VARCHAR(20) DEFAULT 'active',
    FOREIGN KEY (customer_id) REFERENCES CUSTOMER(customer_id),
    FOREIGN KEY (room_id) REFERENCES ROOMS(room_id)
);

-- Sample Data
INSERT INTO ROOMS (room_id, room_type, price_per_day, status) VALUES
(101, 'Single', 1000.00, 'available'),
(102, 'Single', 1000.00, 'available'),
(201, 'Double', 1800.00, 'available'),
(202, 'Double', 1800.00, 'available'),
(301, 'Suite', 3500.00, 'available'),
(302, 'Suite', 3500.00, 'available');
