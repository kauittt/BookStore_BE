package com.example.BookStore.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Integer id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer user;

    private List<?> books;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> quantities;

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
