package com.example.ss04_bai1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class MovieController {


    @GetMapping("/movies")
    public List<Movie> getMovies() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("M001", "Inception", "Sci-Fi", 8.8));
        movies.add(new Movie("M002", "Parasite", "Drama", 8.6));
        return movies;
    }
}

