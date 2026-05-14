package com.session02ex05.controller;

import com.session02ex05.dto.WishRequest;
import com.session02ex05.dto.WishResponse;
import com.session02ex05.model.WishHistory;
import com.session02ex05.service.GenieService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genie")
public class GenieController {

    private final GenieService genieService;

    public GenieController(GenieService genieService) {
        this.genieService = genieService;
    }

    // Điều ước 1: Tìm việc tốt hơn
    @PostMapping("/wishes/job")
    public ResponseEntity<WishResponse> wishForJob(@Valid @RequestBody WishRequest request) {
        return ResponseEntity.ok(genieService.wishForJob(request.getContent()));
    }

    // Điều ước 2: Nâng cao kỹ năng
    @PostMapping("/wishes/skill")
    public ResponseEntity<WishResponse> wishForSkill(@Valid @RequestBody WishRequest request) {
        return ResponseEntity.ok(genieService.wishForSkill(request.getContent()));
    }

    // Điều ước 3: Nhận lời khuyên
    @PostMapping("/wishes/advice")
    public ResponseEntity<WishResponse> wishForAdvice(@Valid @RequestBody WishRequest request) {
        return ResponseEntity.ok(genieService.wishForAdvice(request.getContent()));
    }

    // Xem lịch sử điều ước
    @GetMapping("/wishes/history")
    public ResponseEntity<List<WishHistory>> getWishHistories() {
        return ResponseEntity.ok(genieService.getHistories());
    }
}