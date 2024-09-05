package com.unrealnarr.controller;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.unrealnarr.entity.Critics;
import com.unrealnarr.service.CriticsServiceImpl;
import com.unrealnarr.service.CsvValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name="Critics", description="the Critics API")
@AllArgsConstructor
@RequestMapping("/api/critics")
public class CriticsController {

    @Autowired
    private CsvValidationService validationService;

    @Autowired
    private final CriticsServiceImpl service;

    @Operation(
            summary = "Fetch all critics",
            description = "Fetches a collection of all the critics in the database")
    @GetMapping("/")
    public ResponseEntity<Collection<Critics>> findCritics() throws Exception {

        Collection<Critics> critics = service.getCritics();
        if (critics != null) {
            return new ResponseEntity<>(critics, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(
            summary = "Fetch a specific critic",
            description = "Fetches a specific critic with the corresponding id")
    @GetMapping("/{id}")
    public ResponseEntity<Critics> findById(@PathVariable String id) throws Exception {
        Critics critic = service.findById(id);
        if (critic != null) {
            return new ResponseEntity<>(critic, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Operation(
            summary = "Upload a list of critics",
            description = "Uploads a JSON file containing a list of critics to be stored in the database")
    @PostMapping("/")
    public ResponseEntity<String> uploadCritics(@RequestBody List<Critics> criticsList) {
        try {
            service.saveAll(criticsList);
            return new ResponseEntity<>("Upload erfolgreich!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Upload fehlgeschlagen: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Upload CSV file",
            description = "Uploads a CSV file and stores its contents in the database")
    @PostMapping("/upload-csv")
    public ResponseEntity<String> uploadCsvFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Bitte wählen Sie eine CSV-Datei aus.", HttpStatus.BAD_REQUEST);
        }

        try (InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream())) {
            // Erster Durchlauf: Validierung der CSV-Struktur
            try (CSVReader reader = new CSVReader(inputStreamReader)) {
                validationService.validateCsvFile(reader);
            }

            // Zweiter Durchlauf: Verarbeiten der CSV-Daten
            try (InputStreamReader inputStreamReaderForParsing = new InputStreamReader(file.getInputStream())) {
                try (CSVReader readerForParsing = new CSVReader(inputStreamReaderForParsing)) {
                    CsvToBean<Critics> csvToBean = new CsvToBeanBuilder<Critics>(readerForParsing)
                            .withType(Critics.class)
                            .withIgnoreLeadingWhiteSpace(true)
                            .build();

                    List<Critics> criticsList = csvToBean.parse();

                    // Optional: Validierung jedes Critics-Objekts, falls Methode vorhanden
                    // criticsList.forEach(critic -> validationService.validateCritic(critic));

                    // Speichern aller Critics in der Datenbank
                    service.saveAll(criticsList);

                    return new ResponseEntity<>("CSV-Datei erfolgreich hochgeladen und gespeichert!", HttpStatus.OK);
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace(); // Hilft beim Debuggen
            return new ResponseEntity<>("Fehler beim Verarbeiten der CSV-Datei: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}


