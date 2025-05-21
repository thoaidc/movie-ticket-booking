

# 💳 Payment Service

## 📝 Tổng Quan

**Payment Service** xử lý các giao dịch thanh toán trực tuyến cho vé xem phim. 
Xử lý hoàn tiền theo yêu cầu từ ticket-booking-service nếu có lỗi xảy ra khi đặt vé.

## Kiến trúc

* Sử dụng Spring Boot, Eureka client để đăng ký vào service discovery
* MySQL làm cơ sở dữ liệu chính
* RabbitMQ cho message queue
* Tích hợp với các dịch vụ khác thông qua HTTP
* RESTFul API design

## ⚙️ Thiết Lập

* Build dự án với `Dockerfile` tại thư mục gốc dự án.
* Mã nguồn nằm trong thư mục `src/`.

## 👨‍💻 Phát Triển

* Các API được định nghĩa trong file: `docs/api-specs/payment-service.yaml`.
* Khởi chạy đồng thời với các service khác theo cấu hình `docker-compose.yml` với một lệnh duy nhất:

  ```bash
  docker-compose up
  ```

## 🌐 API Endpoint

* Base URL: 
* Môi trường dev: `http://localhost:8103/`
* Với docker: `http://localhost:8080/`
