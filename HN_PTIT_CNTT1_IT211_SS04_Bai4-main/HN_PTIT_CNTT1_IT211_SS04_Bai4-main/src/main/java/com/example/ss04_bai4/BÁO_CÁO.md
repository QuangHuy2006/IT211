# BÀI TẬP: CRUD API - PUT và DELETE Đúng Cách

## PHẦN 1: PHÂN TÍCH CÁC LỖI

### Khai báo của Junior dev (SAI):
```java
@PostMapping("/products/{productId}")
public Product updateProduct(@PathVariable String productId, @RequestBody Product p) { ... }
```

---

### ❌ LỖI 1: Sử dụng `@PostMapping` thay vì `@PutMapping`

**Vấn đề:**
- `@PostMapping` dùng để **tạo tài nguyên mới** (CREATE)
- `@PutMapping` dùng để **cập nhật toàn bộ tài nguyên** (UPDATE)
- Violates REST API conventions

**HTTP Method đúng:**
- POST: Tạo mới resource
- PUT: Cập nhật toàn bộ resource
- PATCH: Cập nhật một phần resource

---

### ❌ LỖI 2: Kiểu dữ liệu `@PathVariable` là `String` thay vì `Long`

**Vấn đề:**
- URL parameter `productId` phải là `Long` (ID trong database là số)
- Nhận `String` sẽ gây lỗi parse
- Không phù hợp với quy tắc REST

**Sửa lỗi:**
```java
@PathVariable Long productId  // Long thay vì String
```

---

### ❌ LỖI 3: Không kiểm tra ID tồn tại & Thiếu error handling

**Vấn đề:**
- Nếu ID không tồn tại → không xử lý exception
- Không trả về status code phù hợp (404 Not Found)
- Trả về `Product` object trực tiếp không chuyên nghiệp

**Sửa lỗi:**
- Return `ResponseEntity<ApiResponse<Product>>`
- Status 200 OK khi thành công
- Status 404 Not Found khi ID không tồn tại

---

## PHẦN 2: TRIỂN KHAI ENDPOINTS

### File cấu trúc tạo ra:

```
src/main/java/com/example/ss04_bai4/
├── model/
│   └── Product.java               # Entity sản phẩm
├── dto/
│   └── ApiResponse.java           # Response wrapper
├── repository/
│   └── ProductRepository.java     # Database access
├── service/
│   └── ProductService.java        # Business logic
└── controller/
    └── ProductController.java     # API endpoints (GET, POST, PUT, DELETE)
```

---

### PUT Endpoint - Cập nhật sản phẩm ✅

```java
@PutMapping("/{productId}")
public ResponseEntity<ApiResponse<Product>> updateProduct(
        @PathVariable Long productId,
        @Valid @RequestBody Product productDetails) {
    
    Optional<Product> updatedProduct = productService.updateProduct(productId, productDetails);
    
    if (updatedProduct.isPresent()) {
        return ResponseEntity.ok(
            ApiResponse.<Product>builder()
                .success(true)
                .message("Cập nhật sản phẩm thành công")
                .data(updatedProduct.get())
                .build()
        );
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.<Product>builder()
                .success(false)
                .message("Sản phẩm với ID " + productId + " không tồn tại, không thể cập nhật")
                .data(null)
                .build()
            );
    }
}
```

**Ưu điểm:**
- ✓ Sử dụng `@PutMapping` (đúng HTTP method)
- ✓ Nhận `Long productId` từ URL
- ✓ Trả về `ResponseEntity<ApiResponse<Product>>`
- ✓ Kiểm tra ID tồn tại, trả về 404 nếu không
- ✓ Validation dữ liệu input với `@Valid`

---

### DELETE Endpoint - Xóa sản phẩm ✅

