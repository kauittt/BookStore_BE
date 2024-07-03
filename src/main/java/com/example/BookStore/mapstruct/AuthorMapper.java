package com.example.BookStore.mapstruct;

import com.example.BookStore.DTO.AuthorDTO;
import com.example.BookStore.model.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthorMapper {

    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    @Mapping(source = "books", target = "books", ignore = true)
    AuthorDTO toDTO(Author author);

    @Mapping(source = "books", target = "books", ignore = true)
    Author toEntity(AuthorDTO authorDTO);
}
