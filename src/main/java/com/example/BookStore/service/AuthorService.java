//package com.example.BookStore.service;
//
//import com.example.BookStore.DTO.AuthorDTO;
//import com.example.BookStore.mapstruct.AuthorMapper;
//import com.example.BookStore.mapstruct.BookMapper;
//import com.example.BookStore.model.Author;
//import com.example.BookStore.model.Book;
//import com.example.BookStore.model.Response;
//import com.example.BookStore.repository.AuthorRepository;
//import com.example.BookStore.repository.BookRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Service
//public class AuthorService {
//    private final AuthorRepository authorRepository;
//    private final BookRepository bookRepository;
//    private final AuthorMapper authorMapper = AuthorMapper.INSTANCE;
//    private final BookMapper bookMapper = BookMapper.INSTANCE;
//
//    @Autowired
//    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
//        this.authorRepository = authorRepository;
//        this.bookRepository = bookRepository;
//    }
//
//    public List<AuthorDTO> getAllAuthors() {
//        List<Author> authors = authorRepository.findAll();
//        return authors.stream().map(authorMapper::toDTOWithBooks).collect(Collectors.toList());
//    }
//
//    public AuthorDTO getAuthorById(String id) {
//        return authorRepository.findById(id)
//                .map(authorMapper::toDTOWithBooks)
//                .orElseThrow(() -> new RuntimeException(Response.notFound("Author", id)));
//    }
//
//    @Transactional
//    public AuthorDTO deleteAuthor(String id) {
//        Author author = authorRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException(Response.notFound("Author", id)));
//
//        //- Remove Book's references
//        for (Book book : author.getBooks()) {
//            book.getAuthors().remove(author);
//        }
//        author.getBooks().clear();
//
//        authorRepository.delete(author);
//        return authorMapper.toDTO(author);
//    }
//
//    @Transactional
//    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
//        Author author = authorMapper.toEntityWithBooks(authorDTO, bookRepository);
//        return authorMapper.toDTOWithBooks(authorRepository.save(author));
//    }
//
//    @Transactional
//    public AuthorDTO updateAuthor(String id, AuthorDTO authorDTO) {
//        Author authorDB = authorRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException(Response.notFound("Author", id)));
//
//        authorDB.setName(authorDTO.getName());
//
//        List<Book> newBooks = authorDTO.getBooks().stream()
//                .map(bookId -> bookRepository.findById((String) bookId)
//                        .orElseThrow(() -> new RuntimeException(Response.notFound("Book", (String) bookId))))
//                .toList();
//
//        Set<Book> currentBooks = new HashSet<>(authorDB.getBooks());
//        for (Book book : currentBooks) {
//            if (!newBooks.contains(book)) {
//                book.getAuthors().remove(authorDB);
//            }
//        }
//
//        authorDB.getBooks().clear();
//
//        for (Book book : newBooks) {
//            if (!book.getAuthors().contains(authorDB)) {
//                book.getAuthors().add(authorDB);
//            }
//            authorDB.getBooks().add(book);
//        }
//
//        Author savedAuthor = authorRepository.save(authorDB);
//        return authorMapper.toDTOWithBooks(savedAuthor);
//    }
//}
//
//
