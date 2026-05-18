# Báo Cáo: Gửi Đơn Hàng Mới - POST hay PUT?

## Phần 1: Lý Thuyết

### 1.1. So Sánh POST và PUT

#### POST Method
- **Mục đích**: Tạo tài nguyên **mới** trên server
- **Idempotent**: ❌ **KHÔNG** - Lặp lại request nhiều lần sẽ tạo nhiều tài nguyên khác nhau
- **URL**: `/api/v1/orders` (không cần ID trong URL)
- **Body**: Chứa dữ liệu để tạo tài nguyên mới
- **HTTP Status khi thành công**: `201 Created`
- **Ví dụ**:
  ```
  POST /api/v1/orders HTTP/1.1
  Content-Type: application/json
  
  {
    "customerName": "Nguyễn Văn A",
    "address": "123 Đường ABC, TP HCM",
    "productId": "PROD001",
    "quantity": 5
  }
  
  Response: 201 Created
  {
    "orderId": "550e8400-e29b-41d4-a716-446655440000",
    "customerName": "Nguyễn Văn A",
    "address": "123 Đường ABC, TP HCM",
    "productId": "PROD001",
    "quantity": 5,
    "createdAt": "2026-05-18T10:30:00"
  }
  ```

#### PUT Method
- **Mục đích**: **Cập nhật** tài nguyên hiện có (hoặc tạo nếu chưa tồn tại)
- **Idempotent**: ✅ **CÓ** - Lặp lại request nhiều lần sẽ cho kết quả giống nhau
- **URL**: `/api/v1/orders/{id}` (cần ID cụ thể)
- **Body**: Chứa đầy đủ dữ liệu để thay thế tài nguyên
- **HTTP Status khi thành công**: `200 OK` hoặc `204 No Content`
- **Ví dụ**:
  ```
  PUT /api/v1/orders/550e8400-e29b-41d4-a716-446655440000 HTTP/1.1
  Content-Type: application/json
  
  {
    "customerName": "Nguyễn Văn A",
    "address": "456 Đường XYZ, Hà Nội",
    "productId": "PROD002",
    "quantity": 10
  }
  
  Response: 200 OK (cập nhật order)
  ```

### 1.2. Bảng So Sánh

| Tiêu Chí                          | POST                     | PUT                              |
|-----------------------------------|--------------------------|----------------------------------|
| **Tạo tài nguyên mới?**           | ✅ Có (mục đích chính)    | ❌ Cập nhật (tạo là phụ)          |
| **Cập nhật tài nguyên?**          | ❌ Không                  | ✅ Có                             |
| **Idempotent?**                   | ❌ Không                  | ✅ Có                             |
| **Cần ID trong URL?**             | ❌ Không                  | ✅ Có                             |
| **HTTP Status**                   | 201 Created              | 200 OK / 204 No Content          |
| **Ai quản lý ID?**                | Server sinh ID           | Client cung cấp hoặc server sinh |
| **Gọi 2 lần với data giống nhau** | Tạo 2 resource khác nhau | Kết quả giống nhau               |

### 1.3. Khi Nào Dùng

**Dùng POST khi:**
- Tạo tài nguyên mới
- Server quản lý và sinh ID (UUID, auto-increment, ...)
- Client không biết ID trước
- Không cần idempotent (và không nên là idempotent cho creation)
- Gọi lại request tạo mới không được phép giống lần trước

**Dùng PUT khi:**
- Cập nhật tài nguyên hiện có
- Biết ID của tài nguyên cần cập nhật
- Cần idempotent (gọi lại cũng cho kết quả giống)
- Client có thể chỉ định ID

### 1.4. Giải Thích: Ai Đúng?

#### ✅ **ĐỒNG NGHIỆP B ĐÚNG** - Phải dùng **POST**

**Lý do:**

1. **REST Semantics (ngữ nghĩa REST)**:
   - POST = CREATE (tạo mới)
   - PUT = UPDATE (cập nhật)
   - Trong bài tập này chúng ta **tạo mới** đơn hàng, nên phải dùng **POST**

2. **Server Quản Lý ID**:
   - Client gửi lên thông tin đơn hàng nhưng **không có orderId**
   - Server tự sinh `orderId` bằng UUID
   - PUT yêu cầu ID phải có trong URL: `/api/v1/orders/{orderId}`
   - Nhưng client chưa biết orderId (server mới sinh)
   - **Vô lý** với PUT pattern

