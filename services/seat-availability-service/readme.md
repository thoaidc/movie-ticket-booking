
# 🪑 Seat Availability Service

## 📝 Tổng Quan

**Seat Availability Service** 
Thực hiện lấy thông tin suất chiếu và ghế ngồi tại phòng chiếu và khởi tạo danh sách ghế cho từng suất chiếu.
Thực hiện kiểm tra tình trạng ghế trống cho các suất chiếu phim trước khi tiến hành đặt vé. Nếu ghế trống thì tiến hành giữ ghế để đợi
xác minh thông tin và thanh toán đơn hàng. Xử lý mở lại ghế nếu đặt hàng thất bại hoặc xác nhận ghế đã đặt nếu đặt hàng thành công.

## Kiến trúc

* Sử dụng Spring Boot, Eureka client để đăng ký vào service discovery
* MySQL làm cơ sở dữ liệu chính
* RabbitMQ cho message queue
* Tích hợp với các dịch vụ khác thông qua HTTP
* RESTFul API design

## ⚙️ Thiết Lập

* Build dự án với `Dockerfile` tại thư mục gốc dự án.
* Mã nguồn được đặt trong thư mục `src/`.

## 👨‍💻 Phát Triển

* Các API được định nghĩa trong file: `docs/api-specs/seat-availability-service.yaml`.
* Khởi chạy đồng thời với các service khác theo cấu hình `docker-compose.yml` với một lệnh duy nhất:

  ```bash
  docker-compose up
  ```

## 🌐 API Endpoint

* Base URL: 
* Môi trường dev: `http://localhost:8104/`
* Với docker: `http://localhost:8080/`

## 🌐 API chi tiết

* `POST /api/seats/init` - Khởi tạo danh sách ghế và trạng thái ghế của tất cả các suất chiếu
* `GET /api/seats/by-show/{showId}` - Lấy danh sách ghế kèm trạng thái ghế của 1 suất chiếu theo id
