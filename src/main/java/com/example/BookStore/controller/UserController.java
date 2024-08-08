package com.example.BookStore.controller;

import com.example.BookStore.DTO.JwtAuthResponse;
import com.example.BookStore.DTO.UserRegistryDTO;
import com.example.BookStore.DTO.UserResponseDTO;
import com.example.BookStore.mapstruct.UserMapper;
import com.example.BookStore.model.Response;
import com.example.BookStore.security.JWTGenerator;
import com.example.BookStore.service.CustomUserDetailsService;
import com.example.BookStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
//@CrossOrigin("http://localhost:5173")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTGenerator jwtGenerator;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager
            , JWTGenerator jwtGenerator, CustomUserDetailsService customUserDetailsService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
        this.customUserDetailsService = customUserDetailsService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody UserRegistryDTO userRegistryDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRegistryDTO.getUsername(), userRegistryDTO.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);
            String refreshToken = jwtGenerator.generateRefreshToken(authentication);
            return ResponseEntity.ok(new JwtAuthResponse(token, refreshToken));
        } catch (Exception ex) {
            Response response = Response.of(HttpStatus.BAD_REQUEST, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        try {
            String refreshToken = request.get("refreshToken");
            if (jwtGenerator.validateToken(refreshToken)) {
                String username = jwtGenerator.getUsernameFromJWT(refreshToken);
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, jwtGenerator.getAuthoritiesFromJWT(refreshToken));
                String newToken = jwtGenerator.generateToken(authentication);
                String newRefreshToken = jwtGenerator.generateRefreshToken(authentication);
                return ResponseEntity.ok(new JwtAuthResponse(newToken, newRefreshToken));
            } else {
                Response response = Response.of(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception ex) {
            Response response = Response.of(HttpStatus.BAD_REQUEST, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
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

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            UserResponseDTO userResponseDTO = userService.getUserById(id);
            return ResponseEntity.ok(userResponseDTO);
        } catch (Exception ex) {
            Response response = Response.of(HttpStatus.NOT_FOUND, ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        try {
            UserResponseDTO userResponseDTO = userMapper.toUserResponseDTOFull(userService.getUserByUsername(username));
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
