package com.example.BookStore.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderBookDTO {
    private BookDTO book;
    private int orderQuantity;
}
