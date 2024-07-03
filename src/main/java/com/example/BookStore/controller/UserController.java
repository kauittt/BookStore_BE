package com.example.BookStore.controller;

import com.example.BookStore.model.Response;
import com.example.BookStore.model.User;
import com.example.BookStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/register",  consumes = {"application/json"})
    public ResponseEntity<Response> registerUser(@RequestBody User user) {
        Response response = new Response();
        try {
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            user.setDateCreated(LocalDateTime.now());
            user.setDateUpdated(LocalDateTime.now());
            user.setEnabled(1);

            User savedUser = userService.saveUser(user);

            if (savedUser.getId() > 0) {
                response.setStatus(HttpStatus.CREATED.value());
                response.setTimestamp(String.valueOf(System.currentTimeMillis()));
                response.setMessage("User registered successfully");
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setTimestamp(String.valueOf(System.currentTimeMillis()));
                response.setMessage("User registration failed");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception exception) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setTimestamp(String.valueOf(System.currentTimeMillis()));
            response.setMessage("Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User userDetails) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            User updatedUser = user.get();
            updatedUser.setUsername(userDetails.getUsername());
            updatedUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            updatedUser.setDateUpdated(LocalDateTime.now());
            updatedUser.setEnabled(userDetails.getEnabled());
            // Update other fields as needed

            return ResponseEntity.ok(userService.saveUser(updatedUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
