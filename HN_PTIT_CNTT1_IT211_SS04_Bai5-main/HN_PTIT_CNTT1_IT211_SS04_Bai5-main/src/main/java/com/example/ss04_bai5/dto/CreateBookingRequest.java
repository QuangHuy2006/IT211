package com.example.ss04_bai5.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingRequest {
    @JsonProperty("tripId")
    @NotBlank(message = "Trip ID must not be empty")
    private String tripId;

    @NotBlank(message = "Passenger name must not be empty")
    private String passengerName;

    @NotBlank(message = "Phone must not be empty")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Phone must be 10-11 digits")
    private String phone;

    @NotBlank(message = "Email must not be empty")
    @Email(message = "Email must be valid")
    private String email;
}

