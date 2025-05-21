

# 🧭 Discovery Service

## 📝 Tổng Quan

**Discovery Service** là một dịch vụ trung tâm sử dụng **Eureka Server** để quản lý việc đăng ký và phát hiện các microservice khác trong hệ thống. Nhờ đó, các service có thể giao tiếp với nhau một cách linh hoạt mà không cần cấu hình địa chỉ cố định.

## ⚙️ Thiết Lập

* Được xây dựng bằng **Spring Boot** và cấu hình với `application.yml`.
* Mã nguồn nằm trong thư mục `src/`.

## 👨‍💻 Phát Triển

* Điểm khởi đầu nằm tại file: `EurekaServerApplication.java`.
* Để chạy cục bộ, sử dụng một trong các cách sau từ thư mục gốc:


### Chạy bằng Docker:

```bash
docker build -t discovery-service .
docker run -p 8761:8761 discovery-service
```

## 🌐 Bảng Điều Khiển Eureka

* Truy cập địa chỉ:

  ```
  http://localhost:8761/
  ```
* Tại đây sẽ hiển thị danh sách các service đã đăng ký vào hệ thống.

