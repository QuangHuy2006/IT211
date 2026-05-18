package com.example.ss04_bai3;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Movie {
    private String movieId;
    private String title;
    private String genre;
    private int year;


}

