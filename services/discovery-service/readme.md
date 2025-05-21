

# 🧭 Discovery Service

## 📝 Tổng Quan

**Discovery Service** là một dịch vụ trung tâm sử dụng **Eureka Server** để quản lý việc đăng ký và phát hiện các microservice khác trong hệ thống.
Nhờ đó, API gateway có thể định tuyến request tới các service và các service có thể giao tiếp với nhau một cách linh hoạt mà không cần cấu hình địa chỉ cố định.

## ⚙️ Thiết Lập

* Build với `DockerFile` tại thư mục gốc dự án
* Mã nguồn nằm trong thư mục `src/`.

## 👨‍💻 Phát Triển

* Khởi chạy đồng thời với các service khác theo cấu hình `docker-compose.yml` với một lệnh duy nhất:

  ```bash
  docker-compose up
  ```

## 🌐 Bảng điều khiển Eureka

* Truy cập địa chỉ:

  ```
  http://localhost:8761/
  ```
* Tại đây sẽ hiển thị danh sách các service và trạng thái health check của các service đã đăng ký.

