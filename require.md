Xây dựng ứng dụng Mini Shop có đăng nhập, xem sản phẩm, thêm vào giỏ hàng, và trang quản trị, ứng dụng có các chức năng như dưới: (Yêu cầu chỉ xây dựng Backend API):

Trang chủ /
    Hiển thị tiêu đề Mini Profile App
    Nếu chưa đăng nhập, hiển thị Bạn chưa đăng nhập
    Nếu đã đăng nhập, hiển thị Xin chào, <username>
    Hiển thị giao diện theo theme lấy từ cookie: light hoặc dark
    Chức năng chọn theme bằng cookie
    Route /set-theme/:theme
    Cho phép lưu cookie theme
    Chỉ chấp nhận light hoặc dark
    Cookie sống trong 10 phút
    Khi quay lại trang /, giao diện hoặc nội dung phải đổi theo theme đã lưu

Chức năng đăng nhập bằng session
    Route /login
    Form nhập username
    Khi submit, lưu username vào session
    Đồng thời lưu thêm loginTime vào session

Trang cá nhân /profile
    Chỉ cho truy cập khi đã đăng nhập
    Hiển thị: Username, Thời điểm đăng nhập, Số lần đã truy cập trang /profile trong phiên hiện tại, Mỗi lần F5 trang này, bộ đếm tăng lên 1
Chức năng đăng xuất
    Route /logout
    Xóa session
    Sau khi logout, truy cập /profile phải bị chặn và chuyển về /login