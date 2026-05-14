1. Phân tích lỗi logic

Trong code ban đầu:

@GetMapping("/hot-products")
public String getHotProducts() {
List<Product> products = new ArrayList<>();
products.add(new Product("HP001", "Áo thun 'Code is Life'", 199.000));
products.add(new Product("HP002", "Móc khóa 'Bug Free'", 99.000));

    return products.toString();
}

Lỗi nằm ở dòng:

return products.toString();

products.toString() không trả về JSON hợp lệ. Nó chỉ trả về một chuỗi String mô tả object trong Java, ví dụ dạng:

[com.example.Product@1a2b3c, com.example.Product@4d5e6f]

Hoặc nếu có override toString() thì cũng chỉ là text thường, không phải JSON chuẩn.

Trong khi đó, client như frontend, mobile hoặc Postman thường cần dữ liệu JSON dạng:

[
{
"id": "HP001",
"name": "Áo thun 'Code is Life'",
"price": 199.0
},
{
"id": "HP002",
"name": "Móc khóa 'Bug Free'",
"price": 99.0
}
]

Nguyên nhân gốc rễ là: phương thức đang trả về String thay vì trả về trực tiếp List<Product>.
Với @RestController, Spring Boot có thể tự động chuyển object Java sang JSON bằng Jackson, miễn là ta trả về object/list thay vì String.