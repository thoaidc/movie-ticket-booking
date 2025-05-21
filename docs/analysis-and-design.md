# 📊 Hệ thống đặt vé xem phim - Phân Tích và Thiết Kế

---

## Thành viên

Nhóm 2:

| Họ và tên        | Mã sinh viên |
|------------------|--------------|
| Nguyễn Thế Dũng  | B21DCCN275   |
| Nguyễn Đắc Phong | B21DCCN587   |
| Đàm Công Thoại   | B21DCCN685   |


---


## 1. 🎯  Mô Tả Bài Toán và Mô hình nghiệp vụ

> **Bài toán:** Hệ thống đặt phòng khách sạn trực tuyến

_Mô tả nghiệp vụ_

Hệ thống đặt vé xem phim cho phép người dùng tìm kếm và đặt vé trực tuyến.
Người dùng sẽ cũng cấp thông tin cá nhân và lựa chọn suất chiếu, rạp chiếu
cùng vị trí ghế ngồi mong muốn.Hệ thông kiếm tra xem ghế đã được đặt trước
hay chưa; nếu ghế không còn trống quy trình sẽ dừng lại. Nếu đặt vé thành
công, hệ thống sẽ gửi thông báo xác nhận đến người dùng và cập nhật lịch sử
đặt vé.

---

## 2. 🧩 Xác định các Microservice

_Xác định các thực thể_
**Đối tượng sử dụng**
- Khách hàng cá nhân
- Quản trị viên

**Mục tiêu chính**
- Cho phép khách hàng tìm kiếm và vé trực tuyến
- Quản lý tình trạng vé theo thời gian thực
- Tự động hóa quy trình đặt vé và thông báo

**Dữ liệu xử lý**
- Thông tin người dùng
- Thông tin khách sạn/phòng
- Lịch sử đặt phòng
- Trạng thái phòng (trống/đã đặt)


_Thiết kế thực thể_

![Entity Relationship Diagram](../docs/assets/entities.png)


_Thiết kế những dịch vụ_
Ta sẽ đi qua phân tích quy trình nghiệp vụ của dự án này và lọc những logic nghiệp vụ mà sẽ có thể cho vào những thực thể khác nhau để ta có thể đưa ra những đối tượng dịch vụ khả quan của hệ thống.

**Đặt Phòng Khách Sạn**
- Người dùng tìm kiếm phòng (theo ngày, địa điểm, giá).
- Chọn phòng → Kiểm tra tính khả dụng (HotelRoomAvailability).
- Tạo booking → Ghi vào Booking và BookedRoom.
- Thanh toán → Tạo Bill và cập nhật trạng thái.
- Xác nhận → Gửi thông báo (Notification), ghi lịch sử (BookingHistory).


| Tên dịch vụ               | Trách nhiệm                                   | Công nghệ       | CSDL          | Giao thức              |
|---------------------------|-----------------------------------------------|-----------------|---------------|------------------------|
| **user-service**          | Quản lý người dùng, xác thực                 | FastAPI         | PostgreSQL    | REST                   |
| **hotel-service**         | Quản lý khách sạn, phòng, giá                | FastAPI         | PostgreSQL    | REST                   |
| **booking-service**       | Xử lý đặt phòng, thanh toán                  | FastAPI         | PostgreSQL    | REST                   |
| **room-availability**     | Kiểm tra tình trạng phòng                    | FastAPI         | Redis         | gRPC                   |
| **notification-service**  | Gửi email thông báo                          | FastAPI         | (Không dùng DB) | RabbitMQ               |
| **history-service**       | Lưu lịch sử đặt phòng                        | FastAPI         | MongoDB       | REST                   |


## 3. 🔄 Giao tiếp giữa các dịch vụ

### **Cơ chế chính**
![flow](../docs/assets/flow.png)


### **Chi tiết các kênh kết nối**
| **Kết nối**                  | **Protocol** | **Mục đích**                           |  
|-------------------------------|--------------|---------------------------------------|  
| Client ↔ API Gateway          | HTTP/REST    | Tất cả request từ client              |  
| API Gateway ↔ Core Services   | HTTP/REST    | Định tuyến request                    |  
| Booking ↔ Room Availability   | HTTP/REST    | Kiểm tra phòng trống (low latency)    |  
| Booking ↔ Notification        | RabbitMQ     | Gửi thông báo không đồng bộ           |  
| Booking ↔ Booking History     | HTTP/REST    | Ghi log đồng bộ                       |  


---

## 4. 🗂️ Thiết kế dữ liệu chính



---

## 5. 🛠️ Triển khai service

### **Cấu trúc thư mục**
```
services/
├── user-service/
│ ├── app/
│ │ ├── core/ # Chứa các file cài đặt
│ │ ├── api/ # Xử lý route
│ │ ├── models.py # Mô hình SQLAlchemy
│ │ ├── schemas.py # Mô hình Pydantic
│ │ └── main.py # Ứng dụng FastAPI 
│ ├── migrations/ # Alembic migrations
│ └── Dockerfile
├── booking-history-service/
├── hotel-service/
├── notification-service/
├── room-availability-service/
├── booking-service/
gateway/
├── nginx.conf # Reverse proxy rules
└── Dockerfile
deploy/
├── docker-compose.yml # Multi-container setup
└── .env # Environment variables
```

### **Cơ sở dữ liệu của từng dịch vụ**
| Service               | Database       | Tables/Collections                     |
|-----------------------|----------------|----------------------------------------|
| `user-service`        | PostgreSQL     | `users`, `user_profiles`               |
| `hotel-service`       | PostgreSQL     | `hotels`, `rooms`, `hotel_images`      |
| `booking-service`     | PostgreSQL     | `bookings`, `booked_rooms`             |
| `room-availability`   | Redis          | Key: `hotel:{hotel_id}:{date}`         |
| `booking-history`     | MongoDB        | `booking_history` (schemaless)         |
| `notification`        | (NoDB)         | Queues in RabbitMQ                     |


---

## 6. 📦 Deployment với Docker

### **Quy trình triển khai**
1. **Build images**:
   ```bash
   docker-compose build
   ```
2. **Khởi động hệ thống**:
   ```bash
   docker-compose up -d
   ```
3. **Kiểm tra trạng thái**:
   ```bash
   docker-compose ps
   ```
4. **(Optional) Scale service**:
   ```bash
   docker-compose up -d --scale user-service=3
   ```

---



## 7. Ưu điểm kiến trúc

**Ưu điểm:**
- **Dễ mở rộng**: Scale riêng từng service (vd: Notification Service khi có khuyến mãi)
- **Độ tin cậy**: Lỗi 1 service không ảnh hưởng toàn hệ thống
- **Bảo trì dễ**: Chuẩn hóa trên FastAPI, database tách biệt

**Nhược điểm:**
- **Phức tạp vận hành**: Quản lý nhiều DB (PostgreSQL, Redis, MongoDB, RabbitMQ)
- **Khó đồng bộ**: Giao dịch xuyên service cần Saga Pattern
- **Giám sát phức tạp**: Cần tool theo dõi request qua nhiều service
- **Chi phí network**: Giao tiếp liên service tăng overhead

**Phù hợp khi:**
- Cần scale theo từng tính năng
- Team có kinh nghiệm DevOps
- Ưu tiên khả năng chịu tải cao
