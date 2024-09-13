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
            summary = "Fetch all critics",
            description = "Fetches a collection of all the critics in the database")
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

                String nconst = fields[0];
                String primaryName = fields[1];
                String roleInMovieJson = fields[2];

                List<Artist.RoleInMovie> roles = parseRoleInMovieJson(roleInMovieJson);

                Artist artist = new Artist();
                artist.setNconst(nconst);
                artist.setPrimaryName(primaryName);
                artist.setRolesInMovies(roles);

                artists.add(artist);
            }

            return artists;
        }

    }

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

            cleanedJson = cleanCharacters(cleanedJson);
            cleanedJson = cleanJobs(cleanedJson);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(cleanedJson, new TypeReference<List<Artist.RoleInMovie>>() {});

        } catch (Exception e) {
            logger.error("Error parsing roleInMovieJson: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    private String cleanCharacters(String json) {
        Pattern pattern = Pattern.compile("\"" + "characters" + "\":\\s*\\[\\s*\"(.*?)\"\\s*\\]");
        Matcher matcher = pattern.matcher(json);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String fieldContent = matcher.group(1);

            String fixedContent = fieldContent
                    .replace("\"", "\\´")
                    .replace("\"\"", "\"");

            matcher.appendReplacement(sb, "\"" + "characters" + "\": \"" + fixedContent + "\"");
        }
        matcher.appendTail(sb);

        return removeSegmentContent(sb.toString());
    }

    private String removeSegmentContent(String fieldContent) {
        Pattern segmentPattern = Pattern.compile("\\(segment[^)]*\\)");
        Matcher segmentMatcher = segmentPattern.matcher(fieldContent);

        String cleanedContent = segmentMatcher.replaceAll("");

        cleanedContent = cleanedContent.replace("\"\"", "\"");
        cleanedContent = cleanedContent.replaceAll("\\s*,\\s*", ",");
        cleanedContent = cleanedContent.replaceAll("^,|,$", "");

        return cleanedContent;
    }

    private String cleanJobs(String json) {
        Pattern pattern = Pattern.compile("\"job\":\\s*\"(.*?)\"(?=[,}\\]])");
        Matcher matcher = pattern.matcher(json);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String fieldContent = matcher.group(1);

            String fixedContent = fieldContent
                    .replace("\"", "\\´")
                    .replace("\\\"", "\"");

            matcher.appendReplacement(sb, "\"job\": \"" + fixedContent + "\"");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
