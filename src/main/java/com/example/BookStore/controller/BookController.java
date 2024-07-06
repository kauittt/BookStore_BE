package com.example.BookStore.controller;

import com.example.BookStore.DTO.BookDTO;
import com.example.BookStore.model.Response;
import com.example.BookStore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable String id) {
        try {
            BookDTO bookDTO = bookService.getBookById(id);
            return ResponseEntity.ok(bookDTO);
        } catch (Exception ex) {
            Response response = Response.of(HttpStatus.NOT_FOUND, ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody BookDTO bookDTO) {
        try {
            BookDTO savedBook = bookService.createBook(bookDTO);
            return ResponseEntity.ok(savedBook);
        } catch (Exception ex) {
            Response response = Response.of(HttpStatus.BAD_REQUEST, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable String id, @RequestBody BookDTO bookDTO) {
        try {
            BookDTO savedBook = bookService.updateBook(id, bookDTO);
            return ResponseEntity.ok(savedBook);
        } catch (Exception ex) {
            Response response = Response.of(HttpStatus.BAD_REQUEST, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable String id) {
        try {
            BookDTO bookDTO = bookService.deleteBook(id);
            return ResponseEntity.ok(bookDTO);
        } catch (Exception ex) {
            Response response = Response.of(HttpStatus.NOT_FOUND, ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
