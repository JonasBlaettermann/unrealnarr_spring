package com.unrealnarr.controller;

import com.unrealnarr.entity.Protocol;
import com.unrealnarr.service.ProtocolServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@CrossOrigin(origins = "http://localhost:3000")
@Tag(name="Protocol", description="the Protocol API")
@RestController
@RequestMapping("/api/protocols")
public class ProtocolController {

    @Autowired
    private final ProtocolServiceImpl service;

    public ProtocolController(ProtocolServiceImpl service) { this.service = service; }

    @Operation(
            summary = "Fetch the full protocol",
            description = "Fetches the full protocol with all the information")
    @GetMapping("/")
    public ResponseEntity<Collection<Protocol>> getProtocol() throws Exception {
        Collection<Protocol> protocols = service.getProtocols();
        if (protocols != null) {
            return new ResponseEntity<>(protocols, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(
            summary = "Create a new protocol entry",
            description = "Creates a protocol entry")
    @PostMapping("/")
    public ResponseEntity<Protocol> setProtocol(@RequestBody Protocol protocol) throws Exception {
        if (protocol != null) {
            service.addToProtocol(protocol);
            return new ResponseEntity<>(protocol, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}



