
# 🌐 Frontend (Angular 18)

## 📝 Tổng Quan

Đây là mô-đun giao diện người dùng (frontend) trong bài tập hệ thống microservice. Giao diện được xây dựng bằng **Angular 18**, chịu trách nhiệm hiển thị UI và giao tiếp với các dịch vụ backend.

## 🛠️ Công Nghệ Sử Dụng

* **Framework**: Angular 18
* **Styling**: SCSS
* **Trình quản lý gói**: npm

## 🚀 Bắt Đầu

1. Clone repository:

   ```bash
   git clone <repository-url>
   ```

2. Di chuyển vào thư mục frontend:

   ```bash
   cd frontend
   ```

3. Cài đặt dependencies:

   ```bash
   npm install
   ```

4. Khởi chạy server phát triển:

   ```bash
   ng serve
   ```

   hoặc:

   ```bash
   npm start
   ```

5. Truy cập trình duyệt tại:

   ```
   http://localhost:4200/
   ```

## 📁 Cấu Trúc Thư Mục

* `src/` – Chứa mã nguồn chính.
* `app/` – Thành phần, dịch vụ và module của ứng dụng.
* `assets/` – Các tệp tĩnh (ảnh, CSS...).
* `environments/` – Cấu hình môi trường (dev, prod).

## 🧾 Scripts

* `npm start` – Chạy ứng dụng ở chế độ phát triển.
* `npm run build` – Biên dịch project ở chế độ production.
* `ng test` – Chạy unit test.
* `ng lint` – Kiểm tra lỗi lint.
