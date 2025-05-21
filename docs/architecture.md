# System Architecture

## Tổng quan
- **Chức năng chính** : Hệ thống đặt vé xem phim cho phép người dùng tìm kiếm, đặt vé xem phim.
- **Các thành phần** : Hệ thống bao gồm các microservice độc lập, mỗi service chịu trách nhiệm 
cho một chức năng nghiệp vụ cụ thể, cùng với một API Gateway để quản lý và định tuyến các yêu cầu từ client.

## Thành phần hệ thống
- **Movie Service**: Quản lý thông tin về phim, suất chiếu và danh sách ghế ngồi của các phòng chiếu. 
- **Customer Service**: Quản lý thông tin khách hàng, bao gồm việc nhận thông tin cá nhân và xác minh thông tin khách hàng.
- **Payment Service**: Quản lý việc xử lý thanh toán vé xem phim trực tuyến. 
- **Seat Availability Service**: Kiểm tra và xác minh tình trạng ghế ngồi, đảm bảo ghế ngồi đã chọn còn trống trước khi đặt vé.
- **Notification Service**: Gửi email thông báo xác nhận đặt vé thành công đến khách hàng sau khi đặt hàng hoàn tất.
- **Ticket Booking Service**: Xử lý toàn bộ quy trình đặt vé từ khi khách hàng chọn phim, ghế ngồi, xác minh thông tin, đến khi thanh toán và gửi thông báo xác nhận.
- **Discovery Service**: Hỗ trợ API gateway định tuyến tới các service và các service giao tiếp với nhau một cách linh hoạt
- **API Gateway**: Là điểm vào duy nhất cho tất cả các yêu cầu từ client. Nó chịu trách nhiệm định tuyến các yêu cầu đến microservice tương ứng.

## Các giao tiếp
- **Client ↔ API Gateway**: Giao tiếp qua HTTP/REST. Tất cả các yêu cầu từ phía client sẽ đi qua API Gateway.
- **API Gateway ↔ Core Services**: Chủ yếu qua HTTP/REST. API Gateway định tuyến các yêu cầu đến các service nghiệp vụ như 
`ticket-booking-service`, `movie-service`, `seat-avalability-service`.
- **ticket-booking-service ↔ movie-service**: Giao tiếp không đồng bộ qua RabbitMQ. `ticket-booking-service` publish message yêu cầu `movie-service`
xử lý xác minh thông tin phim và lắng nghe message phản hồi của nó.
- **ticket-booking-service ↔ seat-availability-service**: Giao tiếp không đồng bộ qua RabbitMQ. `ticket-booking-service` publish message yêu cầu `seat-availability-service`
xử lý xác minh tình trạng ghế trống và lắng nghe message phản hồi của nó. Publish message yêu cầu `seat-availability-service`
hủy giữ ghế khi đặt vé thất bại hoặc xác nhận đặt ghế khi đặt vé thành công.
- **ticket-booking-service ↔ customer-service**: Giao tiếp không đồng bộ qua RabbitMQ. `ticket-booking-service` publish message yêu cầu `customer-service`
xử lý xác minh thông tin khách hàng và lắng nghe message phản hồi của nó. Gọi `customer-service` bằng REST API qua openfeign để lấy thông tin khách hàng.
- **ticket-booking-service ↔ payment-service**: Giao tiếp không đồng bộ qua RabbitMQ. `ticket-booking-service` publish message yêu cầu `payment-service`
xử lý thông tin thanh toán và lắng nghe message phản hồi của nó. Publish message yêu cầu `payment-service` hoàn tiền nếu đặt vé thất bại.
- **seat-availability-service ↔ movie-service**: `seat-availability-service` gọi `movie-service` bằng REST API qua openfeign 
để lấy thông tin ghế ngồi và khởi tạo danh sách ghế ngồi và trạng thái ghế cho các suất chiếu.
- **notification-service ↔ customer-service**: `notification-service` gọi `customer-service` bằng REST API qua openfeign để lấy thông tin khách hàng.
- **ticket-booking-service ↔ notification-service**: Giao tiếp không đồng bộ qua RabbitMQ. `ticket-booking-service` 
publish một message khi hoàn thành đơn hàng tới `notification-service` để gửi email cho khách hàng.
- **Networking nội bộ**: API gateway và các service giao tiếp với nhau trong mạng nội bộ của Docker Compose sử dụng tên dịch vụ.


## Luồng dữ liệu
- **Yêu cầu từ người dùng**: Client gửi request (ví dụ: chọn phim, chọn suất chiếu) đến API Gateway.
- **Gateway Routing**: API Gateway định tuyến đến `ticket-booking-service` để bắt đầu tiến hành đặt vé.
- **Tương tác giữa các dịch vụ** : 
  - `ticket-booking-service` gọi `movie-service` để xác minh thông tin phim và suất chiếu hợp lệ. Nếu thành công thì tiếp tục bước tiếp theo.
  - `ticket-booking-service` gọi `seat-availability-service` để kiểm tra tình trạng ghế trống và thực hiện giữ ghế nếu thành công. Nếu thành công thì tiếp tục bước tiếp theo.
  - `ticket-booking-service` thông báo cho client để yêu cầu người dùng nhập thông tin cá nhân và gọi `customer-service`
để xác minh và lưu thông tin khách hàng. Tiếp tục bước tiếp theo nếu thành công, ngược lại gọi `seat-availability-service` để hủy giữ ghế.
  - `ticket-booking-service` thông báo cho client để yêu cầu người dùng nhập thông tin thanh toán và gọi `payment-service` để
tiến thành kiểm tra thanh toán và lưu lịch sử thanh toán. Tiếp tục bước tiếp theo nếu thành công. Ngược lại gọi
`seat-availability-service` để hủy giữ ghế.
  - `ticket-booking-service` gọi `seat-availability-service` để xác nhận đặt ghế và cập nhật trạng thái đơn hàng thành công. Tiếp tục bước tiếp theo nếu thành công,
ngược lại gọi `seat-availability-service` để hủy giữ ghế và gọi `payment-service` để hoàn tiền.
  - Sau khi đặt phòng thành công, `ticker-booking-service` gọi `notification-service` để gửi email thông báo cho khách hàng.
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

