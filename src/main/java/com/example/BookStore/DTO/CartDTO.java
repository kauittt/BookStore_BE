package com.example.BookStore.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    public Integer getUser() {
        return user;
    }
}
