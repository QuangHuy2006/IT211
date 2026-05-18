package com.example.ss04_bai3;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // Tình huống A — Xem chi tiết phim theo ID: dùng @PathVariable
    @GetMapping("/{movieId}")
    public ResponseEntity<Movie> getMovieById(@PathVariable String movieId) {
        return movieService.findById(movieId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Tình huống B — Lọc theo thể loại: dùng @RequestParam
    // Ví dụ: GET /api/v1/movies?genre=Sci-Fi
    @GetMapping
    public List<Movie> getMovies(@RequestParam(name = "genre", required = false) String genre) {
        if (genre == null) {
            return movieService.findAll();
        }
        return movieService.findByGenre(genre);
    }
}

