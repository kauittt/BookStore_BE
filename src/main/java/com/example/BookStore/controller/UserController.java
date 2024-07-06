package com.example.BookStore.controller;

import com.example.BookStore.DTO.UserRegistryDTO;
import com.example.BookStore.DTO.UserResponseDTO;
import com.example.BookStore.model.Response;
import com.example.BookStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping(value = "/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistryDTO userRegistryDTO) {
        try {
            UserResponseDTO savedUser = userService.createUser(userRegistryDTO);

            return ResponseEntity.ok(savedUser);
        } catch (Exception ex) {
            Response response = Response.of(HttpStatus.BAD_REQUEST, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            UserResponseDTO userResponseDTO = userService.getUserById(id);
            return ResponseEntity.ok(userResponseDTO);
        } catch (Exception ex) {
            Response response = Response.of(HttpStatus.NOT_FOUND, ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody UserResponseDTO userResponseDTO) {
        try {
            UserResponseDTO savedUser = userService.updateUser(id, userResponseDTO);
            return ResponseEntity.ok(savedUser);
        } catch (Exception ex) {
            Response response = Response.of(HttpStatus.BAD_REQUEST, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            UserResponseDTO userResponseDTO = userService.deleteUser(id);
            return ResponseEntity.ok(userResponseDTO);
        } catch (Exception ex) {
            Response response = Response.of(HttpStatus.NOT_FOUND, ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
