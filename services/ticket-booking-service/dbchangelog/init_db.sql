-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: hdv_ticket_booking
-- ------------------------------------------------------
-- Server version	8.0.37

CREATE DATABASE IF NOT EXISTS `hdv_ticket_booking` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT ENCRYPTION='N';
USE `hdv_ticket_booking`;

DROP TABLE IF EXISTS `booking`;
CREATE TABLE `booking` (
    `id` int NOT NULL AUTO_INCREMENT,
    `customer_id` int NOT NULL,
    `show_id` int NOT NULL,
    `total_amount` bigint NOT NULL,
    `create_time` timestamp NOT NULL,
    `status` varchar(45) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX idx_booking_customer (customer_id),
    INDEX idx_booking_show (show_id),
    INDEX idx_booking_status (status),
    INDEX idx_booking_date (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `booking_seat`;
CREATE TABLE `booking_seat` (
    `id` int NOT NULL AUTO_INCREMENT,
    `booking_id` int NOT NULL,
    `seat_id` int NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY uk_booking_seat (booking_id, seat_id),
    INDEX idx_booking_item_booking (booking_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE BookingHistory (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `customer_id` BIGINT NOT NULL,
    `booking_id` BIGINT NOT NULL,
    `action` VARCHAR(100) NOT NULL,
    `action_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `action_by` VARCHAR(255) NOT NULL,
    CONSTRAINT fk_booking_history_booking FOREIGN KEY (booking_id) REFERENCES booking(id) ON DELETE CASCADE,
    INDEX idx_booking_history_customer (customer_id),
    INDEX idx_booking_history_booking (booking_id),
    INDEX idx_booking_history_date (action_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