3. **Hành Vi Idempotent**:
   - **POST không idempotent** là **CHÍNH XÁC** cho create operation
   - Gọi POST 2 lần = tạo 2 đơn hàng khác nhau (hành vi mong muốn)
   - Gọi PUT 2 lần = cập nhật lại cùng 1 đơn hàng (hành vi không mong muốn trong trường hợp create)

4. **HTTP Status Code**:
   - POST thành công trả `201 Created` (chuẩn cho resource mới)
   - PUT thành công trả `200 OK` (chuẩn cho update)

#### ❌ **Vì Sao Đồng Nghiệp A Sai** (dùng PUT):

1. **Semantic sai**:
   - "Đưa dữ liệu lên server" không phải lý do để dùng PUT
   - POST cũng đưa dữ liệu lên server
   - PUT được dùng cho **update**, không phải **create**

2. **URL không phù hợp**:
   - PUT cần URL: `/api/v1/orders/{id}`
   - Nhưng client chưa biết id (server mới sinh)
   - Mâu thuẫn!

3. **Idempotent gây vấn đề**:
   ```
   PUT /api/v1/orders/uuid-1 (request gọi lần 1)
   → Create một order với id = uuid-1
   
   PUT /api/v1/orders/uuid-1 (request gọi lần 2 giống hệt)
   → Update lại order uuid-1 (không tạo order mới!)
   
   Kết quả: Chỉ có 1 order → SAI
   ```

### 1.5. Ví Dụ Minh Họa: Gọi 2 Lần

**Nếu dùng POST (✅ ĐÚNG)**:
```
Lần 1: POST /api/v1/orders
Request:
{
  "customerName": "Nguyễn Văn A",
  "address": "123 Đường ABC",
  "productId": "PROD001",
  "quantity": 5
}

Response: 201 Created
{
  "orderId": "550e8400-...",
  ...
}

---

Lần 2: POST /api/v1/orders (request y hệt lần 1)
Request:
{
  "customerName": "Nguyễn Văn A",
  "address": "123 Đường ABC",
  "productId": "PROD001",
  "quantity": 5
}

Response: 201 Created
{
  "orderId": "f47ac10b-...",  ← ID KHÁC!
  ...
}

Kết quả: 2 order, 2 orderId khác nhau ✅ CHÍNH XÁC
```

**Nếu Dùng PUT (❌ SAI)**:
```
Lần 1: PUT /api/v1/orders/550e8400-... 
Response: 200 OK (create/update)

Lần 2: PUT /api/v1/orders/550e8400-... (request y hệt)
Response: 200 OK (update lại)

Kết quả: Chỉ có 1 order ❌ SAI - không tạo mới được
```

---

## Phần 2: Thực Thi

### 2.1. Thiết Kế Endpoint

**Endpoint**: `POST /api/v1/orders`

**Request Body**:
```json
{
  "customerName": "string (bắt buộc)",
  "address": "string (bắt buộc)",
  "productId": "string (bắt buộc)",
  "quantity": "integer (bắt buộc, > 0)"
}
```

**Response (201 Created)**:
```json
{
  "orderId": "string (UUID)",
  "customerName": "string",
  "address": "string",
  "productId": "string",
  "quantity": "integer",
  "createdAt": "LocalDateTime"
}
```

**Validation**:
- `customerName`: không được để trống
- `address`: không được để trống
- `productId`: không được để trống
- `quantity`: phải > 0

### 2.2. Cấu Trúc Code

#### 1. Model Order (`Order.java`)

```java
package com.example.ss04_bai2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String orderId;
    private String customerName;
    private String address;
    private String productId;
    private int quantity;
    private LocalDateTime createdAt;
}
```

**Giải thích**:
- `orderId`: ID sinh bằng UUID
- `customerName`, `address`, `productId`, `quantity`: Thông tin đơn hàng từ request
- `createdAt`: Thời gian tạo đơn hàng
- Sử dụng Lombok (`@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`) để giảm boilerplate code

#### 2. DTO CreateOrderRequest (`CreateOrderRequest.java`)

