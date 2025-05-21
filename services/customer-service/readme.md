
# 📄 Customer Service

## 📝 Tổng Quan

**Customer Service** chịu trách nhiệm quản lý thông tin khách hàng và xác minh thông tin khách hàng. 
Nhận thông tin khách hàng từ ticket-booking-service để xác minh và lưu trữ, cung cấp thông tin khách
hàng để notification-service gửi email thông báo.

## Kiến trúc

* Sử dụng Spring Boot, Eureka client để đăng ký vào Services discovery
* MySQL làm cơ sở dữ liệu chính
* RabbitMQ cho message queue
* Tích hợp với các dịch vụ khác thông qua HTTP
* RESTFul API design

## ⚙️ Thiết Lập

* Build bằng Docker với `Dockerfile` tại thư mục gốc dự án.
* Mã nguồn nằm trong thư mục `src/`.

## 👨‍💻 Phát Triển

* Các API được định nghĩa trong file: `docs/api-specs/customer-service.yaml`.
* Khởi chạy đồng thời với các service khác theo cấu hình `docker-compose.yml` với một lệnh duy nhất:

  ```bash
  docker-compose up
  ```

## 🌐 API Endpoint

* Base URL:
* Môi trường dev: `http://localhost:8100`
* Với docker: `http://localhost:8080`

## 🌐 API chi tiết

* `GET /api/customers/{customerId}` - Lấy chi tiết thông tin khách hàng
