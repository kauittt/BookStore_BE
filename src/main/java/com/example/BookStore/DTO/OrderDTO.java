package com.example.BookStore.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Integer user;
    private List<?> books;
    private String name;
    private String phone;
    private String address;

    @JsonIgnore
    public Integer getUser() {
        return user;
    }
}
