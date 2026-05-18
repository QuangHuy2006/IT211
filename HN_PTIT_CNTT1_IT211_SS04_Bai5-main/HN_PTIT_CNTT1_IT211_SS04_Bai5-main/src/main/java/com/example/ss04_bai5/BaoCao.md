# 🚌 API Specification - Hệ Thống Đặt Vé Xe Khách

**Phiên bản API:** v1  
**Base URL:** `/api/v1`  
**Ngày thiết kế:** 18/05/2026  

---

## Phần 1: Bảng Liệt Kê 6 Endpoint API

| # | HTTP Method | Endpoint URL                   | Mô tả                                 | Request Body/Param                              | Response                                                                                                     |
|---|-------------|--------------------------------|---------------------------------------|-------------------------------------------------|--------------------------------------------------------------------------------------------------------------|
| 1 | **GET**     | `/api/v1/trips`                | Lấy danh sách tất cả chuyến xe có sẵn | Query: `?page=1&limit=10`                       | `200 OK` - Danh sách chuyến xe với phân trang                                                                |
| 2 | **GET**     | `/api/v1/trips/{tripId}`       | Lấy thông tin chi tiết một chuyến xe  | Path: `tripId`                                  | `200 OK` - Chi tiết chuyến xe; `404 Not Found` - Chuyến không tồn tại                                        |
| 3 | **GET**     | `/api/v1/trips/search`         | Tìm chuyến xe theo tuyến đường        | Query: `?from=Hà Nội&to=TP.HCM&date=2026-05-20` | `200 OK` - Danh sách chuyến xe phù hợp; `400 Bad Request` - Thiếu tham số                                    |
| 4 | **POST**    | `/api/v1/bookings`             | Đặt vé mới cho một chuyến xe          | JSON: `{tripId, passengerName, phone, email}`   | `201 Created` - Vé được tạo; `400 Bad Request` - Dữ liệu không hợp lệ; `409 Conflict` - Hết chỗ              |
| 5 | **DELETE**  | `/api/v1/bookings/{bookingId}` | Hủy vé đã đặt                         | Path: `bookingId`                               | `204 No Content` - Hủy thành công; `404 Not Found` - Vé không tồn tại                                        |
| 6 | **PUT**     | `/api/v1/bookings/{bookingId}` | Cập nhật thông tin hành khách trên vé | JSON: `{passengerName, phone, email}`           | `200 OK` - Cập nhật thành công; `400 Bad Request` - Dữ liệu không hợp lệ; `404 Not Found` - Vé không tồn tại |

---

## Phần 2: Giải Thích Quyết Định Thiết Kế

### **1. GET /api/v1/trips** - Lấy danh sách chuyến xe
- **HTTP Method:** `GET`
- **Lý do chọn:** 
  - Lấy dữ liệu (không thay đổi trạng thái server)
  - Không có side effect
  - Có thể cache kết quả
  - Idempotent - gọi nhiều lần cho kết quả giống nhau
  - **Phân trang:** Sử dụng Query Parameter `?page=1&limit=10` để tối ưu hóa khi có lượng dữ liệu lớn

---

### **2. GET /api/v1/trips/{tripId}** - Lấy chi tiết chuyến xe
- **HTTP Method:** `GET`
- **Lý do chọn:**
  - Lấy một resource cụ thể (theo ID)
  - Không thay đổi dữ liệu
  - Idempotent
  - **Path Parameter:** Sử dụng `{tripId}` vì đây là identifier duy nhất và là phần của resource URL

---

### **3. GET /api/v1/trips/search** - Tìm kiếm chuyến xe theo tuyến đường
- **HTTP Method:** `GET`
- **Lý do chọn:**
  - Đây là hoạt động tìm kiếm (read-only)
  - Không thay đổi trạng thái
  - Idempotent
  - **Query Parameters:** `?from=...&to=...&date=...` vì các điều kiện tìm kiếm là tùy chọn/linh hoạt, không phải resource identifier cố định
  - **Lưu ý:** Đặt `/search` trước `{tripId}` để tránh nhầm lẫn với route `/trips/{tripId}`

**⚠️ Phân vân giữa 2 method:** Chúng tôi xem xét POST cho search, nhưng chọn GET vì:
- Search là operation **read-only**, không modify dữ liệu
- GET với query string tuân theo quy ước REST (đơn giản hơn POST)
- Client có thể bookmark/cache URL search
- Theo RESTful convention, POST thường dùng khi tạo resource mới

---

