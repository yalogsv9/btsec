# Spring Security 6 — Ví dụ 1 Design

**Mục tiêu:** Xây dựng ví dụ Spring Security 6 đầu tiên trong một ứng dụng Spring Boot, sẵn sàng chạy và commit riêng.

## Phạm vi

- Một ứng dụng Maven Spring Boot đặt tại `SPRING_WS`.
- `vidu1` triển khai xác thực form login với Spring Security 6 và các trang Thymeleaf tối thiểu.
- Các trang công khai, đăng nhập, khu vực yêu cầu xác thực và đăng xuất hoạt động rõ ràng.
- Chưa triển khai nội dung của `vidu2`, không tạo README.

## Kiến trúc

`SecurityConfig` cung cấp `SecurityFilterChain`, người dùng in-memory có mật khẩu BCrypt và `PasswordEncoder`. Controller riêng cho `vidu1` trả về các view Thymeleaf. Static CSS chỉ phục vụ cách trình bày; toàn bộ phân quyền do Spring Security kiểm soát.

## Luồng hoạt động

1. Người dùng mở trang chủ công khai.
2. Truy cập `/vidu1/private` chưa đăng nhập được chuyển tới `/login`.
3. Đăng nhập thành công chuyển tới khu vực riêng; lỗi xác thực hiển thị trên form.
4. Đăng xuất xoá phiên và quay về trang chủ.

## Kiểm thử

- `MockMvc` xác nhận trang chủ công khai, trang riêng bị redirect đến login và đăng nhập thành công trả về trang riêng.
- Maven chạy toàn bộ test trước commit.

## Ràng buộc

- Java 17, Spring Boot 3.x, Spring Security 6.x.
- Mỗi ví dụ được commit riêng; commit message ngắn một dòng, không trailer `Co-authored-by`.
