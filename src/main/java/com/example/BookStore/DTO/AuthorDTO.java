package com.example.BookStore.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDTO {
    private int id;
    private String name;
    //- Nhập mảng id để .map() dùng bookRepository.findById()
    private List<?> books;
}
