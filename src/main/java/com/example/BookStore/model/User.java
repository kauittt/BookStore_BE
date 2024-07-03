package com.example.BookStore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private int enabled;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "dateCreated")
    private LocalDateTime dateCreated;

    @Column(name = "dateUpdated")
    private LocalDateTime dateUpdated;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
//    @JsonBackReference(value = "user-authority")
    @JsonIgnore
    @JoinTable(
            name = "users_authorities",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "authorityId")
    )
    private List<Authority> authorities;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
//    @JsonManagedReference(value = "user-cart")
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    @JsonManagedReference(value = "user-order")
    private List<Order> orders;
}
