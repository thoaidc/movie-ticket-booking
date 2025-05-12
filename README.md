# 🎟️ Movie Ticket Booking System

This project implements a movie ticket booking system using a service-oriented architecture (SOA), designed to simulate the workflow of booking, payment, and confirmation of movie tickets.

---

## 🧩 System Architecture Overview

### 🧭 Task Service
- **Ticket Booking Service**  
  Handles the entire ticket booking process: from movie selection, seat selection, customer information verification, payment, to final confirmation notification.

### 📦 Entity Services
- **Movie Service**  
  Manages movie data, showtimes, and seat availability.

- **Customer Service**  
  Manages customer information and booking verification.

- **Payment Service**  
  Handles online payment processing for movie tickets.

### 🧠 Microservice
- **Seat Availability Service**  
  Verifies current seat availability before confirming the booking.

### 🛎️ Utility Service
- **Notification Service**  
  Sends email confirmation to customers after successful booking and payment.

---

## 👥 Team Members & Contributions

| Name                  | Role                | Contributions                                                                                                  |
|-----------------------|---------------------|----------------------------------------------------------------------------------------------------------------|
| **Nguyễn Thế Dũng**   | Backend Developer   | - Implemented **Movie Service** and **Payment Service**                                                        |
| **Đàm Công Thoại**    | Fullstack Developer | - Built **Ticket Booking Service** and integrated it with all other services<br>- Developed basic frontend UI  |
| **Nguyễn Đắc Phong**  | Backend Developer   | - Developed **Notification Service** and **Customer Service**                                                  |


---

## 🚀 How to Run

### Prerequisites
- Docker
- Docker Compose

### Run with one command:
```bash
docker-compose up
