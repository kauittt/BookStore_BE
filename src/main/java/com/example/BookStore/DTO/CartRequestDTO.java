package com.example.BookStore.DTO;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartRequestDTO {
    private List<String> bookIds;
    private List<String> quantities;
}
