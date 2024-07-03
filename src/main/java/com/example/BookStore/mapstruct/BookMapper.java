package com.example.BookStore.mapstruct;

import com.example.BookStore.DTO.BookDTO;
import com.example.BookStore.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookDTO toDTO(Book book);

    Book toEntity(BookDTO bookDTO);
}
