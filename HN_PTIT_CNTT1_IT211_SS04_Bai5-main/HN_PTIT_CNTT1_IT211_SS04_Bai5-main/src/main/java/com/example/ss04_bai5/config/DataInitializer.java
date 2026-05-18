package com.example.ss04_bai5.config;

import com.example.ss04_bai5.model.Trip;
import com.example.ss04_bai5.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeData(TripRepository tripRepository) {
        return args -> {
            // Chỉ tạo sample data nếu database trống
            if (tripRepository.count() == 0) {
                // Sample Trip 1: Hà Nội -> TP.HCM
                Trip trip1 = new Trip();
                trip1.setFromLocation("Hà Nội");
                trip1.setToLocation("TP.HCM");
                trip1.setDepartureTime(LocalDateTime.of(2026, 5, 20, 23, 0, 0));
                trip1.setArrivalTime(LocalDateTime.of(2026, 5, 21, 10, 30, 0));
                trip1.setPrice(350000.0);
                trip1.setTotalSeats(40);
                trip1.setAvailableSeats(40);
                trip1.setBusType("Standard");
                tripRepository.save(trip1);

                // Sample Trip 2: Hà Nội -> Hải Phòng
                Trip trip2 = new Trip();
                trip2.setFromLocation("Hà Nội");
                trip2.setToLocation("Hải Phòng");
                trip2.setDepartureTime(LocalDateTime.of(2026, 5, 20, 14, 0, 0));
                trip2.setArrivalTime(LocalDateTime.of(2026, 5, 20, 17, 30, 0));
                trip2.setPrice(150000.0);
                trip2.setTotalSeats(50);
                trip2.setAvailableSeats(50);
                trip2.setBusType("VIP");
                tripRepository.save(trip2);

                // Sample Trip 3: TP.HCM -> Nha Trang
                Trip trip3 = new Trip();
                trip3.setFromLocation("TP.HCM");
                trip3.setToLocation("Nha Trang");
                trip3.setDepartureTime(LocalDateTime.of(2026, 5, 20, 22, 0, 0));
                trip3.setArrivalTime(LocalDateTime.of(2026, 5, 21, 4, 30, 0));
                trip3.setPrice(200000.0);
                trip3.setTotalSeats(30);
                trip3.setAvailableSeats(30);
                trip3.setBusType("Sleeper");
                tripRepository.save(trip3);

                // Sample Trip 4: TP.HCM -> Cần Thơ
                Trip trip4 = new Trip();
                trip4.setFromLocation("TP.HCM");
                trip4.setToLocation("Cần Thơ");
                trip4.setDepartureTime(LocalDateTime.of(2026, 5, 20, 6, 0, 0));
                trip4.setArrivalTime(LocalDateTime.of(2026, 5, 20, 9, 0, 0));
                trip4.setPrice(100000.0);
                trip4.setTotalSeats(45);
                trip4.setAvailableSeats(45);
                trip4.setBusType("Standard");
                tripRepository.save(trip4);

                // Sample Trip 5: Hà Nội -> Đà Lạt
                Trip trip5 = new Trip();
                trip5.setFromLocation("Hà Nội");
                trip5.setToLocation("Đà Lạt");
                trip5.setDepartureTime(LocalDateTime.of(2026, 5, 21, 20, 0, 0));
                trip5.setArrivalTime(LocalDateTime.of(2026, 5, 22, 8, 0, 0));
                trip5.setPrice(300000.0);
                trip5.setTotalSeats(40);
                trip5.setAvailableSeats(40);
                trip5.setBusType("VIP");
                tripRepository.save(trip5);

                System.out.println("✅ Sample data initialized successfully!");
            }
        };
    }
}

