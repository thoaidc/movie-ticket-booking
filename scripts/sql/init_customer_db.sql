-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: hdv_customer
-- -----------------------------------------------------
-- Server version	8.0.37

CREATE DATABASE IF NOT EXISTS `hdv_customer` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT ENCRYPTION='N';
USE `hdv_customer`;

DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
    `id` int NOT NULL AUTO_INCREMENT,
    `fullname` varchar(255) NOT NULL,
    `email` varchar(100) NOT NULL,
    `phone` varchar(20) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX idx_customer_name (fullName),
    INDEX idx_customer_email (email),
    INDEX idx_customer_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
