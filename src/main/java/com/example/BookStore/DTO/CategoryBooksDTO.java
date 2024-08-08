package com.example.BookStore.DTO;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CategoryBooksDTO {
    private String category;
    private List<BookDTO> books;
}
