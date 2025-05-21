# 📊 Hệ thống đặt vé xem phim - Phân Tích và Thiết Kế

---

## Thành viên

Nhóm 21:

| Họ và tên        | Mã sinh viên |
|------------------|--------------|
| Nguyễn Thế Dũng  | B21DCCN275   |
| Nguyễn Đắc Phong | B21DCCN587   |
| Đàm Công Thoại   | B21DCCN695   |


---


## 1. 🎯  Mô Tả Bài Toán và Mô hình nghiệp vụ

> **Bài toán:** Hệ thống đặt phòng khách sạn trực tuyến

_Mô tả nghiệp vụ_

Hệ thống đặt vé xem phim cho phép khách hàng chọn phim, suất chiếu, ghế ngồi và tiến hành đặt vé trực tuyến.
Sau khi khách hàng chọn phim, hệ thống sẽ kiểm tra tình trạng chỗ ngồi, xác minh thông tin vé, và xử lý thanh toán. 
Nếu đặt vé thành công, hệ thống sẽ gửi thông báo xác nhận vé đã đặt đến email của khách hàng.

---

## 2. 🧩 Xác định các Microservice

_Xác định các thực thể_

**Đối tượng sử dụng**
- Khách hàng cá nhân

**Mục tiêu chính**
- Cho phép khách hàng tìm kiếm và đặt vé trực tuyến
- Tự động hóa quy trình đặt vé và thông báo

**Dữ liệu xử lý**
- Thông tin người dùng
- Thông tin phim, vé, chỗ ngồi và trạng thái chỗ ngồi
- Thông tin thanh toán

_Thiết kế thực thể_

![Entity Relationship Diagram](../docs/assets/entities.jpg)


_Thiết kế những dịch vụ_

**Đặt vé xem phim**

1. Bắt đầu quy trình đặt vé: Khách hàng truy cập hệ thống và bắt đầu quy trình đặt vé xem phim.

2. Chọn phim và suất chiếu: Khách hàng chọn phim muốn xem, cùng với suất chiếu.

3. Chọn ghế ngồi: Khách hàng chọn ghế ngồi từ danh sách các ghế còn trống.

4. Tạo đơn hàng với trạng thái đang xử lý và xác minh thông tin phim, suất chiếu và ghế ngồi còn trống:
Hệ thống kiểm tra tình trạng ghế ngồi đã được chọn xem còn trống hay đã có người đặt.

5. Nếu không còn ghế ngồi, kết thúc quy trình: Nếu ghế đã được đặt bởi người khác, hệ thống thông báo và dừng quy trình.

6. Nhập thông tin khách hàng: Khách hàng cung cấp các thông tin cá nhân như họ tên, số điện thoại và email.

7. Xác minh thông khách hàng: Hệ thống xác minh thông tin khách hàng và lưu vào cơ sở dữ liệu.

8. Nhập và thực hiện thanh toán: Khách hàng tiến hành thanh toán.

9. Nếu thanh toán thành công, cập nhật tình trạng ghế ngồi: Hệ thống cập nhật tình trạng ghế đã được đặt vào cơ sở dữ liệu.

10. Xác nhận hoàn thành đơn hàng và gửi email thông báo cho khách hàng. Nếu xác nhận thất bại thì gửi yêu cầu hoàn tiền và hủy đặt ghế.


## 3. 🔄 Giao tiếp giữa các dịch vụ

### **Cơ chế chính**
![flow](../docs/assets/flow.jpg)


#### Service Candidates:

| **Service**                | **Mô tả chức năng**                                               |
|----------------------------|-------------------------------------------------------------------|
| API Gateway                | Định tuyến request từ client đến các service phù hợp              |
| Ticket Booking Service     | Xử lý toàn bộ quy trình đặt vé, điều phối giữa các service        |
| Movie Service              | Quản lý thông tin phim, suất chiếu và danh sách ghế               |
| Customer Service           | Quản lý thông tin khách hàng                                      |
| Payment Service            | Xử lý thanh toán trực tuyến                                       |
| Seat Availability Service  | Kiểm tra tình trạng ghế trống trước khi đặt vé                    |
| Notification Service       | Gửi thông báo xác nhận đặt vé cho khách hàng qua email            |
| Discovery Service          | Cho phép các service đăng ký và phát hiện nhau (service registry) |



