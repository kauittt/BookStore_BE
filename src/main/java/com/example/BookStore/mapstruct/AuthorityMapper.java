package com.example.BookStore.mapstruct;

import com.example.BookStore.DTO.AuthorityDTO;
import com.example.BookStore.model.Authority;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthorityMapper {
    AuthorityMapper INSTANCE = Mappers.getMapper(AuthorityMapper.class);

    AuthorityDTO todTO(Authority authority);

    Authority toEntity(AuthorityDTO authorityDTO);
}
