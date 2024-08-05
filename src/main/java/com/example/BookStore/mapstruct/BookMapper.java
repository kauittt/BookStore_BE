package com.example.BookStore.mapstruct;

import com.example.BookStore.DTO.BookDTO;
import com.example.BookStore.DTO.CartBookDTO;
import com.example.BookStore.model.Book;
import com.example.BookStore.model.Response;
import com.example.BookStore.repository.BookRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    @Mapping(source = "category", target = "category")
    BookDTO toDTO(Book book);

    Book toEntity(BookDTO bookDTO);

    @Mapping(source = "quantity", target = "cartQuantity")
    CartBookDTO toCartBookDTO(Book book);

    //- Convert List<Book> -> List<BookDTO>:
    //! Dùng khi RESPONSE
    default List<BookDTO> toListDTO(List<Book> books) {
        return books != null ? books.stream().map(this::toDTO).collect(Collectors.toList()) : Collections.emptyList();
    }

    //- Convert List<ID> -> List<Book>:
    //! Dùng khi REQUEST
    default List<Book> toListEntity(List<?> bookIds, BookRepository bookRepository) {
        // Check if the input list is null or empty
        if (bookIds == null || bookIds.isEmpty()) {
            return Collections.emptyList(); // Return an empty list if the input is null or empty
        }

        return bookIds.stream()
                .filter(Objects::nonNull) // Filter out any null IDs to avoid NullPointerException
                .map(bookId -> bookRepository.findById((String) bookId)
                        .orElseThrow(() -> new RuntimeException(Response.notFound("Book", (String) bookId))))
                .collect(Collectors.toList());
    }


    //! Custom
    default List<CartBookDTO> toCartBookDTOList(List<Book> books) {
        // Check if the books list is null or empty
        if (books == null || books.isEmpty()) {
            return Collections.emptyList(); // Return an empty list if the input is null or empty
        }

        return books.stream()
                .filter(Objects::nonNull) // Filter out any null Book objects
                .map(book -> {
                    CartBookDTO cartBookDTO = toCartBookDTO(book);
                    cartBookDTO.setBook(toDTO(book));
                    return cartBookDTO;
                })
                .collect(Collectors.toList());
    }
}
