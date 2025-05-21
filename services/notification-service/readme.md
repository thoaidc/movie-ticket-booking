

# 📢 Notification Service

## 📝 Tổng Quan

**Notification Service** 
Gọi API tới customer service để lấy thông tin khách hàng đặt vé và gửi thông báo qua email cho khách hàng sau khi đặt vé thành công.

## Kiến trúc

* Sử dụng Spring Boot, Eureka client để đăng ký vào service discovery
* MySQL làm cơ sở dữ liệu chính
* RabbitMQ cho message queue
* Tích hợp với các dịch vụ khác thông qua HTTP
* RESTFul API design

## ⚙️ Thiết Lập

* Build bằng `Dockerfile` tại thư mục gốc dự án.
* Mã nguồn được đặt trong thư mục `src/`.

## 👨‍💻 Phát Triển

* Các API được định nghĩa trong file: `docs/api-specs/notification-service.yaml`.
* Khởi chạy đồng thời với các service khác theo cấu hình `docker-compose.yml` với một lệnh duy nhất:

  ```bash
  docker-compose up
  ```

## 🌐 API Endpoint

* Base URL: 
* Môi trường dev: `http://localhost:8102/`
* Với docker: `http://localhost:8080/`

