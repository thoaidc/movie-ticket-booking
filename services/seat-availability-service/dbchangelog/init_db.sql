-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: hdv_seat_movie
-- ------------------------------------------------------
-- Server version	8.0.37

CREATE DATABASE IF NOT EXISTS `hdv_seat_movie` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT ENCRYPTION='N';
USE `hdv_seat_movie`;

DROP TABLE IF EXISTS `seat_lock`;
CREATE TABLE `seat_lock` (
    `id` int NOT NULL AUTO_INCREMENT,
    `seat_id` INT NOT NULL,
    `show_id` int NOT NULL,
    `lock_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX idx_seat_lock_seat_show (seat_id, show_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
