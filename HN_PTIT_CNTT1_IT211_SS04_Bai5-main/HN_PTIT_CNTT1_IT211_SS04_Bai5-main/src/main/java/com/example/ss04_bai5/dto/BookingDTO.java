package com.example.ss04_bai5.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long bookingId;
    private Long tripId;
    private String passengerName;
    private String phone;
    private String email;
    private String seatNumber;
    private Double price;
    private String status;
    private LocalDateTime bookingDate;
    private LocalDateTime cancellationDate;
}

