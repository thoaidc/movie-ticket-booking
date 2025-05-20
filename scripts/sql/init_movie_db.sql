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
    `name` varchar(45) NOT NULL UNIQUE,
    `address` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `cinema_room`;
CREATE TABLE `cinema_room` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `cinema_id` INT NOT NULL,
    `name` VARCHAR(100) NOT NULL,
    `capacity` INT NOT NULL,
    `screen_type` VARCHAR(10) NOT NULL DEFAULT '2D',
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

DROP TABLE IF EXISTS `show_time`;
CREATE TABLE `show_time` (
    `id` int NOT NULL AUTO_INCREMENT,
    `movie_id` int NOT NULL,
    `cinema_room_id` int NOT NULL,
    `ticket_price` DECIMAL(10,2) NOT NULL,
    `start_time` timestamp not null,
    `end_time` timestamp not null,
    PRIMARY KEY (`id`),
    CONSTRAINT fk_show_movie FOREIGN KEY (movie_id) REFERENCES movie(id),
    CONSTRAINT fk_show_cinema_room FOREIGN KEY (cinema_room_id) REFERENCES cinema_room(id),
    INDEX idx_show_movie (movie_id),
    INDEX idx_show_cinema_room (cinema_room_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `seat`;
CREATE TABLE `seat` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `cinema_room_id` INT NOT NULL,
    `seat_number` INT NOT NULL,
    `seat_row` INT NOT NULL,
    `code` VARCHAR(10) NOT NULL,
    CONSTRAINT fk_seat_room FOREIGN KEY (cinema_room_id) REFERENCES cinema_room(id) ON DELETE CASCADE,
    INDEX idx_seat_room (cinema_room_id),
    UNIQUE KEY uk_seat_screen_row_number (cinema_room_id, seat_row, seat_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

# Data
-- Insert data into cinema table
INSERT INTO `cinema` (`name`, `address`) VALUES
('CGV Vincom Center', '54A Nguyễn Chí Thanh, Đống Đa, Hà Nội'),
('Beta Cineplex Thái Nguyên', '189 Quang Trung, Thái Nguyên'),
('Lotte Cinema Landmark', '21 Ngô Quyền, Hoàn Kiếm, Hà Nội');

-- Insert data into cinema_room table
INSERT INTO `cinema_room` (`cinema_id`, `name`, `capacity`, `screen_type`) VALUES
-- CGV Vincom Center rooms
(1, 'Auditorium 1', 120, '2D'),
(1, 'Auditorium 2', 100, '3D'),
(1, 'IMAX Theater', 200, 'IMAX'),
(1, '4DX Experience', 80, '4DX'),
-- Beta Cineplex rooms
(2, 'Screen 1', 150, '2D'),
(2, 'Screen 2', 150, '2D'),
(2, 'Screen 3', 100, '3D'),
-- Lotte Cinema rooms
(3, 'Standard 1', 120, '2D'),
(3, 'Standard 2', 120, '2D'),
(3, 'Premium', 80, '3D');

-- Insert data into movie table
INSERT INTO `movie` (`name`, `description`, `duration`, `director`, `genre`, `release_date`) VALUES
                                                                                                 ('The Avengers: Endgame', 'After the devastating events of Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos\' actions and restore balance to the universe.', 181, 'Anthony Russo, Joe Russo', 'Action, Adventure, Fantasy', '2024-04-26 00:00:00'),
('Parasite', 'Greed and class discrimination threaten the newly formed symbiotic relationship between the wealthy Park family and the destitute Kim clan.', 132, 'Bong Joon Ho', 'Drama, Thriller', '2024-05-02 00:00:00'),
('Inception', 'A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.', 148, 'Christopher Nolan', 'Action, Adventure, Sci-Fi', '2025-04-16 00:00:00'),
('The Shaw shank Redemption', 'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.', 142, 'Frank Darabont', 'Drama', '2023-04-22 00:00:00'),
('Pulp Fiction', 'The lives of two mob hit men, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.', 154, 'Quentin Tarantino', 'Crime, Drama', '2025-05-06 00:00:00'),
('The Dark Knight', 'When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.', 152, 'Christopher Nolan', 'Action, Crime, Drama', '2025-05-10 00:00:00'),
('The Godfather', 'The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.', 175, 'Francis Ford Coppola', 'Crime, Drama', '2025-04-30 00:00:00'),
('Interstellar', 'A team of explorers travel through a wormhole in space in an attempt to ensure humanity\'s survival.', 169, 'Christopher Nolan', 'Adventure, Drama, Sci-Fi', '2020-05-07 00:00:00'),
('The Matrix', 'A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.', 136, 'Lana Wachowski, Lilly Wachowski', 'Action, Sci-Fi', '2021-05-12 00:00:00'),
('Spirited Away', 'During her family\'s move to the suburbs, a sullen 10-year-old girl wanders into a world ruled by gods, witches, and spirits, and where humans are changed into beasts.', 125, 'Hayao Miyazaki', 'Animation, Adventure, Family', '2025-05-14 00:00:00');

-- Create show times from May 18, 2025 to May 30, 2025
INSERT INTO show_time (`movie_id`, `cinema_room_id`, `ticket_price`, `start_time`, `end_time`) VALUES
-- The Avengers: Endgame shows
(1, 1, 120000.00, '2025-05-18 09:00:00', '2025-05-18 12:01:00'),
(1, 3, 180000.00, '2025-05-19 14:30:00', '2025-05-19 17:31:00'),
(1, 6, 100000.00, '2025-05-20 19:00:00', '2025-05-20 22:01:00'),
(1, 9, 110000.00, '2025-05-21 10:00:00', '2025-05-21 13:01:00'),

-- Parasite shows
(2, 2, 110000.00, '2025-05-18 11:00:00', '2025-05-18 13:12:00'),
(2, 7, 120000.00, '2025-05-19 15:30:00', '2025-05-19 17:42:00'),
(2, 8, 100000.00, '2025-05-22 18:00:00', '2025-05-22 20:12:00'),

-- Inception shows
(3, 3, 170000.00, '2025-05-22 09:30:00', '2025-05-22 11:58:00'),
(3, 4, 200000.00, '2025-05-23 14:00:00', '2025-05-23 16:28:00'),
(3, 10, 130000.00, '2025-05-24 19:30:00', '2025-05-24 21:58:00'),

-- The Shawshank Redemption shows
(4, 5, 90000.00, '2025-05-20 10:00:00', '2025-05-20 12:22:00'),
(4, 1, 120000.00, '2025-05-21 15:30:00', '2025-05-21 17:52:00'),
(4, 8, 100000.00, '2025-05-22 20:00:00', '2025-05-22 22:22:00'),

-- Pulp Fiction shows
(5, 2, 110000.00, '2025-05-22 11:30:00', '2025-05-22 14:04:00'),
(5, 6, 100000.00, '2025-05-23 16:00:00', '2025-05-23 18:34:00'),
(5, 9, 110000.00, '2025-05-24 20:30:00', '2025-05-24 23:04:00'),

-- The Dark Knight shows
(6, 3, 170000.00, '2025-05-24 10:00:00', '2025-05-24 12:32:00'),
(6, 7, 120000.00, '2025-05-25 14:30:00', '2025-05-25 17:02:00'),
(6, 10, 130000.00, '2025-05-26 19:00:00', '2025-05-26 21:32:00'),

-- The Godfather shows
(7, 1, 120000.00, '2025-05-26 09:00:00', '2025-05-26 11:55:00'),
(7, 5, 90000.00, '2025-05-27 13:30:00', '2025-05-27 16:25:00'),
(7, 8, 100000.00, '2025-05-28 18:00:00', '2025-05-28 20:55:00'),

-- Interstellar shows
(8, 3, 170000.00, '2025-05-26 10:30:00', '2025-05-26 13:19:00'),
(8, 4, 200000.00, '2025-05-27 15:00:00', '2025-05-27 17:49:00'),
(8, 9, 110000.00, '2025-05-28 19:30:00', '2025-05-28 22:19:00'),

-- The Matrix shows
(9, 2, 110000.00, '2025-05-28 11:00:00', '2025-05-28 13:16:00'),
(9, 6, 100000.00, '2025-05-29 15:30:00', '2025-05-29 17:46:00'),
(9, 10, 130000.00, '2025-05-30 20:00:00', '2025-05-30 22:16:00'),

-- Spirited Away shows
(10, 1, 120000.00, '2025-05-28 09:30:00', '2025-05-28 11:35:00'),
(10, 7, 120000.00, '2025-05-29 14:00:00', '2025-05-29 16:05:00'),
(10, 8, 100000.00, '2025-05-30 18:30:00', '2025-05-30 20:35:00');

-- Insert seats for each cinema room
-- Create seats for all cinema rooms (10 seats minimum per room as requested)
-- For simplicity, we'll create a 4x4 grid (16 seats) for each room

-- Function to generate seat data for a given room
-- We'll create seats manually for all 10 rooms

-- Room 1 seats (CGV Auditorium 1)
INSERT INTO `seat` (`cinema_room_id`, `seat_row`, `seat_number`, `code`) VALUES
(1, 1, 1, 'A1'),
(1, 1, 2, 'A2'),
(1, 1, 3, 'A3'),
(1, 1, 4, 'A4'),
(1, 2, 1, 'B1'),
(1, 2, 2, 'B2'),
(1, 2, 3, 'B3'),
(1, 2, 4, 'B4'),
(1, 3, 1, 'C1'),
(1, 3, 2, 'C2'),
(1, 3, 3, 'C3'),
(1, 3, 4, 'C4'),
(1, 4, 1, 'D1'),
(1, 4, 2, 'D2'),
(1, 4, 3, 'D3'),
(1, 4, 4, 'D4');

-- Room 2 seats (CGV Auditorium 2)
INSERT INTO `seat` (`cinema_room_id`, `seat_row`, `seat_number`, `code`) VALUES
(2, 1, 1, 'A1'),
(2, 1, 2, 'A2'),
(2, 1, 3, 'A3'),
(2, 1, 4, 'A4'),
(2, 2, 1, 'B1'),
(2, 2, 2, 'B2'),
(2, 2, 3, 'B3'),
(2, 2, 4, 'B4'),
(2, 3, 1, 'C1'),
(2, 3, 2, 'C2'),
(2, 3, 3, 'C3'),
(2, 3, 4, 'C4'),
(2, 4, 1, 'D1'),
(2, 4, 2, 'D2'),
(2, 4, 3, 'D3'),
(2, 4, 4, 'D4');

-- Room 3 seats (CGV IMAX Theater)
INSERT INTO `seat` (`cinema_room_id`, `seat_row`, `seat_number`, `code`) VALUES
(3, 1, 1, 'A1'),
(3, 1, 2, 'A2'),
(3, 1, 3, 'A3'),
(3, 1, 4, 'A4'),
(3, 2, 1, 'B1'),
(3, 2, 2, 'B2'),
(3, 2, 3, 'B3'),
(3, 2, 4, 'B4'),
(3, 3, 1, 'C1'),
(3, 3, 2, 'C2'),
(3, 3, 3, 'C3'),
(3, 3, 4, 'C4'),
(3, 4, 1, 'D1'),
(3, 4, 2, 'D2'),
(3, 4, 3, 'D3'),
(3, 4, 4, 'D4');

-- Room 4 seats (CGV 4DX Experience)
INSERT INTO `seat` (`cinema_room_id`, `seat_row`, `seat_number`, `code`) VALUES
(4, 1, 1, 'A1'),
(4, 1, 2, 'A2'),
(4, 1, 3, 'A3'),
(4, 1, 4, 'A4'),
(4, 2, 1, 'B1'),
(4, 2, 2, 'B2'),
(4, 2, 3, 'B3'),
(4, 2, 4, 'B4'),
(4, 3, 1, 'C1'),
(4, 3, 2, 'C2'),
(4, 3, 3, 'C3'),
(4, 3, 4, 'C4'),
(4, 4, 1, 'D1'),
(4, 4, 2, 'D2'),
(4, 4, 3, 'D3'),
(4, 4, 4, 'D4');

-- Room 5 seats (Beta Screen 1)
INSERT INTO `seat` (`cinema_room_id`, `seat_row`, `seat_number`, `code`) VALUES
(5, 1, 1, 'A1'),
(5, 1, 2, 'A2'),
(5, 1, 3, 'A3'),
(5, 1, 4, 'A4'),
(5, 2, 1, 'B1'),
(5, 2, 2, 'B2'),
(5, 2, 3, 'B3'),
(5, 2, 4, 'B4'),
(5, 3, 1, 'C1'),
(5, 3, 2, 'C2'),
(5, 3, 3, 'C3'),
(5, 3, 4, 'C4'),
(5, 4, 1, 'D1'),
(5, 4, 2, 'D2'),
(5, 4, 3, 'D3'),
(5, 4, 4, 'D4');

-- Room 6 seats (Beta Screen 2)
INSERT INTO `seat` (`cinema_room_id`, `seat_row`, `seat_number`, `code`) VALUES
(6, 1, 1, 'A1'),
(6, 1, 2, 'A2'),
(6, 1, 3, 'A3'),
(6, 1, 4, 'A4'),
(6, 2, 1, 'B1'),
(6, 2, 2, 'B2'),
(6, 2, 3, 'B3'),
(6, 2, 4, 'B4'),
(6, 3, 1, 'C1'),
(6, 3, 2, 'C2'),
(6, 3, 3, 'C3'),
(6, 3, 4, 'C4'),
(6, 4, 1, 'D1'),
(6, 4, 2, 'D2'),
(6, 4, 3, 'D3'),
(6, 4, 4, 'D4');

-- Room 7 seats (Beta Screen 3)
INSERT INTO `seat` (`cinema_room_id`, `seat_row`, `seat_number`, `code`) VALUES
(7, 1, 1, 'A1'),
(7, 1, 2, 'A2'),
(7, 1, 3, 'A3'),
(7, 1, 4, 'A4'),
(7, 2, 1, 'B1'),
(7, 2, 2, 'B2'),
(7, 2, 3, 'B3'),
(7, 2, 4, 'B4'),
(7, 3, 1, 'C1'),
(7, 3, 2, 'C2'),
(7, 3, 3, 'C3'),
(7, 3, 4, 'C4'),
(7, 4, 1, 'D1'),
(7, 4, 2, 'D2'),
(7, 4, 3, 'D3'),
(7, 4, 4, 'D4');

-- Room 8 seats (Lotte Standard 1)
INSERT INTO `seat` (`cinema_room_id`, `seat_row`, `seat_number`, `code`) VALUES
(8, 1, 1, 'A1'),
(8, 1, 2, 'A2'),
(8, 1, 3, 'A3'),
(8, 1, 4, 'A4'),
(8, 2, 1, 'B1'),
(8, 2, 2, 'B2'),
(8, 2, 3, 'B3'),
(8, 2, 4, 'B4'),
(8, 3, 1, 'C1'),
(8, 3, 2, 'C2'),
(8, 3, 3, 'C3'),
(8, 3, 4, 'C4'),
(8, 4, 1, 'D1'),
(8, 4, 2, 'D2'),
(8, 4, 3, 'D3'),
(8, 4, 4, 'D4');

-- Room 9 seats (Lotte Standard 2)
INSERT INTO `seat` (`cinema_room_id`, `seat_row`, `seat_number`, `code`) VALUES
(9, 1, 1, 'A1'),
(9, 1, 2, 'A2'),
(9, 1, 3, 'A3'),
(9, 1, 4, 'A4'),
(9, 2, 1, 'B1'),
(9, 2, 2, 'B2'),
(9, 2, 3, 'B3'),
(9, 2, 4, 'B4'),
(9, 3, 1, 'C1'),
(9, 3, 2, 'C2'),
(9, 3, 3, 'C3'),
(9, 3, 4, 'C4'),
(9, 4, 1, 'D1'),
(9, 4, 2, 'D2'),
(9, 4, 3, 'D3'),
(9, 4, 4, 'D4');

-- Room 10 seats (Lotte Premium)
INSERT INTO `seat` (`cinema_room_id`, `seat_row`, `seat_number`, `code`) VALUES
(10, 1, 1, 'A1'),
(10, 1, 2, 'A2'),
(10, 1, 3, 'A3'),
(10, 1, 4, 'A4'),
(10, 2, 1, 'B1'),
(10, 2, 2, 'B2'),
(10, 2, 3, 'B3'),
(10, 2, 4, 'B4'),
(10, 3, 1, 'C1'),
(10, 3, 2, 'C2'),
(10, 3, 3, 'C3'),
(10, 3, 4, 'C4'),
(10, 4, 1, 'D1'),
(10, 4, 2, 'D2'),
(10, 4, 3, 'D3'),
(10, 4, 4, 'D4');
