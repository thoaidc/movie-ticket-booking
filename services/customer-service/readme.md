
# 📄 Customer Service

## 📝 Tổng Quan

**Customer Service** chịu trách nhiệm quản lý thông tin khách hàng và xác minh đặt vé. Đây là một microservice được xây dựng bằng **...**.

## ⚙️ Thiết Lập

* Được xây dựng từ `Dockerfile` cung cấp sẵn.
* Mã nguồn nằm trong thư mục `src/`.

## 👨‍💻 Phát Triển

* Các API được định nghĩa trong file: `docs/api-specs/customer-service.yaml`.
* Để chạy cục bộ, sử dụng lệnh:

  ```bash
  docker-compose up --build
  ```

  từ thư mục gốc của dự án.

## 🌐 API Endpoint

* Base URL: `http://localhost:5002/`
* Tham khảo chi tiết API tại: `docs/api-specs/customer-service.yaml`
