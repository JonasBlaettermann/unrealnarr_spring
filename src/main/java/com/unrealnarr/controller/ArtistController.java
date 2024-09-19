package com.unrealnarr.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unrealnarr.entity.Artist;
import com.unrealnarr.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    private static final Logger logger = LoggerFactory.getLogger(ArtistController.class);

    @Autowired
    private ArtistService service;

    @Operation(
            summary = "Fetch all artists",
            description = "Fetches a collection of all the artists in the database. With nconst as a primary key, a primaryName and roleInMovie with a tconst of the movie, the ordering number, the category and job and characters name")
    @GetMapping("/")
    public ResponseEntity<Collection<Artist>> findArtists() throws Exception {
        try {
            Collection<Artist> artists = service.getArtists();
            if (artists != null && !artists.isEmpty()) {
                return new ResponseEntity<>(artists, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Fetch a single artist",
            description = "Fetches a artist via nconst, with primaryName and roleInMovie with with a tconst of the movie, the ordering number, the category and job and characters name")
    @GetMapping("/{id}")
    public ResponseEntity<Artist> findById(@PathVariable String id) throws Exception {
        try {
            logger.info(id);
            Artist artist = service.findByNconst(id);
            if (artist != null) {
                return new ResponseEntity<>(artist, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Upload artists dataset",
            description = "Takes a .TSV file and prepares it to be uploaded to the MongoDB")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadArtistData(@RequestParam("file") MultipartFile file) {
        try {
            List<Artist> artists = parseTsvFile(file);
            service.saveAll(artists);
            return ResponseEntity.ok("Artist data successfully uploaded and saved.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error processing file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving artist data: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Update artist dataset",
            description = "Takes a .TSV file and prepares it to be uploaded to the MongoDB and update all the necessary entries and create new entries for new artists")
    @PutMapping("/upload")
    public ResponseEntity<String> updateMovies(@RequestParam("file") MultipartFile file) {
        try {
            List<Artist> artists = parseTsvFile(file);
            logger.info(artists.toString());
            service.updateArtists(artists);
            return ResponseEntity.ok("Artists updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // This function is to parse the TSV file, so it will spit out a useful JSON. The roleInMovie will be parsed in a seperate function.
    private List<Artist> parseTsvFile(MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            List<Artist> artists = new ArrayList<>();
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\t");
                if (fields.length < 3) {
                    // Skip invalid lines
                    continue;
                }

                Artist artist = new Artist();
                artist.setNconst(fields[0]);
                artist.setPrimaryName(fields[1]);
                String roleInMovieJson = fields[2];
                List<Artist.RoleInMovie> roles = parseRoleInMovieJson(roleInMovieJson);
                artist.setRolesInMovies(roles);
                artists.add(artist);
            }

            return artists;
        }

    }

    // Takes the String of RoleInMovie and parses and cleans up the data, including some outliers in characters and job.
    private List<Artist.RoleInMovie> parseRoleInMovieJson(String roleInMovieJson) {
        try {
            String cleanedJson = roleInMovieJson
                    .replace("'", "\"")
                    .replace("\"[", "[")
                    .replace("]\"", "]")
                    .replace("\"]", "]")
                    .replace("\"[", "[")
                    .replace("\\\"", "\"")
                    .replace("\"\"", "\"");

            cleanedJson = cleanField(cleanedJson, "characters");
            cleanedJson = cleanField(cleanedJson, "job");
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(cleanedJson, new TypeReference<List<Artist.RoleInMovie>>() {
            });

        } catch (Exception e) {
            logger.error("Error parsing roleInMovieJson: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    // This function will clean a field of " and unnecessary spaces
    private String cleanField(String json, String field) {
        Pattern pattern;
        if (field.equals("characters")) {
            pattern = Pattern.compile("\"" + "characters" + "\":\\s*\\[\\s*\"(.*?)\"\\s*\\]");
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
