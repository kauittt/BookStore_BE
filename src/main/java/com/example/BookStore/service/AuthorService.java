package com.example.BookStore.service;

import com.example.BookStore.DTO.AuthorDTO;
import com.example.BookStore.DTO.BookDTO;
import com.example.BookStore.mapstruct.AuthorMapper;
import com.example.BookStore.mapstruct.BookMapper;
import com.example.BookStore.model.Author;
import com.example.BookStore.model.Book;
import com.example.BookStore.model.Response;
import com.example.BookStore.repository.AuthorRepository;
import com.example.BookStore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorMapper authorMapper = AuthorMapper.INSTANCE;
    private final BookMapper bookMapper = BookMapper.INSTANCE;

    @Autowired
    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public List<AuthorDTO> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();

        return authors.stream().map(author -> {
            AuthorDTO authorDTO = authorMapper.toDTO(author);

            List<BookDTO> bookDTOs = author.getBooks() != null ? author.getBooks().stream()
                    .map(bookMapper::toDTO)
                    .collect(Collectors.toList()) : Collections.emptyList();

            authorDTO.setBooks(bookDTOs);

            return authorDTO;
        }).collect(Collectors.toList());
    }

    public AuthorDTO getAuthorById(String id) {
        return authorRepository.findById(id)
                .map(authorMapper::toDTO)
                .orElseThrow(() -> new RuntimeException(Response.notFound("Author", id)));
    }

    //- Đã chia rõ create-update ở dưới
//    @Transactional
//    public AuthorDTO saveAuthor(AuthorDTO authorDTO) {
//        Author author = authorMapper.authorDTOToAuthor(authorDTO);
//        Author savedAuthor = authorRepository.save(author);
//        return authorMapper.authorToAuthorDTO(savedAuthor);
//    }

    @Transactional
    public AuthorDTO deleteAuthor(String id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(Response.notFound("Author", id)));

        authorRepository.delete(author);
        return authorMapper.toDTO(author);
    }

    @Transactional
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        Author author = authorMapper.toEntity(authorDTO);

        List<Book> books = authorDTO.getBooks().stream()
                .map(bookId -> bookRepository.findById(String.valueOf(bookId))
                        .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookId)))
                .collect(Collectors.toList());

        books.forEach(book -> book.getAuthors().add(author));
        author.setBooks(books);

        return authorMapper.toDTO(authorRepository.save(author));
    }

    @Transactional
    public AuthorDTO updateAuthor(String id, AuthorDTO authorDTO) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with ID: " + id));

        author.setName(authorDTO.getName());

        List<Book> books = authorDTO.getBooks().stream()
                .map(bookId -> bookRepository.findById(String.valueOf(bookId))
                        .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookId)))
                .collect(Collectors.toList());

        author.setBooks(books);

        return authorMapper.toDTO(authorRepository.save(author));
    }
}


