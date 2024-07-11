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
import java.util.stream.Collectors;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookDTO toDTO(Book book);

    Book toEntity(BookDTO bookDTO);

    @Mapping(source = "quantity", target = "cartQuantity")
    CartBookDTO toCartBookDTO(Book book);

    //- Convert List<Book> -> List<BookDTO>:
    //! Dùng khi RESPONSE
    default List<BookDTO> toListDTO(List<Book> books) {
        return books != null ? books.stream()
                .map(this::toDTO)
                .collect(Collectors.toList()) : Collections.emptyList();
    }

    //- Convert List<ID> -> List<Book>:
    //! Dùng khi REQUEST
    default List<Book> toListEntity(List<?> bookIds, BookRepository bookRepository) {

        return bookIds.stream()
                .map(bookId -> bookRepository.findById((String) bookId)
                        .orElseThrow(() -> new RuntimeException(Response.notFound("Book", (String) bookId))))
                .toList();
    }


    //! Custom
    default List<CartBookDTO> toCartBookDTOList(List<Book> books) {
        return books.stream()
                .map(book -> {
                    CartBookDTO cartBookDTO = toCartBookDTO(book);
                    cartBookDTO.setBook(toDTO(book));

                    return cartBookDTO;
                })
                .collect(Collectors.toList());
    }
}
