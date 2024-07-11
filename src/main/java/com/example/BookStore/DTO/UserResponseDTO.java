package com.example.BookStore.DTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Integer id;

    private String username;

    private String email;

    private String name;

    private String phone;

    private String address;

    private LocalDateTime dateCreate;

    private LocalDateTime dateUpdate;

    private CartResponseDTO cart;

    private List<OrderResponseDTO> orders;
}
