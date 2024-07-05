//package com.example.BookStore.controller;
//
//import com.example.BookStore.DTO.AuthorDTO;
//import com.example.BookStore.model.Response;
//import com.example.BookStore.service.AuthorService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
////! add empty []
//
//@RestController
//@RequestMapping("/authors")
//public class AuthorController {
//    private final AuthorService authorService;
//
//    @Autowired
//    public AuthorController(AuthorService authorService) {
//        this.authorService = authorService;
//    }
//
//    @GetMapping
//    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
//        return ResponseEntity.ok(authorService.getAllAuthors());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getAuthorById(@PathVariable String id) {
//        try {
//            AuthorDTO author = authorService.getAuthorById(id);
//            return ResponseEntity.ok(author);
//        } catch (Exception exception) {
//            Response response = Response.of(HttpStatus.NOT_FOUND, exception.getMessage());
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteAuthor(@PathVariable String id) {
//        try {
//            AuthorDTO deletedAuthor = authorService.deleteAuthor(id);
//            return ResponseEntity.ok(deletedAuthor);
//        } catch (Exception ex) {
//            Response response = Response.of(HttpStatus.NOT_FOUND, ex.getMessage());
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//        }
//    }
//
//    @PostMapping
//    public ResponseEntity<?> createAuthor(@RequestBody AuthorDTO authorDTO) {
//        try {
//            List<String> bookIdsAsString = authorDTO.getBooks().stream()
//                    .map(String::valueOf)
//                    .collect(Collectors.toList());
//            authorDTO.setBooks(bookIdsAsString);
//
//            AuthorDTO createdAuthor = authorService.createAuthor(authorDTO);
//            return ResponseEntity.ok(createdAuthor);
//        } catch (Exception ex) {
//            Response response = Response.of(HttpStatus.BAD_REQUEST, ex.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//        }
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateAuthor(@PathVariable String id, @RequestBody AuthorDTO authorDTO) {
//        try {
//            List<String> bookIdsAsString = authorDTO.getBooks().stream()
//                    .map(String::valueOf)
//                    .collect(Collectors.toList());
//            authorDTO.setBooks(bookIdsAsString);
//
//            AuthorDTO updatedAuthor = authorService.updateAuthor(id, authorDTO);
//            return ResponseEntity.ok(updatedAuthor);
//        } catch (Exception ex) {
//            Response response = Response.of(HttpStatus.NOT_FOUND, ex.getMessage());
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//        }
//    }
//}
//
//
//
