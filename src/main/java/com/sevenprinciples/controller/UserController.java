package com.sevenprinciples.controller;

import com.sevenprinciples.entity.AuthUser;
import com.sevenprinciples.entity.Role;
import com.sevenprinciples.repository.PrivilegeRepository;
import com.sevenprinciples.repository.RoleRepository;
import com.sevenprinciples.repository.UserRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name="Account", description="the Account API")
@AllArgsConstructor
@RequestMapping("/api/account")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

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
            Role adminRole = roleRepository.findByName("USER");
            List<Role> role = Arrays.asList(adminRole);
            user.setRoles(role);
            userRepository.save(user);
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

            userRepository.findByUsername(username);

            return ResponseEntity.ok("{\"message\": \"Login successful\"}");
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Data
    private static class LoginRequest {
        private String username;
        private String password;
    }
}
