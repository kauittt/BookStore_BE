package com.example.BookStore.DTO;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Integer id;
    private Integer user;
    private List<?> books;
}
