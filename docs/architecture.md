# System Architecture

## Tổng quan
- **Chức năng chính** : Hệ thống đặt vé xem phim cho phép người dùng tìm kiếm, đặt vé xem phim.
- **Các thành phần** : Hệ thống bao gồm các microservice độc lập, mỗi service chịu trách nhiệm 
cho một chức năng nghiệp vụ cụ thể, cùng với một API Gateway để quản lý và định tuyến các yêu cầu từ client.

## Thành phần hệ thống
- **Movie Service**: Quản lý thông tin về phim, suất chiếu và tình trạng ghế ngồi. 
- **Customer Service**: Quản lý thông tin khách hàng, bao gồm việc nhận thông tin cá nhân và xác minh thông tin đặt vé.
- **Payment Service**: Quản lý việc xử lý thanh toán vé xem phim trực tuyến. 
- **Seat Availability Service**: Kiểm tra và xác minh tình trạng ghế ngồi, đảm bảo ghế ngồi đã chọn còn trống trước khi đặt vé.
- **Notification Service**: Gửi email thông báo xác nhận đặt vé thành công đến khách hàng sau khi thanh toán hoàn tất.
- **Ticket Booking Service**: Xử lý toàn bộ quy trình đặt vé từ khi khách hàng chọn phim, ghế ngồi, xác minh thông tin, đến khi thanh toán và gửi thông báo xác nhận.
- **Discovery Service**: Giúp các service giao tiếp với nhau
- **API Gateway**: Là điểm vào duy nhất cho tất cả các yêu cầu từ client. Nó chịu trách nhiệm định tuyến các yêu cầu đến microservice thích hợp, xác thực, giới hạn tốc độ (rate limiting), và tổng hợp phản hồi nếu cần.

## Các giao tiếp

## Luồng dữ liệu
- **Yêu cầu từ người dùng**: Client gửi request (ví dụ: tìm phim, đặt vé) đến API Gateway.
- **Gateway Routing**: API Gateway xác thực request và định tuyến đến service phù hợp (ví dụ: `movie-service` để tìm kiếm, `ticket-booking-service` để đặt vé).
- **Tương tác giữa các dịch vụ** : 
  - `booking-service` có thể gọi `seat-availability-service` để kiểm tra ghế trống.
  - Sau khi đặt phòng thành công, `ticker-booking-service` gửi message tới RabbitMQ cho `notification-service`.
- **Tương tác với CSDL/nơi lưu trữ dữ liệu**: Mỗi service tương tác với cơ sở dữ liệu riêng của nó sử dụng MySQL.

## Thiết kế
![Microservice](../docs/assets/microservices.jpg)
## Khả năng mở rộng & chịu lỗi
- **Khả năng mở rộng**:
    - Từng microservice có thể được scale độc lập dựa trên tải (ví dụ: scale `notification-service` trong mùa cao điểm khuyến mãi).
    - Sử dụng Docker và các công cụ điều phối container (như Kubernetes, mặc dù hiện tại là Docker Compose) để dễ dàng tăng số lượng instance của mỗi service.
- **Khả năng chịu lỗi**:
    - Lỗi trong một service (ví dụ: `notification-service` tạm thời không gửi được email) không làm sập toàn bộ hệ thống. Các service khác vẫn có thể hoạt động.
    - API Gateway có thể cài đặt cơ chế retry hoặc circuit breaker để xử lý lỗi từ các service backend.
    - Việc tách biệt cơ sở dữ liệu giúp giảm thiểu ảnh hưởng chéo khi một DB gặp sự cố.

