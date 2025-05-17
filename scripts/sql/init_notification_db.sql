-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: hdv_notification
-- ------------------------------------------------------
-- Server version	8.0.37

CREATE DATABASE IF NOT EXISTS `hdv_notification` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT ENCRYPTION='N';
USE `hdv_notification`;

DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification` (
    `id` int NOT NULL AUTO_INCREMENT,
    `sender` varchar(255) DEFAULT NULL,
    `receiver` varchar(100) NOT NULL,
    `title` varchar(255) NOT NULL,
    `content` VARCHAR(1000) NOT NULL,
    `status` ENUM('PROCESSING', 'SUCCESS', 'FAILED') NOT NULL DEFAULT 'PROCESSING',
    `sent_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX idx_receiver (receiver),
    INDEX idx_status (status),
    INDEX idx_sent_at (sent_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