```java
@DeleteMapping("/{productId}")
public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long productId) {
    boolean isDeleted = productService.deleteProduct(productId);
    
    if (isDeleted) {
        return ResponseEntity.ok(
            ApiResponse.<Void>builder()
                .success(true)
                .message("Xóa sản phẩm ID " + productId + " thành công")
                .data(null)
                .build()
        );
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.<Void>builder()
                .success(false)
                .message("Sản phẩm với ID " + productId + " không tồn tại, không thể xóa")
                .data(null)
                .build()
            );
    }
}
```

**Ưu điểm:**
- ✓ Sử dụng `@DeleteMapping`
- ✓ Kiểm tra ID tồn tại, trả về 404 nếu không
- ✓ **KHÔNG** trả về body rỗng - có thông báo xác nhận rõ ràng
- ✓ Trả về JSON response có `success`, `message`

---

## TESTING VIA POSTMAN

### PUT - Cập nhật sản phẩm (Thành công)
```
PUT http://localhost:8080/api/v1/products/1
Content-Type: application/json

{
  "productName": "Sản phẩm mới",
  "price": 99.99,
  "description": "Mô tả",
  "quantity": 100,
  "category": "Electronics"
}

Response (200 OK):
{
  "success": true,
  "message": "Cập nhật sản phẩm thành công",
  "data": {
    "productId": 1,
    "productName": "Sản phẩm mới",
    "price": 99.99,
    "description": "Mô tả",
    "quantity": 100,
    "category": "Electronics"
  }
}
```

### PUT - ID không tồn tại
```
PUT http://localhost:8080/api/v1/products/999
Content-Type: application/json

{
  "productName": "Test"
}

Response (404 Not Found):
{
  "success": false,
  "message": "Sản phẩm với ID 999 không tồn tại, không thể cập nhật",
  "data": null
}
```

### DELETE - Xóa sản phẩm (Thành công)
```
DELETE http://localhost:8080/api/v1/products/1

Response (200 OK):
{
  "success": true,
  "message": "Xóa sản phẩm ID 1 thành công",
  "data": null
}
```

### DELETE - ID không tồn tại
```
DELETE http://localhost:8080/api/v1/products/999

Response (404 Not Found):
{
  "success": false,
  "message": "Sản phẩm với ID 999 không tồn tại, không thể xóa",
  "data": null
}
```

---

## HTTP Status Codes Sử Dụng

| Code | Ý nghĩa | Khi nào dùng |
|------|---------|------------|
| 200 | OK | PUT/GET thành công |
| 201 | Created | POST thành công |
| 404 | Not Found | ID sản phẩm không tồn tại |
| 400 | Bad Request | Validation error |

---

## BEST PRACTICES ÁP DỤNG

✅ **HTTP Methods Chính xác:**
- GET: Lấy dữ liệu
- POST: Tạo mới
- PUT: Cập nhật toàn bộ
- DELETE: Xóa

✅ **Proper Status Codes:**
- 200 OK: Thành công
- 404 Not Found: Resource không tồn tại
- Luôn trả về status code rõ ràng

✅ **Response Format Nhất Quán:**
- Luôn trả về JSON response có `success`, `message`, `data`
- KHÔNG trả về body rỗng

✅ **Validation:**
- Sử dụng `@Valid` trên request body
- Kiểm tra ID tồn tại trước khi update/delete
- Trả về lỗi phù hợp

✅ **URL Convention:**
- `/api/v1/products` - danh sách
- `/api/v1/products/{productId}` - chi tiết
- Không trộn POST/PUT cho cùng một resource

---

## TÓMNÓM

| Aspect         | Junior Dev (SAI)   | Chính xác (ĐÚNG)                                         |
|----------------|--------------------|----------------------------------------------------------|
| HTTP Method    | `@PostMapping`     | `@PutMapping` / `@DeleteMapping`                         |
| ID Type        | `String productId` | `Long productId`                                         |
| Response       | `Product` object   | `ResponseEntity<ApiResponse<Product>>`                   |
| Error Handling | Không              | 404 Not Found khi ID không tồn tại                       |
| Response Body  | Rỗng khi delete    | Thông báo xác nhận `{"success": true, "message": "..."}` |

