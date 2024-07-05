package com.example.BookStore.controller;

import com.example.BookStore.DTO.CartDTO;
import com.example.BookStore.model.Response;
import com.example.BookStore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        return ResponseEntity.ok(cartService.getAllCarts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCartById(@PathVariable String id) {
        try {
            CartDTO cartDTO = cartService.getCartById(id);
            return ResponseEntity.ok(cartDTO);
        } catch (Exception ex) {
            Response response = Response.of(HttpStatus.NOT_FOUND, ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/add/{id}")
    public ResponseEntity<?> addBooksByIds(@PathVariable String id, @RequestBody CartDTO cartDTO) {
        try {
            CartDTO updatedCart = cartService.addBooksByIds(id, cartDTO);
            return ResponseEntity.ok(updatedCart);
        } catch (Exception ex) {
            Response response = Response.of(HttpStatus.BAD_REQUEST, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/remove/{id}")
    public ResponseEntity<?> removeBooksByIds(@PathVariable String id, @RequestBody CartDTO cartDTO) {
        try {
            CartDTO updatedCart = cartService.removeBooksByIds(id, cartDTO);
            return ResponseEntity.ok(updatedCart);
        } catch (Exception ex) {
            Response response = Response.of(HttpStatus.BAD_REQUEST, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/clean/{id}")
    public ResponseEntity<?> cleanCart(@PathVariable String id) {
        try {
            CartDTO cartDTO = cartService.cleanCart(id);
            return ResponseEntity.ok(cartDTO);
        } catch (Exception ex) {
            Response response = Response.of(HttpStatus.NOT_FOUND, ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
