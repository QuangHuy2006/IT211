package com.example.ss04_bai5.repository;

import com.example.ss04_bai5.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Tìm tất cả vé nào sẽ bị hủy cho một chuyến xe
     */
    @Query("SELECT b FROM Booking b WHERE b.trip.tripId = :tripId AND b.status = 'CONFIRMED'")
    List<Booking> findConfirmedBookingsByTrip(@Param("tripId") Long tripId);

    /**
     * Kiểm tra xem một chuyến xe có vé của hành khách này hay không
     */
    @Query("SELECT b FROM Booking b WHERE b.trip.tripId = :tripId AND b.phone = :phone AND b.status = 'CONFIRMED'")
    List<Booking> findBookingsByTripAndPhone(@Param("tripId") Long tripId, @Param("phone") String phone);

    /**
     * Tìm vé theo email hành khách
     */
    List<Booking> findByEmail(String email);

    /**
     * Tìm tất cả vé chưa hủy của một chuyến xe
     */
    @Query("SELECT b FROM Booking b WHERE b.trip.tripId = :tripId AND b.status = 'CONFIRMED'")
    List<Booking> findActiveBookingsByTrip(@Param("tripId") Long tripId);
}

