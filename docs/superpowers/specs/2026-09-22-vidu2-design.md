# Spring Security — Ví dụ 2 Design

**Mục tiêu:** Bổ sung ví dụ custom login của tài liệu trong cùng ứng dụng, cho phép đăng nhập bằng username hoặc email và hiển thị dữ liệu người dùng đã xác thực.

## Phạm vi

- Giữ nguyên `vidu1` tại `/vidu1` và tài khoản `student` / `123456`.
- Thêm `vidu2` tại `/vidu2`, dùng H2 in-memory và Spring Data JPA.
- Khởi tạo `ROLE_USER` và người dùng `user01` / `user01@gmail.com` / `123456`, full name `Nguyễn Hữu Trung`, avatar mặc định nội bộ.
- Custom `UserDetailsService` truy vấn username hoặc email; trang login và home hiển thị đúng dữ liệu principal.
- Không tạo README và không triển khai `vidu3`.

## Kiến trúc

Hai `SecurityFilterChain` có matcher riêng ngăn ví dụ 2 ghi đè cấu hình ví dụ 1. Chain 1 chỉ xử lý `/vidu1/**`; chain 2 xử lý `/vidu2/**` và `/vidu2/login`. `vidu2` dùng entity `Role` và `AppUser` (tên tránh va chạm với `User` của Spring Security), repository, custom user details và DAO authentication provider.

## Luồng

1. Khách mở `/vidu2` được chuyển tới `/vidu2/login`.
2. Nhập `user01` hoặc `user01@gmail.com` cùng mật khẩu `123456`.
3. Dịch vụ xác thực tìm user bằng `findByUsernameOrEmail`, BCrypt kiểm tra mật khẩu, rồi chuyển về `/vidu2`.
4. Header hiển thị full name, username, email, role, avatar; đăng xuất về `/vidu2/login?logout` và huỷ session.
5. `/vidu2/admin/**` chỉ cho phép `ROLE_ADMIN`.

## Kiểm thử

- MockMvc kiểm tra anonymous redirect, login thành công bằng username/email, login sai redirect lỗi, header có thông tin user, logout với CSRF, và `/vidu2/admin/**` bị cấm với user thường.
- `mvn test` chạy đồng thời toàn bộ test `vidu1` và `vidu2`.

## Ràng buộc

- Giữ Spring Boot 3.5.0 và Java 17 để tương thích dự án hiện tại; không nâng lên Boot 4/Java 26 như bản tài liệu.
- Mọi file `vidu2` và cập nhật cấu hình sẽ ở một commit `feat: add vidu2`, không có trailer `Co-authored-by`.
