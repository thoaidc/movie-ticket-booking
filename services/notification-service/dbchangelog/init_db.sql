-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: hdv_notification
-- ------------------------------------------------------
-- Server version	8.0.37

CREATE DATABASE IF NOT EXISTS `hdv_notification` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT ENCRYPTION='N';
USE `hdv_notification`;

DROP TABLE IF EXISTS `email`;
CREATE TABLE `email` (
  `id` int NOT NULL AUTO_INCREMENT,
  `recipient` varchar(100) NOT NULL,
  `subject` varchar(45) DEFAULT NULL,
  `content` text NOT NULL,
  `status` varchar(45) NOT NULL,
  `sent_time` timestamp NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
