
# 🎟️ Ticket Booking Service

## 📝 Tổng Quan

**Ticket Booking Service** điều phối toàn bộ quy trình đặt vé xem phim 
— từ việc nhận yêu cầu đặt vé (xác minh thông tin phim, tình trạng ghế), 
xác minh thông tin khách hàng, thanh toán, cho đến gửi thông báo xác nhận.
Xử lý rollback cho ghế đã giữ và hoàn tiền nếu đặt vé thất bại.

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

* Các API được định nghĩa trong: `docs/api-specs/ticket-booking-service.yaml`.
* Khởi chạy đồng thời với các service khác theo cấu hình `docker-compose.yml` với một lệnh duy nhất:

  ```bash
  docker-compose up
  ```

## 🌐 API Endpoint

* Base URL: 
* Môi trường dev: `http://localhost:8105/`
* Với docker: `http://localhost:8080/`

## 🌐 API chi tiết

* `POST /api/bookings` - Tạo yêu cầu đặt vé xem phim
* `POST /api/bookings/customers/verify` - Gửi thông tin xác nhận thông tin khách hàng
* `POST /api/bookings/payments` - Gửi thông tin xử lý thanh toán đơn hàng
