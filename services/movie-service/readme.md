
# 🎬 Movie Service

## 📝 Tổng Quan

**Movie Service** chịu trách nhiệm quản lý thông tin phim, suất chiếu và tình trạng ghế trống.

## Kiến trúc

* Sử dụng Spring Boot, Spring cloud gateway + service discovery
* MySQL làm cơ sở dữ liệu chính
* RabbitMQ cho message queue
* Tích hợp với các dịch vụ khác thông qua HTTP
* RESTful API design

## ⚙️ Thiết Lập

* Được xây dựng bằng `Dockerfile` cung cấp sẵn.
* Mã nguồn nằm trong thư mục `src/`.

## 👨‍💻 Phát Triển

* Các API được định nghĩa trong: `docs/api-specs/movie-service.yaml`.
* Để chạy cục bộ, sử dụng lệnh sau từ thư mục gốc:

  ```bash
  docker-compose up --build
  ```

## 🌐 API Endpoint

* Base URL: `http://localhost:8080`

## 🌐 API chi tiết

* `GET /api/cinemas` - Lấy thông tin tất cả các rạp
* `GET /api/movies` - Lấy thông tin tất cả các phim
* `GET /api/movies/{movieId}` - Lấy thông tin chi tiết của phim
* `GET /api/seats/by-all-shows` - Lấy tất cả các ghế cua tất cả các suất chiếu
* `GET /api/seats/by-show/{showId}` - Lấy tất cả các ghế của 1 suất chiếu
* `GET /api/shows/{movieId}` - Lấy tất cả các suất chiếu theo phim



