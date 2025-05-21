
# 🎟️ Ticket Booking Service

## 📝 Tổng Quan

**Ticket Booking Service** điều phối toàn bộ quy trình đặt vé xem phim — từ việc chọn phim, chọn ghế, xác minh thông tin khách hàng, kiểm tra ghế trống, thanh toán, cho đến gửi thông báo xác nhận. 

## Kiến trúc

* Sử dụng Spring Boot, Spring cloud gateway + service discovery
* MySQL làm cơ sở dữ liệu chính
* RabbitMQ cho message queue
* Tích hợp với các dịch vụ khác thông qua HTTP
* RESTful API design

## ⚙️ Thiết Lập

* Được xây dựng bằng `Dockerfile` đã cung cấp.
* Mã nguồn nằm trong thư mục `src/`.

## 👨‍💻 Phát Triển

* Các API được định nghĩa trong: `docs/api-specs/ticket-booking-service.yaml`.
* Để chạy cục bộ, sử dụng lệnh:

  ```bash
  docker-compose up --build
  ```

## 🌐 API Endpoint

* Base URL: `http://localhost:8080/`

## 🌐 API chi tiết

* `POST /api/bookings` - Đặt vé
* `POST /api/bookings/customers/verify` - Xác nhận thông tin khách hàng
* `POST /api/bookings/payments` - Thanh toán
