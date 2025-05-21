
# 🎟️ Hệ Thống Đặt Vé Xem Phim

Dự án này triển khai một hệ thống đặt vé xem phim theo kiến trúc hướng dịch vụ (SOA), mô phỏng quy trình từ đặt vé, thanh toán cho đến xác nhận thành công.

---

## 🧩 Tổng Quan Kiến Trúc Hệ Thống

### 🧭 API gateway

* Xử lý định tuyến các request từ client đến các service cụ thể theo tên service trong api.

### 🧭 Task Service

* **Ticket Booking Service**
  Xử lý toàn bộ quy trình đặt vé: chọn phim, chọn ghế, xác minh thông tin khách hàng, thanh toán và gửi thông báo xác nhận.

### 📦 Entity Services

* **Movie Service**
  Quản lý dữ liệu phim, suất chiếu và danh sách ghế của các phòng chiếu.

* **Customer Service**
  Quản lý thông tin khách hàng đặt vé.

* **Payment Service**
  Xử lý thanh toán trực tuyến cho vé xem phim.

### 🧠 Microservice

* **Seat Availability Service**
  Kiểm tra và xác minh tình trạng ghế trống trước khi xác nhận đặt vé.

### 🛎️ Utility Service

* **Discovery Service**
  Quản lý việc đăng ký và phát hiện các service trong hệ thống, hỗ trợ api gateway định tuyến và các service giao tiếp với nhau.

* **Notification Service**
  Gửi email thông báo cho khách hàng sau khi đặt vé thành công.

---

## 👥 Thành Viên & Đóng Góp

| Họ Tên               | Vai Trò             | Đóng Góp                                                                                                            |
| -------------------- | ------------------- |---------------------------------------------------------------------------------------------------------------------|
| **Nguyễn Thế Dũng**  | Lập trình Backend   | - Triển khai **Movie Service** và **Payment Service**                                                               |
| **Đàm Công Thoại**   | Lập trình Fullstack | - Xây dựng **Ticket Booking Service** và tích hợp với các services khác<br>- Phát triển giao diện người dùng cơ bản |
| **Nguyễn Đắc Phong** | Lập trình Backend   | - Phát triển **Notification Service** và **Customer Service**                                                        |

---

## 🚀 Hướng Dẫn Chạy Dự Án

### Yêu Cầu

* Docker
* Docker Compose

### Chạy bằng một lệnh duy nhất:

```bash
docker-compose up
```
