-- HMS Database Schema Setup Script
-- Run this script to initialize the database

CREATE DATABASE IF NOT EXISTS hms_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hms_db;

-- Patients Table
CREATE TABLE patients (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    disease VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone_number VARCHAR(20),
    address VARCHAR(255),
    gender VARCHAR(10),
    blood_group VARCHAR(10),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_patient_email (email),
    INDEX idx_patient_phone (phone_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Doctors Table
CREATE TABLE doctors (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone_number VARCHAR(20),
    experience_years INT,
    qualification VARCHAR(100),
    clinic_address VARCHAR(255),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    consultation_fee DECIMAL(10, 2),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_doctor_email (email),
    INDEX idx_doctor_specialization (specialization)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Appointments Table
CREATE TABLE appointments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    appointment_date DATETIME NOT NULL,
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    reason VARCHAR(255),
    notes TEXT,
    duration_minutes INT,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(id),
    INDEX idx_appointment_patient (patient_id),
    INDEX idx_appointment_doctor (doctor_id),
    INDEX idx_appointment_date (appointment_date),
    INDEX idx_appointment_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Billings Table
CREATE TABLE billings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    patient_id BIGINT NOT NULL,
    appointment_id BIGINT,
    amount DECIMAL(10, 2) NOT NULL,
    description VARCHAR(255),
    payment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    payment_method VARCHAR(50),
    payment_date DATE,
    invoice_number VARCHAR(50) UNIQUE,
    remarks TEXT,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (appointment_id) REFERENCES appointments(id),
    INDEX idx_billing_patient (patient_id),
    INDEX idx_billing_appointment (appointment_id),
    INDEX idx_billing_status (payment_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert Sample Data

-- Sample Patients
INSERT INTO patients (name, age, disease, email, phone_number, gender, blood_group, status) VALUES
('John Doe', 45, 'Diabetes', 'john@example.com', '9876543210', 'M', 'O+', 'ACTIVE'),
('Jane Smith', 38, 'Hypertension', 'jane@example.com', '9876543211', 'F', 'AB+', 'ACTIVE'),
('Robert Brown', 55, 'Heart Disease', 'robert@example.com', '9876543212', 'M', 'A+', 'ACTIVE');

-- Sample Doctors
INSERT INTO doctors (name, specialization, email, phone_number, experience_years, qualification, consultation_fee, status) VALUES
('Dr. Sharma', 'Cardiologist', 'dr.sharma@hospital.com', '8765432100', 15, 'MD Cardiology', 500.00, 'ACTIVE'),
('Dr. Patel', 'Endocrinologist', 'dr.patel@hospital.com', '8765432101', 12, 'MD Endocrinology', 400.00, 'ACTIVE'),
('Dr. Kumar', 'General Physician', 'dr.kumar@hospital.com', '8765432102', 10, 'MBBS', 300.00, 'ACTIVE');

-- Sample Appointments
INSERT INTO appointments (patient_id, doctor_id, appointment_date, status, reason, duration_minutes) VALUES
(1, 1, '2026-06-15 10:00:00', 'SCHEDULED', 'Routine checkup', 30),
(2, 2, '2026-06-16 14:00:00', 'SCHEDULED', 'Follow-up consultation', 30),
(3, 3, '2026-06-17 09:30:00', 'SCHEDULED', 'General consultation', 30);

-- Sample Billings
INSERT INTO billings (patient_id, appointment_id, amount, description, payment_status, invoice_number) VALUES
(1, 1, 500.00, 'Consultation Fees', 'PENDING', 'INV-001'),
(2, 2, 400.00, 'Consultation Fees', 'PAID', 'INV-002'),
(3, 3, 300.00, 'Consultation Fees', 'PENDING', 'INV-003');
