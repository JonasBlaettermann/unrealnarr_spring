package com.unrealnarr.controller;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.unrealnarr.entity.Critic;
import com.unrealnarr.service.CriticsServiceImpl;
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

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name="Critics", description="the Critics API")
@AllArgsConstructor
@RequestMapping("/api/critics")
public class CriticsController {

    @Autowired
    private final CriticsServiceImpl service;

    @Operation(
            summary = "Fetch all critics",
            description = "Fetches a collection of all the critics in the database")
    @GetMapping("/")
    public ResponseEntity<Collection<Critic>> findCritics() throws Exception {

        Collection<Critic> critics = service.getCritics();
        if (critics != null) {
            return new ResponseEntity<>(critics, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(
            summary = "Fetch a specific critic",
            description = "Fetches a specific critic with the corresponding id")
    @GetMapping("/{id}")
    public ResponseEntity<Critic> findById(@PathVariable String id) throws Exception {
        Critic critic = service.findById(id);
        if (critic != null) {
            return new ResponseEntity<>(critic, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}


