package com.example.BookStore.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Embeddable
public class CartBookId implements Serializable {
    private Integer cartId;
    private Integer bookId;

    public CartBookId() {}

    public CartBookId(Integer cartId, Integer bookId) {
        this.cartId = cartId;
        this.bookId = bookId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartBookId that = (CartBookId) o;
        return Objects.equals(cartId, that.cartId) && Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, bookId);
    }
}
