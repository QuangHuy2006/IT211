4. Phân tích logic
   Các endpoint được thiết kế
   POST /api/genie/wishes/job

Dùng để ước tìm được công việc tốt hơn.

POST /api/genie/wishes/skill

Dùng để ước nâng cao kỹ năng.

POST /api/genie/wishes/advice

Dùng để nhận lời khuyên từ thần đèn.

GET /api/genie/wishes/history

Dùng để xem lịch sử các điều ước.

5. HTTP Status Code sử dụng
   Trường hợp	Status Code
   Điều ước thành công	200 OK
   Dữ liệu thiếu hoặc rỗng	400 Bad Request
   Điều ước bị cấm	400 Bad Request
   Hết 3 lượt ước	403 Forbidden
   Xem lịch sử thành công	200 OK
6. Test bằng Postman
   Điều ước hợp lệ
   POST http://localhost:8080/api/genie/wishes/job

Body:

{
"content": "Tôi muốn tìm được công việc Java Backend tốt hơn"
}

Response:

{
"message": "Thần đèn đã giúp bạn tìm được một công việc tốt hơn!",
"remainingWishes": 2
}
Điều ước bị cấm
POST http://localhost:8080/api/genie/wishes/job

Body:

{
"content": "Tôi muốn có thêm điều ước"
}

Response:

{
"message": "Điều ước không hợp lệ! Thần đèn không thể cho thêm điều ước hoặc làm bạn giàu ngay lập tức."
}

Status:

400 Bad Request
Thiếu dữ liệu

Body:

{
"content": ""
}

Response:

{
"message": "Nội dung điều ước không được để trống"
}

Status:

400 Bad Request
Hết lượt ước

Sau khi đã thực hiện đủ 3 điều ước hợp lệ, gửi tiếp request thứ 4:

{
"message": "Thần đèn đã hết lượt ước!"
}

Status:

403 Forbidden
Xem lịch sử
GET http://localhost:8080/api/genie/wishes/history

Response mẫu:

[
{
"wishType": "FIND_JOB",
"content": "Tôi muốn tìm được công việc Java Backend tốt hơn",
"status": "ACCEPTED",
"resultMessage": "Thần đèn đã giúp bạn tìm được một công việc tốt hơn!",
"createdAt": "2026-05-14T10:30:00"
}
]