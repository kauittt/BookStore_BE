package com.example.BookStore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "books")
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private String image;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @Column(name = "author")
    private String author;

    @Column(name = "quantity")
    private int quantity;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartBook> cartBooks;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderBook> orderBooks;

//    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
//    @JoinTable(
//            name = "carts_books",
//            joinColumns = @JoinColumn(name = "bookId"),
//            inverseJoinColumns = @JoinColumn(name = "cartId")
//    )
////    @JsonBackReference(value = "cart-book")
////    @JsonIgnore
//    private List<Cart> carts;




//    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
//    @JoinTable(
//            name = "orders_books",
//            joinColumns = @JoinColumn(name = "bookId"),
//            inverseJoinColumns = @JoinColumn(name = "orderId")
//    )
////    @JsonBackReference(value = "order-book")
////    @JsonIgnore
//    private List<Order> orders;


}