```java
package com.example.ss04_bai2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    
    @NotBlank(message = "Tên khách hàng không được để trống")
    private String customerName;
    
    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;
    
    @NotBlank(message = "ID sản phẩm không được để trống")
    private String productId;
    
    @NotNull(message = "Số lượng không được để trống")
    @Positive(message = "Số lượng phải lớn hơn 0")
    private Integer quantity;
}
```

**Giải thích**:
- DTO (Data Transfer Object) dùng để nhận dữ liệu từ request
- Sử dụng Jakarta Validation Annotations:
  - `@NotBlank`: Kiểm tra String không được trống
  - `@NotNull`: Kiểm tra field không null
  - `@Positive`: Kiểm tra số phải > 0
- Tách DTO ra vì Order không cần `orderId` và `createdAt` khi nhận request (server sinh)

#### 3. REST Controller (`OrderController.java`)

```java
package com.example.ss04_bai2.controller;

import com.example.ss04_bai2.model.Order;
import com.example.ss04_bai2.dto.CreateOrderRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        // Sinh orderId bằng UUID
        String orderId = UUID.randomUUID().toString();
        
        // Ghi nhận thời gian tạo
        LocalDateTime createdAt = LocalDateTime.now();
        
        // Tạo đối tượng Order
        Order order = new Order(
            orderId,
            createOrderRequest.getCustomerName(),
            createOrderRequest.getAddress(),
            createOrderRequest.getProductId(),
            createOrderRequest.getQuantity(),
            createdAt
        );
        
        // Trả về HTTP 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
```

**Giải thích**:
- `@RestController`: Đánh dấu class là REST API controller
- `@RequestMapping("/api/v1/orders")`: Base path cho endpoint
- `@PostMapping`: Xử lý HTTP POST request
- `@Valid @RequestBody`: Validate dữ liệu request body trước khi xử lý
- `UUID.randomUUID().toString()`: Sinh orderId ngẫu nhiên là UUID
- `LocalDateTime.now()`: Ghi nhận thời gian hiện tại
- `ResponseEntity.status(HttpStatus.CREATED)`: Trả về HTTP 201 Created
- Server tự quản lý ID, client không cần cung cấp

### 2.3. Cách Sử Dụng

**1. Khởi động ứng dụng**:
```bash
cd C:\Users\Admin\Desktop\IT211\ss04_bai2
gradlew.bat bootRun
```

**2. Test bằng Postman hoặc cURL**:

```bash
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Nguyễn Văn A",
    "address": "123 Đường ABC, TP HCM",
    "productId": "PROD001",
    "quantity": 5
  }'
```

**3. Response**:
```json
HTTP/1.1 201 Created

{
  "orderId": "550e8400-e29b-41d4-a716-446655440000",
  "customerName": "Nguyễn Văn A",
  "address": "123 Đường ABC, TP HCM",
  "productId": "PROD001",
  "quantity": 5,
  "createdAt": "2026-05-18T10:30:45"
}
```

### 2.4. Các File Tạo Ra

```
ss04_bai2/
├── src/main/java/com/example/ss04_bai2/
│   ├── Ss04Bai2Application.java          (Main class - không thay đổi)
│   ├── model/
│   │   └── Order.java                    (✅ Tạo mới)
│   ├── dto/
│   │   └── CreateOrderRequest.java       (✅ Tạo mới)
│   ├── controller/
│   │   └── OrderController.java          (✅ Tạo mới)
│   └── resources/
│       └── application.properties
├── build.gradle                          (Cập nhật dependencies)
└── BAO_CAO.md                            (File này)
```

---

## Kết Luận

**Đáp án**: ✅ **ĐỒNG NGHIỆP B ĐÚNG**

- **HTTP Method**: POST (không phải PUT)
- **Endpoint**: POST /api/v1/orders
- **Nguyên nhân**: 
  - POST dùng để **tạo tài nguyên mới** (semantic REST)
  - PUT dùng để **cập nhật tài nguyên** (không phù hợp)
  - Server sinh orderId (client không biết ID trước)
  - HTTP 201 Created là status chuẩn cho create operation

- **Implementation**: 
  - Model Order chứa orderId, customerName, address, productId, quantity, createdAt
  - DTO CreateOrderRequest cho validation
  - Controller tạo orderId bằng UUID.randomUUID()
  - Trả về HTTP 201 Created + Order object đầy đủ

