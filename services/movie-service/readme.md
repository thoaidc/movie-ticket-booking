
# 🎬 Movie Service

## 📝 Tổng Quan

**Movie Service** chịu trách nhiệm quản lý thông tin phim, suất chiếu và danh sách ghế của các phòng chiếu.
Cung cấp API để seat-availability-service khởi tạo danh sách ghế và trạng thái ghế cho từng suất chiếu.
Cung cấp API cho client lấy thông tin về phim, suất chiếu.

## Kiến trúc

* Sử dụng Spring Boot, Eureka client để đăng ký vào services discovery
* MySQL làm cơ sở dữ liệu chính
* RabbitMQ cho message queue
* Tích hợp với các dịch vụ khác thông qua HTTP
* RESTFul API design

## ⚙️ Thiết Lập

* Build với `Dockerfile` tại thư mục gốc dự án.
* Mã nguồn nằm trong thư mục `src/`.

## 👨‍💻 Phát Triển

* Các API được định nghĩa trong: `docs/api-specs/movie-service.yaml`.
* Khởi chạy đồng thời với các service khác theo cấu hình `docker-compose.yml` với một lệnh duy nhất:

  ```bash
  docker-compose up
  ```

## 🌐 API Endpoint

* Base URL: 
* Môi trường dev: `http://localhost:8101`
* Với docker: `http://localhost:8080`

## 🌐 API chi tiết

* `GET /api/cinemas` - Lấy thông tin tất cả các rạp phim
* `GET /api/movies` - Lấy thông tin tất cả các phim đang chiếu
* `GET /api/movies/{movieId}` - Lấy thông tin chi tiết của phim
* `GET /api/seats/by-all-shows` - Lấy tất cả các ghế của tất cả các suất chiếu để seat-availability-service khởi tạo danh sách ghế và trạng thái ghế
* `GET /api/seats/by-show/{showId}` - Lấy tất cả các ghế của 1 suất chiếu
* `GET /api/shows/{movieId}` - Lấy tất cả các suất chiếu của một phim