### **4. POST /api/v1/bookings** - Đặt vé mới
- **HTTP Method:** `POST`
- **Lý do chọn:**
  - **Tạo resource mới** (vé booking) trên server
  - Có side effect - thay đổi trạng thái (giảm số ghế trống, tạo record mới)
  - **Không idempotent** - gọi 2 lần = 2 đơn đặt vé khác nhau
  - Sử dụng **Request Body (JSON)** vì dữ liệu hành khách phức tạp, cần gửi đầy đủ thông tin
  - Response: `201 Created` (HTTP status cho tạo resource thành công)

---

### **5. DELETE /api/v1/bookings/{bookingId}** - Hủy vé
- **HTTP Method:** `DELETE`
- **Lý do chọn:**
  - Xóa một resource cụ thể
  - Tuân theo REST convention - DELETE cho xóa
  - Có side effect - thay đổi trạng thái (hủy vé, cộng lại ghế)
  - **Path Parameter:** `{bookingId}` là identifier của resource được xóa
  - Response: `204 No Content` (xóa thành công, không có response body)

---

### **6. PUT /api/v1/bookings/{bookingId}** - Cập nhật thông tin hành khách
- **HTTP Method:** `PUT`
- **Lý do chọn:**
  - **Cập nhật resource hiện tại** (không tạo mới)
  - Tuân theo REST convention - PUT cho cập nhật toàn bộ resource
  - Idempotent - cập nhật 2 lần với dữ liệu giống nhau = kết quả như nhau
  - Sử dụng **Request Body (JSON)** để gửi các trường cần cập nhật
  - **Path Parameter:** `{bookingId}` xác định vé nào được cập nhật

**⚠️ Phân vân giữa PUT vs PATCH:**
- Chúng tôi **chọn PUT** vì:
  - Cập nhật tất cả thông tin hành khách (name, phone, email) một lúc
  - PUT giữ ngữ nghĩa "replace" -thay thế toàn bộ resource
  - Đơn giản hơn, phù hợp với scenario này
  - PATCH sẽ dùng nếu cập nhật từng field riêng lẻ
- POST KHÔNG dùng vì POST dành cho tạo resource mới, không phải cập nhật

---

## Phần 3: Quy Tắc RESTful Được Áp Dụng

| Nguyên tắc            | Áp dụng                                                                           |
|-----------------------|-----------------------------------------------------------------------------------|
| **Stateless**         | Mỗi request chứa đủ thông tin, server không giữ session                           |
| **Resource-oriented** | URL là tên resource (trips, bookings), không phải action (getTrip, createBooking) |
| **HTTP Method chuẩn** | GET (read), POST (create), PUT (update), DELETE (delete)                          |
| **Versioning**        | `/api/v1/**` để hỗ trợ nâng cấp API trong tương lai                               |
| **Status Code chuẩn** | 200, 201, 204, 400, 404, 409 đúng theo HTTP semantics                             |
| **Path vs Query**     | Path: resource identifier (ID); Query: filter/pagination/search criteria          |

---

## Phần 4: Ví Dụ Request/Response

### **Ví dụ 1: GET /api/v1/trips/search?from=Hà Nội&to=TP.HCM&date=2026-05-20**
```json
Response 200 OK:
{
  "data": [
    {
      "tripId": "TR001",
      "from": "Hà Nội",
      "to": "TP.HCM",
      "departureTime": "2026-05-20T23:00:00",
      "arrivalTime": "2026-05-21T10:30:00",
      "price": 350000,
      "availableSeats": 5,
      "totalSeats": 40
    }
  ],
  "total": 1
}
```

### **Ví dụ 2: POST /api/v1/bookings**
```json
Request Body:
{
  "tripId": "TR001",
  "passengerName": "Nguyễn Văn A",
  "phone": "0901234567",
  "email": "nguyena@example.com"
}

Response 201 Created:
{
  "bookingId": "BK001",
  "tripId": "TR001",
  "passengerName": "Nguyễn Văn A",
  "phone": "0901234567",
  "email": "nguyena@example.com",
  "bookingDate": "2026-05-18T10:30:00",
  "seatNumber": "A15",
  "status": "CONFIRMED"
}
```

### **Ví dụ 3: DELETE /api/v1/bookings/BK001**
```
Response 204 No Content:
(No body - xóa thành công)
```

---

## Kết Luận
API spec này tuân theo **REST architecture** và **HTTP best practices**, đảm bảo:
- ✅ Dễ hiểu, dễ dùng cho frontend developer
- ✅ Scalable - sẵn sàng cho versioning v2, v3...
- ✅ Consistent - cùng quy ước HTTP untuk tất cả endpoints
- ✅ Robust - xử lý error cases rõ ràng

