package com.example.BookStore.DTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private int id;
    private LocalDateTime dateCreate;
    private LocalDateTime dateUpdate;
    //- Input
    private int user;
    private List<?> books;
    private String name;
    private String phone;
    private String address;
}
