package com.example.ss04_bai5.service;

import com.example.ss04_bai5.dto.BookingDTO;
import com.example.ss04_bai5.dto.CreateBookingRequest;
import com.example.ss04_bai5.dto.UpdateBookingRequest;
import com.example.ss04_bai5.model.Booking;
import com.example.ss04_bai5.model.Trip;
import com.example.ss04_bai5.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final TripService tripService;

    /**
     * Đặt vé mới cho một chuyến xe
     * Giảm số ghế trống, tạo booking mới
     */
    @Transactional
    public BookingDTO createBooking(CreateBookingRequest request) {
        Long tripId = Long.parseLong(request.getTripId());

        // Kiểm tra chuyến xe có tồn tại không và có ghế trống không
        Trip trip = tripService.getTripEntityById(tripId);
        if (trip.getAvailableSeats() <= 0) {
            throw new RuntimeException("No available seats for this trip");
        }

        // Tạo vé mới
        Booking booking = new Booking();
        booking.setTrip(trip);
        booking.setPassengerName(request.getPassengerName());
        booking.setPhone(request.getPhone());
        booking.setEmail(request.getEmail());
        booking.setPrice(trip.getPrice());
        booking.setStatus(Booking.BookingStatus.CONFIRMED);

        // Gán số ghế tự động
        booking.setSeatNumber(generateSeatNumber(tripId));

        // Lưu booking
        booking = bookingRepository.save(booking);

        // Giảm số ghế trống của chuyến xe
        tripService.decreaseAvailableSeats(tripId);

        return convertToDTO(booking);
    }

    /**
     * Hủy vé đã đặt
     * Tăng số ghế trống lên
     */
    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking already cancelled");
        }

        // Đánh dấu vé là hủy
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        booking.setCancellationDate(LocalDateTime.now());
        bookingRepository.save(booking);

        // Tăng số ghế trống của chuyến xe
        tripService.increaseAvailableSeats(booking.getTrip().getTripId());
    }

    /**
     * Cập nhật thông tin hành khách trên vé
     */
    @Transactional
    public BookingDTO updateBooking(Long bookingId, UpdateBookingRequest request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new RuntimeException("Cannot update cancelled booking");
        }

        booking.setPassengerName(request.getPassengerName());
        booking.setPhone(request.getPhone());
        booking.setEmail(request.getEmail());

        booking = bookingRepository.save(booking);
        return convertToDTO(booking);
    }

    /**
     * Lấy thông tin vé theo ID
     */
    public BookingDTO getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));
        return convertToDTO(booking);
    }

    /**
     * Tạo số ghế tự động (ví dụ: A1, A2, B1, B2, ...)
     */
    private String generateSeatNumber(Long tripId) {
        java.util.List<Booking> bookings = bookingRepository.findActiveBookingsByTrip(tripId);
        int seatCount = bookings.size() + 1;

        char row = (char) ('A' + (seatCount - 1) / 10);
        int col = ((seatCount - 1) % 10) + 1;

        return "" + row + col;
    }

    /**
     * Convert Booking entity to BookingDTO
     */
    private BookingDTO convertToDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setTripId(booking.getTrip().getTripId());
        dto.setPassengerName(booking.getPassengerName());
        dto.setPhone(booking.getPhone());
        dto.setEmail(booking.getEmail());
        dto.setSeatNumber(booking.getSeatNumber());
        dto.setPrice(booking.getPrice());
        dto.setStatus(booking.getStatus().toString());
        dto.setBookingDate(booking.getBookingDate());
        dto.setCancellationDate(booking.getCancellationDate());
        return dto;
    }
}

