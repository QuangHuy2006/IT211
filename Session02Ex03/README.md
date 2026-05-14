4. Phân tích logic
   Vai trò của HTTP Status Code

HTTP Status Code giúp client biết kết quả xử lý request là gì.

Ví dụ:

200 OK

Dùng khi lấy dữ liệu hoặc cập nhật thành công.

201 Created

Dùng khi tạo mới dữ liệu thành công.

204 No Content

Dùng khi xóa thành công và không cần trả về body.

404 Not Found

Dùng khi tài nguyên không tồn tại.

Nếu API trả về null thay vì 404 Not Found, client sẽ khó biết là:

dữ liệu thật sự không tồn tại,
server bị lỗi,
response bị sai định dạng,
hay API xử lý thiếu logic.

Vì vậy, dùng ResponseEntity giúp kiểm soát rõ cả body và status code.

5. Vì sao cần Jackson Dataformat XML?

Mặc định Spring Boot Web hỗ trợ JSON rất tốt thông qua Jackson.

Nhưng nếu muốn API trả về XML khi client gửi:

Accept: application/xml

thì cần cài thêm thư viện:

implementation 'com.fasterxml.jackson.dataformat:jackson-dataformat-xml'

Thư viện này giúp Spring Boot chuyển object Java thành XML.

Annotation:

@JacksonXmlRootElement(localName = "item")

giúp định nghĩa tên thẻ gốc trong XML.

6. Test API bằng Postman
   6.1. GET tất cả items
   GET http://localhost:8080/api/items

Header JSON:

Accept: application/json

Kết quả:

[
{
"id": 1,
"name": "Gạo ST25",
"price": 180000.0,
"quantity": 50
}
]
6.2. GET item theo ID
GET http://localhost:8080/api/items/1

Nếu có dữ liệu:

200 OK

Nếu không tồn tại:

404 Not Found
6.3. POST tạo mới item
POST http://localhost:8080/api/items

Header:

Content-Type: application/json
Accept: application/json

Body:

{
"name": "Đường trắng",
"price": 25000,
"quantity": 80
}

Status trả về:

201 Created
6.4. PUT cập nhật item
PUT http://localhost:8080/api/items/1

Body:

{
"name": "Gạo ST25 loại đặc biệt",
"price": 200000,
"quantity": 40
}

Nếu ID tồn tại:

200 OK

Nếu ID không tồn tại:

404 Not Found
6.5. DELETE item
DELETE http://localhost:8080/api/items/1

Nếu xóa thành công:

204 No Content

Nếu ID không tồn tại:

404 Not Found
7. Test trả về XML

Trong Postman, gửi request:

GET http://localhost:8080/api/items/1

Header:

Accept: application/xml

Kết quả XML:

<item>
    <id>1</id>
    <name>Gạo ST25</name>
    <price>180000.0</price>
    <quantity>50</quantity>
</item>

Nếu muốn JSON:

Accept: application/json