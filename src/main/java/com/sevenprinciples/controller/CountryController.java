package com.sevenprinciples.controller;

import com.sevenprinciples.entity.Country;
import com.sevenprinciples.service.CountryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@CrossOrigin(origins = "http://localhost:3000")
@Tag(name="Country", description="the Country API")
@RestController
@RequestMapping("/api/countries")
public class CountryController {

    @Autowired
    private final CountryServiceImpl service;

    public CountryController(CountryServiceImpl service) {
        this.service = service;
    }

    @Operation(
            summary = "Fetch all countries",
            description = "Fetches a collection of all the countries in the database")
    @GetMapping("/")
    public ResponseEntity<Collection<Country>> findCountries() throws Exception {
        Collection<Country> countries = service.getCountries();
        if (countries != null) {
            return new ResponseEntity<>(countries, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(
            summary = "Fetch a specific country",
            description = "Fetches a specific country with the corresponding id")
    @GetMapping("/{id}")
    public ResponseEntity<Country> findById(@PathVariable String id) throws Exception {
        Country country = service.findById(id);
        if (country != null) {
            return new ResponseEntity<>(country, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(
            summary = "Create a new country",
            description = "Creates a new country entry, whereas the id, name and countryCode is needed.")
    @PostMapping("/")
    public ResponseEntity<Country> setCountry(@RequestBody Country country) throws Exception {
        if (country != null) {
            service.setCountry(country);
            return new ResponseEntity<>(country, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(
            summary = "Updates a country entry",
            description = "Updates a country entry via the id. You can change the name and/or countryCode.")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> updateCountry(@PathVariable("id") final String id, @RequestBody final Country country) {
        try {
            service.updateCountry(id, country);
            return ResponseEntity.ok("Land erfolgreich aktualisiert.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ein Fehler ist aufgetreten.");
        }
    }

    @Operation(
            summary = "Delete a country entry",
            description = "Delets a country entry via the id.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCountry(@PathVariable String id) {
        try {
            service.deleteCountry(id);
            return ResponseEntity.ok("Land wurde erfolgreich gelöscht.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ein Fehler ist aufgetreten.");
        }
    }
}