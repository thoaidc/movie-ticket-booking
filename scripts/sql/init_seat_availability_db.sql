-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: hdv_seat_availability
-- ------------------------------------------------------
-- Server version	8.0.37

CREATE DATABASE IF NOT EXISTS `hdv_seat_availability` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT ENCRYPTION='N';
USE `hdv_seat_availability`;

DROP TABLE IF EXISTS `seat_show`;
CREATE TABLE `seat_show` (
    `id` int NOT NULL AUTO_INCREMENT,
    `seat_id` int NOT NULL,
    `show_id` int NOT NULL,
    `status` ENUM('AVAILABLE', 'RESERVED', 'BOOKED') NOT NULL DEFAULT 'AVAILABLE',
    PRIMARY KEY (`id`),
    INDEX idx_seat_room (seat_id),
    INDEX idx_seat_show (show_id),
    INDEX idx_seat_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
