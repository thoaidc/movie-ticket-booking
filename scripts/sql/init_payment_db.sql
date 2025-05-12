-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: hdv_payment
-- ------------------------------------------------------
-- Server version	8.0.37

CREATE DATABASE IF NOT EXISTS `hdv_payment` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT ENCRYPTION='N';
USE `hdv_payment`;

DROP TABLE IF EXISTS `payment_gateway`;
CREATE TABLE `payment_gateway` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL UNIQUE,
    `status` ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    INDEX idx_payment_gateway_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `booking_id` INT NOT NULL,
    `gateway_id` INT NOT NULL,
    `transaction_id` VARCHAR(255),
    `amount` DECIMAL(10,2) NOT NULL,
    `status` ENUM('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED') NOT NULL DEFAULT 'PENDING',
    `payment_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `currency` VARCHAR(3) DEFAULT 'VND',
    CONSTRAINT fk_payment_gateway FOREIGN KEY (gateway_id) REFERENCES payment_gateway(id),
    INDEX idx_payment_booking (booking_id),
    INDEX idx_payment_status (status),
    INDEX idx_payment_transaction (transaction_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `refund`;
CREATE TABLE `refund` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `payment_id` INT NOT NULL,
    `amount` DECIMAL(10,2) NOT NULL,
    `reason` TEXT,
    `status` ENUM('PENDING', 'PROCESSED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    `refund_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_refund_payment FOREIGN KEY (payment_id) REFERENCES payment(id),
    INDEX idx_refund_payment (payment_id),
    INDEX idx_refund_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
