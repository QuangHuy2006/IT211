package com.example.ss04_bai3;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final List<Movie> movies = new ArrayList<>(Arrays.asList(
            new Movie("M001", "Interstellar", "Sci-Fi", 2014),
            new Movie("M002", "Inception", "Sci-Fi", 2010),
            new Movie("M003", "The Godfather", "Crime", 1972),
            new Movie("M004", "The Shawshank Redemption", "Drama", 1994)
    ));

    public Optional<Movie> findById(String movieId) {
        return movies.stream()
                .filter(m -> m.getMovieId().equalsIgnoreCase(movieId))
                .findFirst();
    }

    public List<Movie> findAll() {
        return new ArrayList<>(movies);
    }

    public List<Movie> findByGenre(String genre) {
        if (genre == null || genre.isBlank()) return findAll();
        String normalized = genre.trim();
        return movies.stream()
                .filter(m -> m.getGenre().equalsIgnoreCase(normalized))
                .collect(Collectors.toList());
    }
}

