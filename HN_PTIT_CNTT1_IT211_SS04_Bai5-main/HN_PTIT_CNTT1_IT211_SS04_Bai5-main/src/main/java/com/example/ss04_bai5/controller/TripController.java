package com.example.ss04_bai5.controller;

import com.example.ss04_bai5.dto.ApiResponse;
import com.example.ss04_bai5.dto.TripDTO;
import com.example.ss04_bai5.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/trips")
@RequiredArgsConstructor
public class TripController {
    private final TripService tripService;

    /**
     * GET /api/v1/trips?page=1&limit=10
     * Lấy danh sách tất cả chuyến xe với phân trang
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<TripDTO>>> getAllTrips(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            Page<TripDTO> trips = tripService.getAllTrips(page, limit);
            return ResponseEntity.ok(
                    ApiResponse.ok("Get all trips successfully", trips)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to get trips: " + e.getMessage()));
        }
    }

    /**
     * GET /api/v1/trips/{tripId}
     * Lấy thông tin chi tiết một chuyến xe theo ID
     */
    @GetMapping("/{tripId}")
    public ResponseEntity<ApiResponse<TripDTO>> getTripById(@PathVariable Long tripId) {
        try {
            TripDTO trip = tripService.getTripById(tripId);
            return ResponseEntity.ok(
                    ApiResponse.ok("Get trip details successfully", trip)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * GET /api/v1/trips/search?from=Hà Nội&to=TP.HCM&date=2026-05-20
     * Tìm chuyến xe theo tuyến đường
     * Note: @GetMapping("/search") phải đặt trước "{tripId}" để tránh nhầm lẫn routing
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TripDTO>>> searchTrips(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        try {
            if (from == null || from.isEmpty() || to == null || to.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Missing required parameters: from, to, date"));
            }

            List<TripDTO> trips = tripService.searchTrips(from, to, date);
            return ResponseEntity.ok(
                    ApiResponse.ok("Search trips successfully", trips)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to search trips: " + e.getMessage()));
        }
    }
}

