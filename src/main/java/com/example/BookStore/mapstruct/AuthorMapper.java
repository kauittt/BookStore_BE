package com.example.BookStore.mapstruct;

import com.example.BookStore.DTO.AuthorDTO;
import com.example.BookStore.DTO.BookDTO;
import com.example.BookStore.model.Author;
import com.example.BookStore.model.Book;
import com.example.BookStore.model.Response;
import com.example.BookStore.repository.BookRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface AuthorMapper {
    BookMapper bookMapper = BookMapper.INSTANCE;

    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    @Mapping(source = "books", target = "books", ignore = true)
    AuthorDTO toDTO(Author author);

    @Mapping(source = "books", target = "books", ignore = true)
    Author toEntity(AuthorDTO authorDTO);

    default AuthorDTO toDTOWithBooks(Author author) {
        AuthorDTO authorDTO = toDTO(author);

        List<BookDTO> bookDTOs = author.getBooks() != null ? author.getBooks().stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList()) : Collections.emptyList();

        authorDTO.setBooks(bookDTOs);

        return authorDTO;
    }

    default Author toEntityWithBooks(AuthorDTO authorDTO, BookRepository bookRepository) {
        Author author = toEntity(authorDTO);

        List<Book> books = authorDTO.getBooks().stream()
                .map(bookId -> bookRepository.findById((String) bookId)
                        .orElseThrow(() -> new RuntimeException(Response.notFound("Book", (String) bookId))))
                .toList();

        for (Book book : books) {
            if (!book.getAuthors().contains(author)) {
                book.getAuthors().add(author);
            }
        }
        author.setBooks(books);

        return author;
    }
}
