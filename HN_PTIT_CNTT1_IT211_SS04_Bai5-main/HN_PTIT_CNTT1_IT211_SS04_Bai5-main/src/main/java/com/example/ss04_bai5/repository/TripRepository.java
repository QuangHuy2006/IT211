package com.example.ss04_bai5.repository;

import com.example.ss04_bai5.model.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    /**
     * Tìm chuyến xe theo tuyến đường, ngày khởi hành và có ghế trống
     */
    @Query("SELECT t FROM Trip t WHERE " +
           "LOWER(t.fromLocation) = LOWER(:from) AND " +
           "LOWER(t.toLocation) = LOWER(:to) AND " +
           "DATE(t.departureTime) = DATE(:date) AND " +
           "t.availableSeats > 0 " +
           "ORDER BY t.departureTime")
    List<Trip> searchTrips(
            @Param("from") String from,
            @Param("to") String to,
            @Param("date") LocalDateTime date
    );

    /**
     * Lấy tất cả chuyến xe với phân trang
     */
    Page<Trip> findAll(Pageable pageable);

    /**
     * Tìm chuyến xe từ địa điểm bắt đầu
     */
    List<Trip> findByFromLocation(String fromLocation);

    /**
     * Tìm chuyến xe đến địa điểm kết thúc
     */
    List<Trip> findByToLocation(String toLocation);
}

