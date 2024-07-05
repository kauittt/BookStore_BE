package com.example.BookStore.DTO;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistryDTO {
    private String username;
    private String password;
}
