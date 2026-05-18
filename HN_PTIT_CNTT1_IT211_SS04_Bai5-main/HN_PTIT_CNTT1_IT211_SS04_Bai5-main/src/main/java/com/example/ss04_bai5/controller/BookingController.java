package com.example.ss04_bai5.controller;

import com.example.ss04_bai5.dto.ApiResponse;
import com.example.ss04_bai5.dto.BookingDTO;
import com.example.ss04_bai5.dto.CreateBookingRequest;
import com.example.ss04_bai5.dto.UpdateBookingRequest;
import com.example.ss04_bai5.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    /**
     * POST /api/v1/bookings
     * Đặt vé mới cho một chuyến xe cụ thể
     * Request body: {tripId, passengerName, phone, email}
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BookingDTO>> createBooking(
            @Valid @RequestBody CreateBookingRequest request) {
        try {
            BookingDTO booking = bookingService.createBooking(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Booking created successfully", booking));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("No available seats")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to create booking: " + e.getMessage()));
        }
    }

    /**
     * DELETE /api/v1/bookings/{bookingId}
     * Hủy vé đã đặt
     * Response: 204 No Content (nếu thành công)
     */
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long bookingId) {
        try {
            bookingService.cancelBooking(bookingId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to cancel booking: " + e.getMessage()));
        }
    }

    /**
     * PUT /api/v1/bookings/{bookingId}
     * Cập nhật thông tin hành khách trên vé (họ tên, số điện thoại, email)
     */
    @PutMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<BookingDTO>> updateBooking(
            @PathVariable Long bookingId,
            @Valid @RequestBody UpdateBookingRequest request) {
        try {
            BookingDTO booking = bookingService.updateBooking(bookingId, request);
            return ResponseEntity.ok(
                    ApiResponse.ok("Booking updated successfully", booking)
            );
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update booking: " + e.getMessage()));
        }
    }

    /**
     * GET /api/v1/bookings/{bookingId}
     * Lấy thông tin vé (thêm endpoint này để tiện tra cứu)
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<BookingDTO>> getBookingById(@PathVariable Long bookingId) {
        try {
            BookingDTO booking = bookingService.getBookingById(bookingId);
            return ResponseEntity.ok(
                    ApiResponse.ok("Get booking details successfully", booking)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}

