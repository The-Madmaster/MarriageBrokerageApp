-- Version 1: Create all application tables

CREATE TABLE app_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    contact_number VARCHAR(255),
    role VARCHAR(255) NOT NULL,
    account_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
    account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,
    credentials_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_date TIMESTAMP,
    last_updated_date TIMESTAMP
);

CREATE TABLE broker_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    app_user_id BIGINT NOT NULL UNIQUE,
    broker_name VARCHAR(255) NOT NULL,
    firm_name VARCHAR(255),
    registration_number VARCHAR(255) NOT NULL UNIQUE,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    pincode VARCHAR(10) NOT NULL,
    firm_contact_number VARCHAR(20),
    created_date TIMESTAMP,
    last_updated_date TIMESTAMP,
    FOREIGN KEY (app_user_id) REFERENCES app_users(id) ON DELETE CASCADE
);

-- THIS IS THE COMPLETE VERSION OF THE CLIENT_PROFILES TABLE
CREATE TABLE client_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    broker_id BIGINT NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    date_of_birth DATE,
    gender VARCHAR(20),
    marital_status VARCHAR(50),
    height_cm INTEGER,
    religion VARCHAR(100),
    caste VARCHAR(100),
    sub_caste VARCHAR(100),
    mother_tongue VARCHAR(50),
    country VARCHAR(100),
    state VARCHAR(100),
    city VARCHAR(100),
    complexion VARCHAR(50),
    body_type VARCHAR(50),
    education VARCHAR(255),
    occupation VARCHAR(255),
    annual_income DOUBLE PRECISION,
    diet VARCHAR(50),
    smoking_habit VARCHAR(50),
    drinking_habit VARCHAR(50),
    about_me VARCHAR(1000),
    photo_url VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    preferred_partner_min_age INTEGER,
    preferred_partner_max_age INTEGER,
    preferred_partner_religion VARCHAR(100),
    preferred_partner_caste VARCHAR(100),
    preferred_partner_min_height_cm INTEGER,
    preferred_partner_max_height_cm INTEGER,
    created_date TIMESTAMP,
    last_updated_date TIMESTAMP,
    FOREIGN KEY (broker_id) REFERENCES app_users(id) ON DELETE CASCADE
);