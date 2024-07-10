package com.example.BookStore.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Embeddable
public class OrderBookId implements Serializable {
    // Getters and Setters
    private Integer orderId;
    private Integer bookId;

    // Constructors, getters, setters, equals and hashCode methods
    public OrderBookId() {}

    public OrderBookId(Integer orderId, Integer bookId) {
        this.orderId = orderId;
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderBookId that = (OrderBookId) o;
        return Objects.equals(orderId, that.orderId) && Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, bookId);
    }
}
