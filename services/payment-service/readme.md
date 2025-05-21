

# 💳 Payment Service

## 📝 Tổng Quan

**Payment Service** xử lý các giao dịch thanh toán trực tuyến cho vé xem phim. 

## Kiến trúc

* Sử dụng Spring Boot, Spring cloud gateway + service discovery
* MySQL làm cơ sở dữ liệu chính
* RabbitMQ cho message queue
* Tích hợp với các dịch vụ khác thông qua HTTP
* RESTful API design

## ⚙️ Thiết Lập

* Được xây dựng thông qua `Dockerfile` có sẵn.
* Mã nguồn nằm trong thư mục `src/`.

## 👨‍💻 Phát Triển

* Các API được định nghĩa trong file: `docs/api-specs/payment-service.yaml`.
* Để chạy cục bộ, sử dụng lệnh sau tại thư mục gốc:

  ```bash
  docker-compose up --build
  ```

## 🌐 API Endpoint

* Base URL: `http://localhost:8080/`
