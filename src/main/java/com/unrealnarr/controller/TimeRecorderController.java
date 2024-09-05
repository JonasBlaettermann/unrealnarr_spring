package com.unrealnarr.controller;

import com.unrealnarr.entity.Protocol;
import com.unrealnarr.entity.TimeRecorder;
import com.unrealnarr.service.ProtocolServiceImpl;
import com.unrealnarr.service.TimeRecorderServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "TimeRecorder", description = "the TimeRecorder API")
@AllArgsConstructor
@RequestMapping("/api/timerecorder")
public class TimeRecorderController {

    @Autowired
    private final TimeRecorderServiceImpl service;

    @Autowired
    private final ProtocolServiceImpl protocolService;

    @Operation(
            summary = "Fetch all time recorder data",
            description = "Fetches a collection of all the time recorder data in the database")
    @GetMapping("/")
    public ResponseEntity<Collection<TimeRecorder>> fetchTimeRecords() throws Exception {

        Collection<TimeRecorder> timeRecorder = service.getTimeRecorder();
        if (timeRecorder != null) {
            return new ResponseEntity<>(timeRecorder, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Operation(
            summary = "Create a new time record",
            description = "Creates a new time record entry, whereas the id, name and countryCode is needed.")
    @PostMapping("/")
    public ResponseEntity<TimeRecorder> setTimeRecord(@RequestBody TimeRecorder timeRecorder) throws Exception {
        if (timeRecorder != null) {
            service.setTimeRecorder(timeRecorder);
            protocolService.addToProtocol(new Protocol("Started a new time record at: " + timeRecorder.getStart(), getCurrentUsername()));
            return new ResponseEntity<>(timeRecorder, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(
            summary = "Updates a time record entry",
            description = "Updates a time record entry via the id.")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> updateTimeRecorder(@PathVariable("id") final String id, @RequestBody final TimeRecorder timeRecord) {
        try {
            service.updateTimeRecorder(id, timeRecord);
            protocolService.addToProtocol(new Protocol("Stopped time record at: " + timeRecord.getEnd(), getCurrentUsername()));
            return ResponseEntity.ok("TimeRecord " + timeRecord.getId() + " erfolgreich aktualisiert.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ein Fehler ist aufgetreten.");
        }
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
