package com.example.ss04_bai5.service;

import com.example.ss04_bai5.dto.TripDTO;
import com.example.ss04_bai5.model.Trip;
import com.example.ss04_bai5.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripService {
    private final TripRepository tripRepository;

    /**
     * Lấy tất cả chuyến xe với phân trang
     */
    public Page<TripDTO> getAllTrips(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit); // page bắt đầu từ 1
        Page<Trip> trips = tripRepository.findAll(pageable);
        return trips.map(this::convertToDTO);
    }

    /**
     * Lấy chi tiết một chuyến xe theo ID
     */
    public TripDTO getTripById(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found with id: " + tripId));
        return convertToDTO(trip);
    }

    /**
     * Tìm chuyến xe theo tuyến đường
     */
    public List<TripDTO> searchTrips(String from, String to, LocalDateTime date) {
        List<Trip> trips = tripRepository.searchTrips(from, to, date);
        return trips.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Lấy chuyến xe từ repository (private, dùng nội bộ)
     */
    public Trip getTripEntityById(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found with id: " + tripId));
    }

    /**
     * Giảm số ghế trống khi đặt vé thành công
     */
    public void decreaseAvailableSeats(Long tripId) {
        Trip trip = getTripEntityById(tripId);
        if (trip.getAvailableSeats() > 0) {
            trip.setAvailableSeats(trip.getAvailableSeats() - 1);
            tripRepository.save(trip);
        } else {
            throw new RuntimeException("No available seats for trip: " + tripId);
        }
    }

    /**
     * Tăng số ghế trống khi hủy vé
     */
    public void increaseAvailableSeats(Long tripId) {
        Trip trip = getTripEntityById(tripId);
        if (trip.getAvailableSeats() < trip.getTotalSeats()) {
            trip.setAvailableSeats(trip.getAvailableSeats() + 1);
            tripRepository.save(trip);
        }
    }

    /**
     * Convert Trip entity to TripDTO
     */
    private TripDTO convertToDTO(Trip trip) {
        TripDTO dto = new TripDTO();
        dto.setTripId(trip.getTripId());
        dto.setFromLocation(trip.getFromLocation());
        dto.setToLocation(trip.getToLocation());
        dto.setDepartureTime(trip.getDepartureTime());
        dto.setArrivalTime(trip.getArrivalTime());
        dto.setPrice(trip.getPrice());
        dto.setTotalSeats(trip.getTotalSeats());
        dto.setAvailableSeats(trip.getAvailableSeats());
        dto.setBusType(trip.getBusType());
        dto.setCreatedAt(trip.getCreatedAt());
        return dto;
    }
}

