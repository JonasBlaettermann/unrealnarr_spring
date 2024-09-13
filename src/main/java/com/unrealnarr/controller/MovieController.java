package com.unrealnarr.controller;

import com.unrealnarr.entity.Movie;
import com.unrealnarr.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService service;

    @Operation(
            summary = "Fetch all movies",
            description = "Fetches a collection of all the movies in the database")
    @GetMapping("/")
    public ResponseEntity<Collection<Movie>> findMovies() throws Exception {

        Collection<Movie> movie = service.getMovies();
        if (movie != null) {
            return new ResponseEntity<>(movie, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /*
    @PostMapping("/upload")
    public ResponseEntity<String> uploadArtistData(@RequestParam("file") MultipartFile file) {
        try {
            List<Movie> movies = parseTsvFile(file);
            service.saveAll(movies);
            return ResponseEntity.ok("Movie data successfully uploaded and saved.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error processing file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving movie data: " + e.getMessage());
        }
    }

     */

    /*
    private List<Movie> parseTsvFile(MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            List<Movie> movies = new ArrayList<>();

            // Skip the header line
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] values = line.split("\t");

                if (values.length >= 13) { // Vergewissere dich, dass alle erforderlichen Spalten vorhanden sind
                    Movie movie = new Movie();
                    movie.setTconst(values[0]);
                    movie.setTitleType(values[1]);
                    movie.setGermanTitle(values[2]);
                    movie.setPrimaryTitle(values[3]);
                    movie.setOriginalTitle(values[4]);
                    movie.setStartYear(values[5]);
                    movie.setEndYear(values[6]);
                    movie.setRuntimeInMinutes(values[7]);
                    movie.setGenres(values[8]);
                    movie.setArtistInfo(values[9]);
                    movie.setImdbRating(values[10]);
                    movie.setRating(values[12]);
                    movie.setDate(values[13]);

                    movies.add(movie);
                }
            }
            yourService.saveMovies(movies);
        }
    }

    private List<RoleInMovie> parseRoleInMovieJson(String roleInMovieJson) {
        // Implement JSON parsing here (e.g., using Jackson or Gson)
        return new ArrayList<>();
    }

     */
}
