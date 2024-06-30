package com.example.BookStore.repository;

import com.example.BookStore.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(path = "carts")
public interface CartRepository extends JpaRepository<Cart, String> {
}
