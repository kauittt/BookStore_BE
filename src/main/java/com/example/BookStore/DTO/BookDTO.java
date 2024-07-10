package com.example.BookStore.DTO;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private Integer id;

    private String name;

    private String image;

    private String description;

    private double price;

    private String author;

    private int quantity ;

//    private int cartQuantity ;

}
