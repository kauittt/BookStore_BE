package com.example.BookStore.repository;

import com.example.BookStore.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    @Query(value = "SELECT * FROM carts where userId = :userId", nativeQuery = true)
    Cart findCartByUserId(@Param("userId") String username);
}
