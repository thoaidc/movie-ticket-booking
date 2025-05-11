-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: hdv_movie
-- ------------------------------------------------------
-- Server version	8.0.37

CREATE DATABASE IF NOT EXISTS `hdv_movie` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT ENCRYPTION='N';
USE `hdv_movie`;

DROP TABLE IF EXISTS `cinema`;
CREATE TABLE `cinema` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(45) NOT NULL,
    `address` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `cinema_room`;
CREATE TABLE `cinema_room` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `cinema_id` INT NOT NULL,
    `name` VARCHAR(100) NOT NULL,
    `capacity` INT NOT NULL,
    `screen_type` ENUM('2D', '3D', 'IMAX', '4DX') NOT NULL DEFAULT '2D',
    CONSTRAINT fk_room_cinema FOREIGN KEY (cinema_id) REFERENCES cinema(id) ON DELETE CASCADE,
    INDEX idx_room_cinema (cinema_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `movie`;
CREATE TABLE `movie` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(45) NOT NULL,
    `description` varchar(1000) DEFAULT NULL,
    `duration` int NOT NULL,
    `director` varchar(100) DEFAULT NULL,
    `genre` varchar(45) DEFAULT NULL,
    `release_date` timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX idx_movie_name (name),
    INDEX idx_movie_genre (genre),
    INDEX idx_movie_release (release_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `show`;
CREATE TABLE `show` (
  `id` int NOT NULL AUTO_INCREMENT,
  `movie_id` int NOT NULL,
  `cinema_room_id` int NOT NULL,
  `ticket_price` DECIMAL(10,2) NOT NULL,
  `status` ENUM('ACTIVE', 'CANCELLED', 'COMPLETED') NOT NULL DEFAULT 'ACTIVE',
  `start_time` timestamp not null,
  `end_time` timestamp not null,
  PRIMARY KEY (`id`),
  CONSTRAINT fk_show_movie FOREIGN KEY (movie_id) REFERENCES movie(id),
  CONSTRAINT fk_show_cinema_room FOREIGN KEY (cinema_room_id) REFERENCES cinema_room(id),
  INDEX idx_show_movie (movie_id),
  INDEX idx_show_cinema_room (cinema_room_id),
  INDEX idx_show_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `seat` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `cinema_room_id` INT NOT NULL,
    `seat_number` VARCHAR(10) NOT NULL,
    `seat_row` VARCHAR(10) NOT NULL,
    `status` ENUM('AVAILABLE', 'BOOKED') NOT NULL DEFAULT 'AVAILABLE',
    CONSTRAINT fk_seat_room FOREIGN KEY (cinema_room_id) REFERENCES cinema_room(id) ON DELETE CASCADE,
    INDEX idx_seat_room (cinema_room_id),
    UNIQUE KEY uk_seat_screen_row_number (cinema_room_id, seat_row, seat_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
