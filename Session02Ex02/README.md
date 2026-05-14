Sự khác nhau giữa POST và PUT

POST dùng để tạo mới tài nguyên.

Ví dụ:

POST /api/customers

POST thường không idempotent. Nghĩa là nếu gửi cùng một request nhiều lần, có thể tạo ra nhiều bản ghi khác nhau.

Ví dụ gửi 2 lần:

{
"name": "Nguyễn Văn A",
"email": "a@gmail.com"
}

Có thể tạo ra:

{
"id": 1,
"name": "Nguyễn Văn A",
"email": "a@gmail.com"
}

và:

{
"id": 2,
"name": "Nguyễn Văn A",
"email": "a@gmail.com"
}

PUT dùng để cập nhật hoặc thay thế một tài nguyên cụ thể.

Ví dụ:

PUT /api/customers/1

PUT có tính idempotent. Nghĩa là nếu gửi cùng một request nhiều lần thì kết quả cuối cùng vẫn như nhau, không tạo thêm bản ghi mới.

5. Vì sao code cũ bị lỗi?

Code cũ dùng:

@PostMapping
public ResponseEntity<Customer> createOrUpdateCustomer(@RequestBody Customer customer)

Tức là dùng POST cho cả tạo mới và cập nhật.

Lỗi nghiêm trọng nằm ở đoạn:

if (existingCustomer.isPresent()) {
// cập nhật
} else {
customers.add(customer);
return new ResponseEntity<>(customer, HttpStatus.CREATED);
}

Nếu client gửi ID nhưng ID đó không tồn tại, hệ thống lại tạo mới khách hàng. Điều này sai nghiệp vụ vì thao tác cập nhật không được phép tự tạo bản ghi mới.

Khi người dùng bấm cập nhật nhiều lần hoặc gửi nhầm ID, hệ thống có thể tạo dữ liệu trùng lặp.

6. API đúng sau khi sửa
   Tạo mới khách hàng
   POST http://localhost:8080/api/customers

Body:

{
"name": "Nguyễn Văn A",
"email": "vana@gmail.com"
}

Response:

{
"id": 1,
"name": "Nguyễn Văn A",
"email": "vana@gmail.com"
}

Status:

201 Created
Cập nhật khách hàng
PUT http://localhost:8080/api/customers/1

Body:

{
"name": "Nguyễn Văn A Updated",
"email": "updated@gmail.com"
}

Response:

{
"id": 1,
"name": "Nguyễn Văn A Updated",
"email": "updated@gmail.com"
}

Status:

200 OK
Nếu khách hàng không tồn tại
PUT http://localhost:8080/api/customers/999

Response không có body.

Status:

404 Not Found