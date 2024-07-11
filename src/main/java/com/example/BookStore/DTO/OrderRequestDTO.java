package com.example.BookStore.DTO;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    private Integer userId;
    private List<String> bookIds;
    private List<String> quantities;
}
