
# 📄 Customer Service

## 📝 Tổng Quan

**Customer Service** chịu trách nhiệm quản lý thông tin khách hàng và xác minh đặt vé.

## Kiến trúc

* Sử dụng Spring Boot, Spring cloud gateway + service discovery
* MySQL làm cơ sở dữ liệu chính
* RabbitMQ cho message queue
* Tích hợp với các dịch vụ khác thông qua HTTP
* RESTful API design

## ⚙️ Thiết Lập

* Được xây dựng từ `Dockerfile` cung cấp sẵn.
* Mã nguồn nằm trong thư mục `src/`.

## 👨‍💻 Phát Triển

* Các API được định nghĩa trong file: `docs/api-specs/customer-service.yaml`.
* Để chạy cục bộ, sử dụng lệnh:

  ```bash
  docker-compose up --build
  ```

  từ thư mục gốc của dự án.

## 🌐 API Endpoint

* Base URL: `http://localhost:8080`

## 🌐 API chi tiết

* `GET /api/customers/{customerId}` - Lấy chi tiết thông tin khách hàng


