package com.unrealnarr.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unrealnarr.dto.MovieDTO;
import com.unrealnarr.entity.Artist;
import com.unrealnarr.entity.Movie;
import com.unrealnarr.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    private MovieService service;

    @Operation(
            summary = "Fetch all movies",
            description = "Fetches a collection of all the movies in the database")
    @GetMapping("/")
    public ResponseEntity<Collection<Movie>> findMovies() throws Exception {
        try {
            Collection<Movie> movies = service.getMovies();
            if (movies != null && !movies.isEmpty()) {
                return new ResponseEntity<>(movies, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Fetch a single movie",
            description = "Fetches a single movie via the tconst")
    @GetMapping("/{id}")
    public ResponseEntity<Movie> findById(@PathVariable String id) throws Exception {
        try {
            logger.info(id);
            Movie movie = service.findMovieByTconst(id);
            if (movie != null) {
                return new ResponseEntity<>(movie, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Fetch a list of movies",
            description = "Fetches a collection of all the movies in the database in a more comprehensive and searchable list")
    @GetMapping("/list")
    public ResponseEntity<Collection<MovieDTO>> getMovieList() throws Exception {
        try {
            Collection<MovieDTO> movies = service.getMovieList();
            if (movies != null && !movies.isEmpty()) {
                return new ResponseEntity<>(movies, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Upload dataset",
            description = "Takes a .TSV file and prepares it to be uploaded to the MongoDB")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadMovieData(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file uploaded");
        }
        try {
            logger.info(file.toString());
            List<Movie> movies = parseTsvFile(file);
            logger.info(movies.toString());
            service.saveAll(movies);
            return ResponseEntity.ok("Movie data successfully uploaded and saved.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error processing file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving movie data: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Update dataset",
            description = "Takes a .TSV file and prepares it to be uploaded to the MongoDB and update all the necessary entries and create new entries for new movies")
    @PutMapping("/upload")
    public ResponseEntity<String> updateMovies(@RequestParam("file") MultipartFile file) {
        try {
            List<Movie> movies = parseTsvFile(file);
            logger.info(movies.toString());
            service.updateMovies(movies);
            return ResponseEntity.ok("Movies updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // This function is to parse the TSV file, so it will spit out a useful JSON. The roleInMovie will be parsed in a seperate function.
    private List<Movie> parseTsvFile(MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            List<Movie> movies = new ArrayList<>();
            reader.readLine();
            while ((line = reader.readLine()) != null) {

                String[] fields = line.split("\t");
                if (fields.length < 14) {
                    // Skip invalid lines
                    continue;
                }
                Movie movie = new Movie();
                movie.setTconst(fields[0]);
                movie.setTitleType(fields[1]);
                movie.setGermanTitle(fields[2]);
                movie.setPrimaryTitle(fields[3]);
                movie.setOriginalTitle(fields[4]);

                try {
                    movie.setStartYear(Integer.parseInt(fields[5]));
                } catch (NumberFormatException e) {
                    movie.setStartYear(0);
                }
                try {
                    movie.setEndYear(Integer.parseInt(fields[6]));
                } catch (NumberFormatException e) {
                    movie.setEndYear(0);
                }
                try {
                    movie.setRuntimeInMinutes(Integer.parseInt(fields[7]));
                } catch (NumberFormatException e) {
                    movie.setRuntimeInMinutes(0);
                }

                movie.setGenres(fields[8]);

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                try {
                    List<Artist> artistInfo = parseArtistInfo(fields[9]);
                    movie.setArtistInfo(artistInfo);
                } catch (Exception e) {
                    movie.setArtistInfo(new ArrayList<>());
                }

                try {
                    movie.setIMDBRating(Float.parseFloat(fields[10]));
                } catch (NumberFormatException e) {
                    movie.setIMDBRating(0); // or some default value
                }
                try {
                    movie.setRating(Float.parseFloat(fields[11]));
                } catch (NumberFormatException e) {
                    movie.setRating(0); // or some default value
                }
                try {
                    String critic = parseCriticJson(fields[12]);
                    movie.setCritics(critic);
                } catch (Exception e) {
                    movie.setCritics("");
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    movie.setDate(dateFormat.parse(fields[13]));
                } catch (ParseException e) {
                    movie.setDate(new Date()); // or some default value
                }

                movies.add(movie);
            }
            return movies;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Takes a String of the artist Info and cleanes it up, including outliers in characters, job and primaryName.
    private List<Artist> parseArtistInfo(String artistInfoStr) {
        try {
            String cleanedStr = artistInfoStr
                    .replaceAll("\\\\", "")
                    .replace("\"\"", "\"")
                    .replaceAll("'", "\"")
                    .replaceAll("\"\"Self\"\"", "\"Self\"")
                    .replaceAll("\\\\\"", "\"")
                    .replace("\"[", "[")
                    .replace("]\"", "]");

            cleanedStr = cleanField(cleanedStr, "characters");
            cleanedStr = cleanField(cleanedStr, "job");
            cleanedStr = cleanField(cleanedStr, "primaryName");

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            return objectMapper.readValue(cleanedStr, new TypeReference<List<Map<String, Object>>>() {
                    })
                    .stream()
                    .map(this::mapArtist)
                    .collect(Collectors.toList());

        } catch (JsonProcessingException e) {
            logger.error("Error parsing artistInfo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // This function will clean a field of " and unnecessary spaces
    private String cleanField(String json, String field) {
        Pattern pattern;
        if (field.equals("characters")) {
            pattern = Pattern.compile("\"characters\"\\s*:\\s*\\[\\s*\"(.*?)\"\\s*\\]");
        } else {
            pattern = Pattern.compile("\"" + field + "\":\\s*\"(.*?)\"(?=[,}\\]])");
        }
        Matcher matcher = pattern.matcher(json);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String fieldContent = matcher.group(1);

            String fixedContent = fieldContent
                    .replace("\"", "\\'")
                    .replace("\\\"", "\"");

            matcher.appendReplacement(sb, "\"" + field + "\": \"" + fixedContent + "\"");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    // A function to map the Artist
    private Artist mapArtist(Map<String, Object> artistMap) {
        Artist artist = new Artist();
        artist.setNconst((String) artistMap.get("nconst"));
        artist.setPrimaryName((String) artistMap.get("primaryName"));

        Map<String, Object> roleMap = (Map<String, Object>) artistMap.get("role");
        if (roleMap != null) {
            Artist.RoleInMovie roleInMovie = new Artist.RoleInMovie();
            roleInMovie.setTconst((String) roleMap.get("tconst"));
            roleInMovie.setOrdering((int) roleMap.get("ordering"));
            roleInMovie.setCategory((String) roleMap.get("category"));
            roleInMovie.setJob((String) roleMap.getOrDefault("job", null));
            roleInMovie.setCharacters((String) roleMap.get("characters"));

            List<Artist.RoleInMovie> roles = new ArrayList<>();
            roles.add(roleInMovie);
            artist.setRolesInMovies(roles);
        }

        return artist;
    }

    private String parseCriticJson(String critic) {
        List<String> stringList = Arrays.asList(critic);
        return String.join("", stringList);
    }

    // Removes the Segment Content, but I don't know if I really need this.
    private String removeSegmentContent(String fieldContent) {
        Pattern segmentPattern = Pattern.compile("\\(segment[^)]*\\)");
        Matcher segmentMatcher = segmentPattern.matcher(fieldContent);

        String cleanedContent = segmentMatcher.replaceAll("");

        cleanedContent = cleanedContent.replace("\"\"", "\"");
        cleanedContent = cleanedContent.replaceAll("\\s*,\\s*", ",");
        cleanedContent = cleanedContent.replaceAll("^,|,$", "");

        return cleanedContent;
    }

}