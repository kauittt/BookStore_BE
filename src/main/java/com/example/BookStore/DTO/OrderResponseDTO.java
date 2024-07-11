package com.example.BookStore.DTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
    private Integer id;
    private LocalDateTime dateCreate;
    private LocalDateTime dateUpdate;
    private List<OrderBookDTO> books;
    private String name;
    private String phone;
    private String address;
}
