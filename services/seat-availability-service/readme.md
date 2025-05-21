
# 🪑 Seat Availability Service

## 📝 Tổng Quan

**Seat Availability Service** có nhiệm vụ kiểm tra tình trạng ghế trống cho các suất chiếu phim trước khi tiến hành đặt vé. 

## Kiến trúc

* Sử dụng Spring Boot, Spring cloud gateway + service discovery
* MySQL làm cơ sở dữ liệu chính
* RabbitMQ cho message queue
* Tích hợp với các dịch vụ khác thông qua HTTP
* RESTful API design

## ⚙️ Thiết Lập

* Được xây dựng dựa trên `Dockerfile` có sẵn.
* Mã nguồn được đặt trong thư mục `src/`.

## 👨‍💻 Phát Triển

* Các API được định nghĩa trong file: `docs/api-specs/seat-availability-service.yaml`.
* Để chạy cục bộ, thực hiện lệnh sau từ thư mục gốc:

  ```bash
  docker-compose up --build
  ```

## 🌐 API Endpoint

* Base URL: `http://localhost:8080/`

## 🌐 API chi tiết

* `POST /api/seats/init` - Khởi tạo danh sách ghế và trạng thái ghế của tất cả các suất chiếu
* `GET /api/seats/by-show/{showId}` - Lấy danh sách ghế kèm trạng thái ghế của 1 suất chiếu theo id