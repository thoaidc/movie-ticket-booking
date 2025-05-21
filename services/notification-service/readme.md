

# 📢 Notification Service

## 📝 Tổng Quan

**Notification Service** chịu trách nhiệm gửi thông báo xác nhận qua email cho khách hàng sau khi đặt vé và thanh toán thành công. 

## Kiến trúc

* Sử dụng Spring Boot, Spring cloud gateway + service discovery
* MySQL làm cơ sở dữ liệu chính
* RabbitMQ cho message queue
* Tích hợp với các dịch vụ khác thông qua HTTP
* RESTful API design

## ⚙️ Thiết Lập

* Được xây dựng bằng `Dockerfile` có sẵn.
* Mã nguồn được đặt trong thư mục `src/`.

## 👨‍💻 Phát Triển

* Các API được định nghĩa trong file: `docs/api-specs/notification-service.yaml`.
* Để chạy cục bộ, sử dụng lệnh sau từ thư mục gốc:

  ```bash
  docker-compose up --build
  ```

## 🌐 API Endpoint

* Base URL: `http://localhost:8080/`

