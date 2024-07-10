package com.example.BookStore.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartRequestDTO {
    private List<?> books;
    private List<Integer> quantities;
}
