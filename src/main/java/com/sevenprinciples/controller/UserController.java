package com.sevenprinciples.controller;

import com.sevenprinciples.config.JwtProvider;
import com.sevenprinciples.entity.AuthResponse;
import com.sevenprinciples.entity.AuthUser;
import com.sevenprinciples.entity.Protocol;
import com.sevenprinciples.entity.Role;
import com.sevenprinciples.repository.RoleRepository;
import com.sevenprinciples.repository.UserRepository;
import com.sevenprinciples.service.ProtocolServiceImpl;
import com.sevenprinciples.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name="Account", description="the Account API")
@AllArgsConstructor
@RequestMapping("/api/account")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ProtocolController protocolController;

    @Autowired
    private ProtocolServiceImpl protocolService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Operation(
            summary = "Register a User",
            description = "Registers a user to the database via Username and Password")
    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody AuthUser user){
        try {
            if (userRepository.findByUsername(user.getUsername()).isPresent())
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already taken. Please try again");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setActive(true);
            Role role = roleRepository.findByName("USER");
            List<Role> roles = Collections.singletonList(role);
            user.setRoles(roles);
            userRepository.save(user);
            protocolService.addToProtocol(new Protocol("Registrieren", user.getUsername()));

            return ResponseEntity.ok(HttpStatus.CREATED);
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Signin a User",
            description = "One can signin as a User via Username and Password")
    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("UserController Authentication {}", authentication);
            String token = JwtProvider.generateToken(authentication);
            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage("Login successful");
            authResponse.setJwt(token);
            authResponse.setStatus(true);
            authResponse.setUser(userRepository.findByUsername(username).get());

            protocolService.addToProtocol(new Protocol("Login", username));

            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String userNamens = ((UserDetails)principal).getUsername();
            logger.info("UserController username {}", userNamens);
            logger.info("UserController Principal {}", principal);

            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(
            summary = "Signout a User",
            description = "A function to Singout a user")
    @PostMapping("/signout")
    public ResponseEntity<?> signout() {
        return ResponseEntity.ok("Ausgelogt");
    }

    @Operation(
            summary = "Fetch a specific User",
            description = "Fetches a specific user with the corresponding id")
    @GetMapping("/{id}")
    public ResponseEntity<AuthUser> findById(@PathVariable String id) throws Exception {
        AuthUser user = userRepository.findById(id).get();
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(
            summary = "Updates a user role ",
            description = "Updates a user  via the id.")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateCountry(@PathVariable("id") final String id, @RequestBody final String role) {
        try {
            AuthUser user = userRepository.findById(id).get();
            if(!user.getUsername().isEmpty()) {
                Role newRole = roleRepository.findByName("ADMIN");
                if(user.getRoles().stream().toList().getFirst().getName().equals("ADMIN")){
                    newRole = roleRepository.findByName("USER");
                }
                List<Role> roles = Collections.singletonList(newRole);;
                user.setRoles(roles);
                protocolService.addToProtocol(new Protocol("Changed the Role to " + newRole.getName(), user.getUsername()));
                userRepository.save(user);
            } else {
                throw new IllegalArgumentException("Es existiert der User mit der ID " + id + " nicht in der Datenbank");
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ein Fehler ist aufgetreten.");
        }
    }

    @Data
    private static class LoginRequest {
        private String username;
        private String password;
    }
}
