# Spring Security — Ví dụ 3 Design

**Mục tiêu:** Bổ sung ví dụ Spring Security nhỏ, độc lập tại `/vidu3`, minh họa cơ chế “Remember me” bằng cookie mà không cần dịch vụ OAuth2 bên ngoài.

## Phạm vi

- Giữ nguyên đầy đủ Ví dụ 1 tại `/vidu1` và Ví dụ 2 tại `/vidu2`.
- Thêm Ví dụ 3 tại `/vidu3`, dùng tài khoản in-memory `rememberme` / `123456`.
- Form đăng nhập tại `/vidu3/login` có checkbox `remember-me`.
- Khi checkbox được chọn, Spring Security phát cookie remember-me hiệu lực 7 ngày. Cookie cho phép người dùng truy cập lại `/vidu3` khi session không còn.
- Đăng xuất tại `/vidu3/logout` phải hủy session và cookie remember-me, rồi chuyển tới `/vidu3/login?logout`.
- Không dùng OAuth2, Google credentials, database hay dependency mới.

## Kiến trúc

`Vidu3SecurityConfig` cung cấp `SecurityFilterChain` có `@Order(3)` và chỉ khớp `/vidu3/**`, nhờ vậy không thay đổi các chain của hai ví dụ trước. Chain dùng `InMemoryUserDetailsManager` riêng, form login tùy biến, và `TokenBasedRememberMeServices` được Spring Security cấu hình qua `rememberMe`.

`Vidu3Controller` trả về các view Thymeleaf login và home. Home lấy `Authentication` để hiển thị username cùng loại principal, giúp nhận biết người dùng vừa đăng nhập bằng session hay được phục hồi qua remember-me. Các template tái sử dụng CSS hiện có.

## Luồng hoạt động

1. Khách truy cập `/vidu3` được chuyển đến `/vidu3/login`.
2. Người dùng nhập `rememberme` / `123456`; không chọn checkbox thì nhận session thông thường.
3. Nếu chọn `remember-me`, phản hồi login tạo cookie remember-me với thời hạn 7 ngày.
4. Một yêu cầu mới tới `/vidu3` có cookie nhưng không có session được Spring Security xác thực tự động và hiển thị home.
5. POST `/vidu3/logout` với CSRF token xóa session và cookie, chuyển về `/vidu3/login?logout`.

## Xử lý lỗi

- Sai username hoặc password chuyển về `/vidu3/login?error=true`.
- Cookie remember-me bị xóa hoặc không hợp lệ không cấp quyền; truy cập `/vidu3` quay lại login.
- POST logout thiếu CSRF token bị từ chối theo mặc định của Spring Security.

## Kiểm thử

- MockMvc xác nhận anonymous redirect, đăng nhập thông thường, login lỗi, và đăng xuất CSRF.
- MockMvc xác nhận login chọn `remember-me` trả cookie; cookie đó xác thực được một request không có session.
- `mvn test` chạy toàn bộ bộ test của Ví dụ 1, 2 và 3.

## Ràng buộc

- Giữ Java 17, Spring Boot 3.5.0 và Spring Security 6.
- Không thêm dependency, OAuth2, Google credentials hoặc database cho Ví dụ 3.
- Duy trì ba security chain có matcher riêng: `/vidu1/**`, `/vidu2/**`, `/vidu3/**`.
- Mỗi ví dụ được commit riêng, message ngắn một dòng và không có trailer `Co-authored-by`.
