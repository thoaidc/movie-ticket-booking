
# 🚪 API Gateway

## 📝 Tổng Quan

**API Gateway** đóng vai trò là cổng giao tiếp trung tâm giữa frontend và các microservice (movie, customer, booking,...). Nó định tuyến các yêu cầu HTTP đến đúng dịch vụ backend.

## ⚙️ Công Nghệ

* **Spring Cloud Gateway**
* **Java Spring Boot**

## 🚀 Khởi Chạy

Chạy bằng Docker:

```bash
docker build -t api-gateway .
docker run -p 8080:8080 api-gateway
```

## 🌐 Mặc định truy cập tại:

```
http://localhost:8080/
```
