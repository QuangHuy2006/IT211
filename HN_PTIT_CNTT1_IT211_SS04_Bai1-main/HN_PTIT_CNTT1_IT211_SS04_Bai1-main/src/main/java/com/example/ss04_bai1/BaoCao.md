====== GIẢI THÍCH CHI TIẾT BÀI TẬP ======

## PHẦN 1: PHÂN TÍCH LOGIC - TẠI SAO LỖI?

### 🔴 Vấn đề:
Client nhận về chuỗi ký tự lạ: [Movie@3a1b2c3d, Movie@4f2e1a0b]
Thay vì JSON: [{"movieId":"M001","title":"Inception",...}]

### 🔍 Nguyên nhân gốc rễ:

1. **Dòng lỗi trong code gốc:**
   ```java
   public String getMovies() {
       List<Movie> movies = new ArrayList<>();
       movies.add(new Movie("M001", "Inception", "Sci-Fi", 8.8));
       movies.add(new Movie("M002", "Parasite", "Drama", 8.6));
       return movies.toString();  // ← LỖI Ở ĐÂY
   }
   ```

2. **Tại sao xảy ra?**
   - `movies.toString()` gọi method `toString()` của List
   - List gọi `toString()` trên từng phần tử (Movie object)
   - Movie class **KHÔNG override** `toString()` method
   - Nên nó sử dụng implementation mặc định từ Object class
   - Object.toString() trả về format: `ClassName@HashCode`
   - Kết quả: `[Movie@3a1b2c3d, Movie@4f2e1a0b]`

3. **Vấn đề thứ hai:**
   - Return type là `String` thay vì `List<Movie>`
   - Spring Boot không biết phải serialize thành JSON
   - Chỉ trả về raw string text

### 💡 Giải pháp:

#### 1. Thay đổi return type từ String → List<Movie>
```java
// ❌ Sai
public String getMovies() { ... }

// ✅ Đúng
public List<Movie> getMovies() { ... }
```

#### 2. Return danh sách trực tiếp (không dùng toString())
```java
// ❌ Sai
return movies.toString();

// ✅ Đúng
return movies;
```

#### 3. Spring Boot tự động serialize
- Khi return type là `List<Movie>` (Java object)
- Spring Boot sẽ tự động convert thành JSON nhờ Jackson library
- Điều này chỉ xảy ra vì:
  - Sử dụng `@RestController` (không phải @Controller)
  - Có getters/setters trong Movie class
  - Content-Type được set là application/json

#### 4. Thêm @RequestMapping cấp controller
```java
@RestController
@RequestMapping("/api/v1")  // ← Thêm dòng này
public class MovieController {
    
    @GetMapping("/movies")   // Endpoint đầy đủ: /api/v1/movies
    public List<Movie> getMovies() { ... }
}
```

---

## PHẦN 2: KẾT QUẢ CUỐI CÙNG

### File: MovieController.java
✅ Điểm cải tiến:

1. ✅ Return type: `List<Movie>` (không phải String)
2. ✅ Return: `return movies;` (không phải movies.toString())
3. ✅ Thêm @RequestMapping("/api/v1") cấp class
4. ✅ Endpoint đầy đủ: GET /api/v1/movies
5. ✅ Thêm đầy đủ getters/setters cho Movie class
6. ✅ Spring tự động serialize List → JSON

### JSON Response (sample):
```json
[
  {
    "movieId": "M001",
    "title": "Inception",
    "genre": "Sci-Fi",
    "rating": 8.8
  },
  {
    "movieId": "M002",
    "title": "Parasite",
    "genre": "Drama",
    "rating": 8.6
  }
]
```

### Cách test:
1. Chạy ứng dụng Spring Boot
2. Gọi GET http://localhost:8080/api/v1/movies
3. Kết quả sẽ là JSON hợp lệ ✅

---

## KIẾN THỨC MỀM:

### @RestController vs @Controller
- `@RestController` = `@Controller` + `@ResponseBody`
- Tự động serialize return value thành JSON/XML
- `@Controller` chỉ return view name, cần thêm @ResponseBody nếu muốn JSON

### Jackson Library
- Spring Boot tự động cấu hình Jackson
- Chuyển đổi Java Object ↔ JSON
- Dùng getter/setter để xác định properties serialize

### HTTP GET Convention
- Không thay đổi dữ liệu
- Pass parameters qua URL query string
- Safe operation (có thể cache 200 lần)

---

## SO SÁNH CODE:

❌ CODE CŨ (LỖI):
```java
@RestController
public class MovieController {
    @GetMapping("/movies")
    public String getMovies() {  // Return type: String ❌
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("M001", "Inception", "Sci-Fi", 8.8));
        movies.add(new Movie("M002", "Parasite", "Drama", 8.6));
        return movies.toString();  // Trả về [Movie@3a1b2c3d, ...] ❌
    }
}
```

✅ CODE MỚI (ĐÚNG):
```java
@RestController
@RequestMapping("/api/v1")  // ✅ Endpoint chuẩn tắc
public class MovieController {
    @GetMapping("/movies")  // Full URL: /api/v1/movies
    public List<Movie> getMovies() {  // Return type: List<Movie> ✅
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("M001", "Inception", "Sci-Fi", 8.8));
        movies.add(new Movie("M002", "Parasite", "Drama", 8.6));
        return movies;  // Trả về JSON hợp lệ ✅
    }
}
```

====== HOÀN THÀNH ======