#### Service Capabilities:

| **Service**               | **Chức năng chính**                                                                          |
|---------------------------|----------------------------------------------------------------------------------------------|
| API Gateway               | - Định tuyến request                                                                         |
| Ticket Booking Service    | - Chọn phim, suất chiếu, ghế<br>- Xác minh khách hàng<br>- Gọi thanh toán<br>- Gửi thông báo |
| Movie Service             | - Quản lý thông tin rạp, phim, phòng chiếu, suất chiếu, danh sách ghế của phòng              |
| Customer Service          | - Quản lý thông tin khách hàng<br>- Xác minh thông tin khi đặt vé                            |
| Payment Service           | - Xử lý yêu cầu thanh toán                                                                   |
| Seat Availability Service | - Kiểm tra và xác minh tình trạng ghế trống trong suất chiếu cụ thể                          |
| Notification Service      | - Nhận message từ hàng đợi<br>- Gửi email thông báo xác nhận                                 |
| Discovery Service         | - Đăng ký service<br>- Phát hiện service khác (Eureka registry)                              |



#### Interactions:

| **Kết nối**                                        | **Protocol** | **Mục đích**                                                                       |
|----------------------------------------------------|--------------|------------------------------------------------------------------------------------|
| Client ↔ API Gateway                               | HTTP/REST    | Tất cả request từ client                                                           |
| API Gateway ↔ Ticket Booking Service               | HTTP/REST    | Định tuyến yêu cầu đặt vé                                                          |
| API Gateway ↔ Movie Service                        | HTTP/REST    | Lấy thông tin phim, suất chiếu, ghế                                                |
| API Gateway ↔ Seat availability Service            | HTTP/REST    | Lấy thông tin tình trạng ghế ngồi của suất chiếu                                   |
| Ticket Booking Service ↔ Movie Service             | RabbitMQ     | Lấy thông tin phim, rạp, suất chiếu, danh sách ghế                                 |
| Seat Availability Service ↔ Movie Service          | HTTP/REST    | Lấy thông tin ghế ngồi và khởi tạo trạng thái                                      |
| Ticket Booking Service ↔ Seat Availability Service | RabbitMQ     | Kiểm tra ghế trống trước khi đặt, giữ hoặc bỏ giữ ghế, xác nhận tình trạng đặt ghế |
| Ticket Booking Service ↔ Payment Service           | RabbitMQ     | Gửi yêu cầu thanh toán                                                             |
| Ticket Booking Service ↔ Customer Service          | RabbitMQ     | Xác minh/ghi nhận thông tin khách hàng                                             |
| Ticket Booking Service ↔ Customer Service          | HTTP/REST    | Lấy thông tin khách hàng                                                           |
| Ticket Booking Service ↔ Notification Service      | RabbitMQ     | Gửi thông báo không đồng bộ sau khi đặt vé                                         |
| Notification Service ↔ Customer Service            | HTTP/REST    | Lấy thông tin khách hàng                                                           |
| Các Services ↔ Discovery Service                   | Eureka       | Đăng ký & phát hiện service                                                        |



#### Data Ownership:

| **Service**                  | **Dữ liệu sở hữu**                                                               |
|------------------------------|----------------------------------------------------------------------------------|
| Movie Service                | - Danh sách phim, rạp, suất chiếu, phòng chiếu, danh sách ghế                    |
| Customer Service             | - Thông tin khách hàng                                                           |
| Payment Service              | - Giao dịch thanh toán                                                           |
| Ticket Booking Service       | - Thông tin đơn đặt vé tạm thời<br>- Liên kết khách hàng – ghế – phim - payment  |
| Seat Availability Service    | - Trạng thái ghế theo suất chiếu                                                 |
| Notification Service         | - Nhật ký gửi thông báo                                                          |

