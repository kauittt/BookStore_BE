package com.example.BookStore.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer user;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> quantities;
    private List<?> books;
    private String name;
    private String phone;
    private String address;

//    @JsonIgnore
//    public Integer getUser() {
//        return user;
//    }
//
//    @JsonIgnore
//    public List<Integer> getQuantities() {
//        return quantities;
//    }
}
