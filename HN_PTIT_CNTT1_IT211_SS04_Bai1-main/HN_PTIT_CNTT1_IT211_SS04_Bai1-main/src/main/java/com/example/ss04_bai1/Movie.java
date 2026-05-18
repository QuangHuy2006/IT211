package com.example.ss04_bai1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    private String movieId;
    private String title;
    private String genre;
    private double rating;
}

